# Monitoramento de Qualidade — Microsserviço

API REST para registrar leituras de sensores e gerenciar alertas de qualidade da Vinheria Agnello.

- **Porta:** 8084
- **Stack:** Java 17 + Jersey (JAX-RS) + Grizzly + Oracle JDBC
- **Rede Docker:** vinheria-network

---

## Rotas disponíveis

### Leituras de Sensor — `/leituras`

| Método | Rota      | Descrição                        |
|--------|-----------|----------------------------------|
| GET    | /leituras | Lista todas as leituras          |
| POST   | /leituras | Registra nova leitura de sensor  |

**Body do POST /leituras:**
```json
{
  "tipoSensor": "TEMPERATURA",
  "valor": 16.5,
  "localizacao": "Adega Principal",
  "registradoEm": "2026-11-08T18:00:00.000"
}
```

**Tipos de sensor aceitos:** `TEMPERATURA`, `UMIDADE`

**Limites válidos:**
- Temperatura: entre -10°C e 50°C
- Umidade: entre 0% e 100%

---

### Alertas — `/alertas`

| Método | Rota                      | Descrição                        |
|--------|---------------------------|----------------------------------|
| GET    | /alertas                  | Lista todos os alertas           |
| POST   | /alertas                  | Cria novo alerta                 |
| PATCH  | /alertas/{id}/{status}    | Atualiza status do alerta        |
| DELETE | /alertas/{id}             | Remove um alerta                 |

**Body do POST /alertas:**
```json
{
  "idLeituraSensor": 1,
  "severidade": "ALTA",
  "status": "ABERTO",
  "descricao": "Temperatura acima de 18C na Adega Principal",
  "criadoEm": "2026-11-08T18:00:00.000"
}
```

**Severidades aceitas:** `BAIXA`, `MEDIA`, `ALTA`, `CRITICA`

**Status aceitos:** `ABERTO`, `EM_ANDAMENTO`, `RESOLVIDO`, `DESCARTADO`

**Fluxo de transições de status:**
```
ABERTO → EM_ANDAMENTO → RESOLVIDO
                      → DESCARTADO
ABERTO → DESCARTADO

RESOLVIDO e DESCARTADO são estados finais — não permitem mais mudança.
```

**Exemplo de PATCH:**
```
PATCH /alertas/1/EM_ANDAMENTO
```

---

## Como configurar o banco de dados

Abra o arquivo abaixo e preencha com suas credenciais Oracle:

```
src/main/java/br/com/vinheria/qualidade/infra/DatabaseConnection.java
```

```java
private static final String URL     = "sua_url_oracle";
private static final String USUARIO = "seu_usuario";
private static final String SENHA   = "sua_senha";
```

### Tabelas necessárias no Oracle

Execute os scripts abaixo antes de subir o serviço:

```sql
-- Tabela de leituras de sensor
CREATE TABLE tb_leituras_sensor (
    id              NUMBER PRIMARY KEY,
    tipo_sensor     VARCHAR2(50) NOT NULL,
    valor           NUMBER(10,2) NOT NULL,
    localizacao     VARCHAR2(200),
    registrado_em   TIMESTAMP NOT NULL
);

CREATE SEQUENCE seq_tb_leituras_sensor START WITH 1 INCREMENT BY 1;

-- Tabela de alertas de qualidade
CREATE TABLE tb_alertas_qualidade (
    id                  NUMBER PRIMARY KEY,
    id_leitura_sensor   NUMBER NOT NULL,
    severidade          VARCHAR2(20) NOT NULL,
    status              VARCHAR2(20) NOT NULL,
    descricao           VARCHAR2(500),
    criado_em           TIMESTAMP
);

CREATE SEQUENCE seq_tb_alertas_qualidade START WITH 1 INCREMENT BY 1;
```

---

## Como rodar localmente (sem Docker)

```bash
mvn compile exec:java
```

A API sobe em `http://localhost:8084/`

---

## Como configurar o pipeline no Jenkins

> Pré-requisito: Jenkins rodando via docker-compose (veja o README raiz de vinheria7).
> Se você já configurou o Jenkins para o `gestao-pedidos`, pule para o Passo 6.

### Passo 1 — Acessar o Jenkins

```
http://localhost:8080
```

Se for a primeira vez, obtenha a senha inicial:
```bash
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

---

### Passo 2 — Instalar plugins sugeridos

Clique em **"Install suggested plugins"** e aguarde a instalação.

---

### Passo 3 — Criar usuário admin

Preencha os dados do seu usuário e clique em **Save and Continue**.

---

### Passo 4 — Permissão Docker para o Jenkins

Execute no terminal do seu PC:
```bash
docker exec -u root jenkins chmod 666 /var/run/docker.sock
```

---

### Passo 5 — Configurar Maven no Jenkins

1. **Manage Jenkins** → **Tools** → seção **Maven**
2. **Add Maven** → Name: `Maven 3` → marque **"Install automatically"**
3. **Save**

---

### Passo 6 — Criar o job do pipeline

1. No painel principal, clique em **"New Item"**
2. Preencha o nome: `monitoramento-qualidade`
3. Selecione **"Pipeline"** e clique em **OK**

---

### Passo 7 — Configurar o pipeline

**Na seção "Build Triggers":**
- Marque **"Poll SCM"**
- Schedule:
  ```
  H/2 * * * *
  ```

**Na seção "Pipeline":**
- **Definition:** `Pipeline script from SCM`
- **SCM:** `Git`
- **Repository URL:** URL do seu repositório `monitoramento-qualidade`
- **Branch Specifier:** `*/master`
- **Script Path:** `Jenkinsfile`

Clique em **Save**.

---

### Passo 8 — Rodar o pipeline pela primeira vez

1. Clique em **"Build Now"**
2. Em **Build History**, clique no item `#1`
3. Clique em **"Console Output"**

Saída esperada:
```
[Pipeline] stage (Checkout)
Clonando repositorio monitoramento-qualidade...
[Pipeline] stage (Build)
Compilando monitoramento-qualidade com Maven...
[INFO] BUILD SUCCESS
[Pipeline] stage (Docker Build)
Gerando imagem Docker...
[Pipeline] stage (Deploy)
Realizando deploy do monitoramento-qualidade...
monitoramento-qualidade
Finished: SUCCESS
```

---

### Passo 9 — Verificar se o serviço subiu

```bash
curl http://localhost:8084/leituras
```

Retorno esperado (banco vazio):
```json
[]
```

---

## Testando o fluxo completo

Exemplo de fluxo: sensor detecta temperatura alta → cria alerta → operador assume → resolve

**1. Registrar leitura alta:**
```bash
curl -X POST http://localhost:8084/leituras \
  -H "Content-Type: application/json" \
  -d '{
    "tipoSensor": "TEMPERATURA",
    "valor": 22.5,
    "localizacao": "Adega Principal",
    "registradoEm": "2026-11-08T18:00:00.000"
  }'
```

**2. Criar alerta para essa leitura (use o id retornado acima):**
```bash
curl -X POST http://localhost:8084/alertas \
  -H "Content-Type: application/json" \
  -d '{
    "idLeituraSensor": 1,
    "severidade": "ALTA",
    "status": "ABERTO",
    "descricao": "Temperatura acima de 18C na Adega Principal",
    "criadoEm": "2026-11-08T18:00:00.000"
  }'
```

**3. Operador assume o alerta:**
```bash
curl -X PATCH http://localhost:8084/alertas/1/EM_ANDAMENTO
```

**4. Operador resolve:**
```bash
curl -X PATCH http://localhost:8084/alertas/1/RESOLVIDO
```

---

## Erros comuns

| Erro | Causa | Solução |
|------|-------|---------|
| `Got permission denied while trying to connect to Docker` | Jenkins sem permissão no socket | Execute o Passo 4 novamente |
| `mvn: command not found` | Maven não configurado | Refaça o Passo 5 |
| `Connection refused` na porta 8084 | Container não subiu | Verifique o Console Output do pipeline |
| `Transicao invalida` no PATCH | Status fora do fluxo permitido | Verifique o fluxo de transições acima |
| `ORA-01017: invalid username/password` | Credenciais Oracle erradas | Corrija o DatabaseConnection.java e refaça o push |
