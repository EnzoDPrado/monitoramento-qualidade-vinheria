package br.com.vinheria.qualidade;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://0.0.0.0:8084/";

    public static HttpServer startServer() {
        // registra controllers e o filtro de autenticacao (AuthFilter)
        final ResourceConfig rc = new ResourceConfig()
                .packages("br.com.vinheria.qualidade.controller")
                .packages("br.com.vinheria.qualidade.infra");

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println("Servico monitoramento-qualidade rodando em: " + BASE_URI);
        System.out.println("Pressione Ctrl+C para parar...");
        System.in.read();
        server.stop();
    }
}
