package br.com.vinheria.qualidade.usecases;

import br.com.vinheria.qualidade.entities.AlertaQualidade;
import br.com.vinheria.qualidade.infra.ApiError;
import br.com.vinheria.qualidade.repository.AlertaQualidadeRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AtualizarStatusAlertaUseCase {

    // transições de estado permitidas
    private static final Map<String, List<String>> TRANSICOES = Map.of(
            "ABERTO",       List.of("EM_ANDAMENTO", "DESCARTADO"),
            "EM_ANDAMENTO", List.of("RESOLVIDO", "DESCARTADO")
    );

    private final AlertaQualidadeRepository alertaRepository;

    public AtualizarStatusAlertaUseCase(AlertaQualidadeRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    public AlertaQualidade execute(Long id, String novoStatus) throws SQLException {
        String statusNormalizado = novoStatus.toUpperCase();

        AlertaQualidade alerta = alertaRepository.findById(id).orElseThrow(() ->
                new WebApplicationException(
                        Response.status(Response.Status.NOT_FOUND)
                                .entity(new ApiError("Alerta nao encontrado"))
                                .type(MediaType.APPLICATION_JSON)
                                .build()
                ));

        validarTransicao(alerta.getStatus(), statusNormalizado);

        alertaRepository.updateStatus(id, statusNormalizado);
        alerta.setStatus(statusNormalizado);
        return alerta;
    }

    private void validarTransicao(String statusAtual, String novoStatus) {
        List<String> permitidos = TRANSICOES.get(statusAtual);

        // se não está no mapa, é estado terminal
        if (permitidos == null)
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(new ApiError("Alerta ja esta em estado terminal: " + statusAtual))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );

        if (!permitidos.contains(novoStatus))
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(new ApiError(
                                    "Transicao invalida de " + statusAtual + " para " + novoStatus +
                                    ". Permitidos: " + permitidos
                            ))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
    }
}
