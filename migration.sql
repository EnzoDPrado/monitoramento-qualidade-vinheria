-- =============================================================
-- Migration: monitoramento-qualidade
-- Servico: Monitoramento de Qualidade - Vinheria Agnello
-- Execute este script no banco Oracle da faculdade
-- =============================================================

-- -------------------------------------------------------------
-- Tabela de leituras de sensor (temperatura e umidade)
-- -------------------------------------------------------------
CREATE TABLE tb_leituras_sensor (
    id              NUMBER          NOT NULL,
    tipo_sensor     VARCHAR2(20)    NOT NULL,
    valor           NUMBER(10, 2)   NOT NULL,
    localizacao     VARCHAR2(200),
    registrado_em   TIMESTAMP       NOT NULL,
    CONSTRAINT pk_leituras_sensor   PRIMARY KEY (id),
    CONSTRAINT ck_leitura_tipo      CHECK (tipo_sensor IN ('TEMPERATURA', 'UMIDADE'))
);

CREATE SEQUENCE seq_tb_leituras_sensor
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- -------------------------------------------------------------
-- Tabela de alertas de qualidade
-- -------------------------------------------------------------
CREATE TABLE tb_alertas_qualidade (
    id                  NUMBER          NOT NULL,
    id_leitura_sensor   NUMBER          NOT NULL,
    severidade          VARCHAR2(20)    NOT NULL,
    status              VARCHAR2(20)    NOT NULL,
    descricao           VARCHAR2(500),
    criado_em           TIMESTAMP,
    CONSTRAINT pk_alertas_qualidade     PRIMARY KEY (id),
    CONSTRAINT fk_alerta_leitura        FOREIGN KEY (id_leitura_sensor)
        REFERENCES tb_leituras_sensor(id),
    CONSTRAINT ck_alerta_severidade     CHECK (
        severidade IN ('BAIXA', 'MEDIA', 'ALTA', 'CRITICA')
    ),
    CONSTRAINT ck_alerta_status         CHECK (
        status IN ('ABERTO', 'EM_ANDAMENTO', 'RESOLVIDO', 'DESCARTADO')
    )
);

CREATE SEQUENCE seq_tb_alertas_qualidade
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- -------------------------------------------------------------
-- Dados de exemplo (opcional — remova se nao quiser)
-- -------------------------------------------------------------
INSERT INTO tb_leituras_sensor (id, tipo_sensor, valor, localizacao, registrado_em)
VALUES (seq_tb_leituras_sensor.nextval, 'TEMPERATURA', 16.5,
        'Adega Principal', TIMESTAMP '2026-11-08 08:00:00');

INSERT INTO tb_leituras_sensor (id, tipo_sensor, valor, localizacao, registrado_em)
VALUES (seq_tb_leituras_sensor.nextval, 'TEMPERATURA', 22.5,
        'Adega Principal', TIMESTAMP '2026-11-08 12:00:00');

INSERT INTO tb_leituras_sensor (id, tipo_sensor, valor, localizacao, registrado_em)
VALUES (seq_tb_leituras_sensor.nextval, 'UMIDADE', 65.0,
        'Barril-07', TIMESTAMP '2026-11-08 08:00:00');

INSERT INTO tb_alertas_qualidade (id, id_leitura_sensor, severidade, status, descricao, criado_em)
VALUES (seq_tb_alertas_qualidade.nextval, 2, 'ALTA', 'ABERTO',
        'Temperatura acima de 18C na Adega Principal',
        TIMESTAMP '2026-11-08 12:01:00');

COMMIT;
