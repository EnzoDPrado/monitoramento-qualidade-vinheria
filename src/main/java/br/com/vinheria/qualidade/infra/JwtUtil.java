package br.com.vinheria.qualidade.infra;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

// Classe responsável por gerar e validar tokens JWT
public class JwtUtil {

    // Chave secreta usada para assinar o token
    // Em producao isso viria de uma variavel de ambiente, nao hardcoded assim
    private static final String SECRET = "vinheria-secret-key-2026";

    // Token expira em 1 hora (em milissegundos)
    private static final long EXPIRACAO_MS = 60 * 60 * 1000;

    private static final Algorithm algoritmo = Algorithm.HMAC256(SECRET);

    // Gera um token JWT para o usuario informado
    public static String gerarToken(String usuario) {
        return JWT.create()
                .withSubject(usuario)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRACAO_MS))
                .withIssuer("vinheria-monitoramento-qualidade")
                .sign(algoritmo);
    }

    // Valida o token e retorna o usuario se for valido
    // Lanca excecao se o token for invalido ou expirado
    public static String validarToken(String token) throws JWTVerificationException {
        DecodedJWT jwt = JWT.require(algoritmo)
                .withIssuer("vinheria-monitoramento-qualidade")
                .build()
                .verify(token);

        return jwt.getSubject();
    }
}
