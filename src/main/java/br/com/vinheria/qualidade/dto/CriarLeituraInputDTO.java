package br.com.vinheria.qualidade.dto;

import br.com.vinheria.qualidade.entities.LeituraSensor;

import java.sql.Timestamp;

public record CriarLeituraInputDTO(
        String tipoSensor,
        Double valor,
        String localizacao,
        Timestamp registradoEm
) {
    public LeituraSensor toLeitura() {
        return new LeituraSensor(
                this.tipoSensor,
                this.valor,
                this.localizacao,
                this.registradoEm
        );
    }
}
