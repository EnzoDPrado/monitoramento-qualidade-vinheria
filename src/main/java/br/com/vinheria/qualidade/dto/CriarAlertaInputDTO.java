package br.com.vinheria.qualidade.dto;

import br.com.vinheria.qualidade.entities.AlertaQualidade;

import java.sql.Timestamp;

public record CriarAlertaInputDTO(
        Long idLeituraSensor,
        String severidade,
        String status,
        String descricao,
        Timestamp criadoEm
) {
    public AlertaQualidade toAlerta() {
        return new AlertaQualidade(
                this.idLeituraSensor,
                this.severidade,
                this.status,
                this.descricao,
                this.criadoEm
        );
    }
}
