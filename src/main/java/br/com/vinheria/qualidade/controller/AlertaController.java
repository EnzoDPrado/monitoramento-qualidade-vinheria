package br.com.vinheria.qualidade.controller;

import br.com.vinheria.qualidade.dto.CriarAlertaInputDTO;
import br.com.vinheria.qualidade.service.AlertaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.sql.SQLException;

@Path("alertas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController() throws SQLException {
        this.alertaService = new AlertaService();
    }

    @GET
    public Response listarTodos() throws SQLException {
        var alertas = alertaService.listarTodos();
        return Response.ok(alertas).build();
    }

    @POST
    public Response criar(CriarAlertaInputDTO input, @Context UriInfo uriInfo) throws SQLException {
        var alerta = alertaService.criar(input);

        UriBuilder uri = uriInfo.getAbsolutePathBuilder();
        uri.path(String.valueOf(alerta.getId()));
        return Response.created(uri.build()).entity(alerta).build();
    }

    @PATCH
    @Path("/{id}/{status}")
    public Response atualizarStatus(@PathParam("id") Long id, @PathParam("status") String status) throws SQLException {
        var alerta = alertaService.atualizarStatus(id, status);
        return Response.ok(alerta).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) throws SQLException {
        alertaService.deletar(id);
        return Response.noContent().build();
    }
}
