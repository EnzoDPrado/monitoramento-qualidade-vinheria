package br.com.vinheria.qualidade.entities;

import java.sql.Timestamp;

// Representa um alerta gerado quando um sensor ultrapassa os limites
public class AlertaQualidade {

    private Long id;
    private Long idLeituraSensor;
    private String severidade;    // BAIXA, MEDIA, ALTA, CRITICA
    private String status;        // ABERTO, EM_ANDAMENTO, RESOLVIDO, DESCARTADO
    private String descricao;
    private Timestamp criadoEm;

    public AlertaQualidade() {}

    public AlertaQualidade(Long id, Long idLeituraSensor, String severidade, String status,
                           String descricao, Timestamp criadoEm) {
        this.id = id;
        this.idLeituraSensor = idLeituraSensor;
        this.severidade = severidade;
        this.status = status;
        this.descricao = descricao;
        this.criadoEm = criadoEm;
    }

    public AlertaQualidade(Long idLeituraSensor, String severidade, String status,
                           String descricao, Timestamp criadoEm) {
        this.idLeituraSensor = idLeituraSensor;
        this.severidade = severidade;
        this.status = status;
        this.descricao = descricao;
        this.criadoEm = criadoEm;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdLeituraSensor() { return idLeituraSensor; }
    public void setIdLeituraSensor(Long idLeituraSensor) { this.idLeituraSensor = idLeituraSensor; }

    public String getSeveridade() { return severidade; }
    public void setSeveridade(String severidade) { this.severidade = severidade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Timestamp getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Timestamp criadoEm) { this.criadoEm = criadoEm; }
}
