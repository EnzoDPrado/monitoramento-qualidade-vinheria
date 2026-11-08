package br.com.vinheria.qualidade.usecases;

import br.com.vinheria.qualidade.dto.CriarAlertaInputDTO;
import br.com.vinheria.qualidade.entities.AlertaQualidade;
import br.com.vinheria.qualidade.infra.ApiError;
import br.com.vinheria.qualidade.repository.AlertaQualidadeRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

public class CriarAlertaUseCase {

    private static final List<String> SEVERIDADES_VALIDAS = List.of("BAIXA", "MEDIA", "ALTA", "CRITICA");
    private static final List<String> STATUS_VALIDOS      = List.of("ABERTO", "EM_ANDAMENTO", "RESOLVIDO", "DESCARTADO");

    private final AlertaQualidadeRepository alertaRepository;

    public CriarAlertaUseCase(AlertaQualidadeRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    public AlertaQualidade execute(CriarAlertaInputDTO input) throws SQLException {
        validar(input);

        AlertaQualidade alerta = input.toAlerta();
        alertaRepository.create(alerta);
        return alerta;
    }

    private void validar(CriarAlertaInputDTO input) {
        if (input.idLeituraSensor() == null)
            throw badRequest("ID da leitura do sensor e obrigatorio");

        if (input.severidade() == null || input.severidade().isBlank())
            throw badRequest("Severidade e obrigatoria");

        if (!SEVERIDADES_VALIDAS.contains(input.severidade().toUpperCase()))
            throw badRequest("Severidade invalida. Use: BAIXA, MEDIA, ALTA ou CRITICA");

        if (input.status() == null || input.status().isBlank())
            throw badRequest("Status e obrigatorio");

        if (!STATUS_VALIDOS.contains(input.status().toUpperCase()))
            throw badRequest("Status invalido. Use: ABERTO, EM_ANDAMENTO, RESOLVIDO ou DESCARTADO");

        if (input.descricao() != null && input.descricao().length() > 500)
            throw badRequest("Descricao deve ter no maximo 500 caracteres");
    }

    private WebApplicationException badRequest(String mensagem) {
        return new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ApiError(mensagem))
                        .type(MediaType.APPLICATION_JSON)
                        .build()
        );
    }
}
