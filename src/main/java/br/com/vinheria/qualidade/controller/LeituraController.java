package br.com.vinheria.qualidade.controller;

import br.com.vinheria.qualidade.dto.CriarLeituraInputDTO;
import br.com.vinheria.qualidade.service.LeituraService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.sql.SQLException;

@Path("leituras")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LeituraController {

    private final LeituraService leituraService;

    public LeituraController() throws SQLException {
        this.leituraService = new LeituraService();
    }

    @GET
    public Response listarTodas() throws SQLException {
        var leituras = leituraService.listarTodas();
        return Response.ok(leituras).build();
    }

    @POST
    public Response criar(CriarLeituraInputDTO input, @Context UriInfo uriInfo) throws SQLException {
        var leitura = leituraService.criar(input);

        UriBuilder uri = uriInfo.getAbsolutePathBuilder();
        uri.path(String.valueOf(leitura.getId()));
        return Response.created(uri.build()).entity(leitura).build();
    }
}
