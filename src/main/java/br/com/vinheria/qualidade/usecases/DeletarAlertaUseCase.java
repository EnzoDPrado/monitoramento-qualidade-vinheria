package br.com.vinheria.qualidade.usecases;

import br.com.vinheria.qualidade.infra.ApiError;
import br.com.vinheria.qualidade.repository.AlertaQualidadeRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

public class DeletarAlertaUseCase {

    private final AlertaQualidadeRepository alertaRepository;

    public DeletarAlertaUseCase(AlertaQualidadeRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    public void execute(Long id) throws SQLException {
        alertaRepository.findById(id).orElseThrow(() ->
                new WebApplicationException(
                        Response.status(Response.Status.NOT_FOUND)
                                .entity(new ApiError("Alerta nao encontrado"))
                                .type(MediaType.APPLICATION_JSON)
                                .build()
                ));

        alertaRepository.delete(id);
    }
}
