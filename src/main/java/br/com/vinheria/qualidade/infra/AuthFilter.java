package br.com.vinheria.qualidade.infra;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

// @Provider faz o Jersey registrar esse filtro automaticamente
// Ele intercepta TODAS as requisicoes antes de chegarem nos controllers
@Provider
public class AuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String caminho = requestContext.getUriInfo().getPath();

        // Rota de login nao precisa de token — deixa passar
        if (caminho.equals("auth/login")) {
            return;
        }

        // Pega o header Authorization da requisicao
        String authHeader = requestContext.getHeaderString("Authorization");

        // Se nao veio o header ou nao começa com "Bearer ", bloqueia
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            bloquear(requestContext, "Token nao informado. Use o header: Authorization: Bearer <token>");
            return;
        }

        // Extrai o token (remove o prefixo "Bearer ")
        String token = authHeader.substring(7);

        try {
            // Valida o token — se for invalido lanca excecao
            JwtUtil.validarToken(token);
        } catch (JWTVerificationException e) {
            bloquear(requestContext, "Token invalido ou expirado: " + e.getMessage());
        }
    }

    private void bloquear(ContainerRequestContext ctx, String mensagem) {
        ctx.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ApiError(mensagem))
                        .type(MediaType.APPLICATION_JSON)
                        .build()
        );
    }
}
