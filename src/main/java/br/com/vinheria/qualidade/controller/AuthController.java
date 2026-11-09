package br.com.vinheria.qualidade.controller;

import br.com.vinheria.qualidade.infra.ApiError;
import br.com.vinheria.qualidade.infra.JwtUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

// DTO de entrada do login
record LoginInputDTO(String usuario, String senha) {}

// DTO de saida — retorna o token gerado
record LoginResponseDTO(String token) {}

@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    // Usuario fixo para simulacao — em producao viria do banco
    private static final String USUARIO_FIXO = "admin";
    private static final String SENHA_FIXA   = "vinheria123";

    @POST
    @Path("/login")
    public Response login(LoginInputDTO input) {

        if (input == null || input.usuario() == null || input.senha() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiError("Usuario e senha sao obrigatorios"))
                    .build();
        }

        // Valida as credenciais
        if (!input.usuario().equals(USUARIO_FIXO) || !input.senha().equals(SENHA_FIXA)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ApiError("Usuario ou senha incorretos"))
                    .build();
        }

        // Credenciais corretas — gera e retorna o token
        String token = JwtUtil.gerarToken(input.usuario());
        return Response.ok(new LoginResponseDTO(token)).build();
    }
}
