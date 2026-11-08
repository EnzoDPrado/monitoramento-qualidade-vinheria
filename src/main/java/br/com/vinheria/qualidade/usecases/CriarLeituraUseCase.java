package br.com.vinheria.qualidade.usecases;

import br.com.vinheria.qualidade.dto.CriarLeituraInputDTO;
import br.com.vinheria.qualidade.entities.LeituraSensor;
import br.com.vinheria.qualidade.infra.ApiError;
import br.com.vinheria.qualidade.repository.LeituraSensorRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

public class CriarLeituraUseCase {

    // Tipos de sensor aceitos
    private static final List<String> TIPOS_VALIDOS = List.of("TEMPERATURA", "UMIDADE");

    // Limites razoáveis para os sensores da adega
    private static final double TEMP_MIN = -10.0;
    private static final double TEMP_MAX = 50.0;
    private static final double UMIDADE_MIN = 0.0;
    private static final double UMIDADE_MAX = 100.0;

    private final LeituraSensorRepository leituraRepository;

    public CriarLeituraUseCase(LeituraSensorRepository leituraRepository) {
        this.leituraRepository = leituraRepository;
    }

    public LeituraSensor execute(CriarLeituraInputDTO input) throws SQLException {
        validar(input);

        LeituraSensor leitura = input.toLeitura();
        leituraRepository.create(leitura);
        return leitura;
    }

    private void validar(CriarLeituraInputDTO input) {
        if (input.tipoSensor() == null || input.tipoSensor().isBlank())
            throw badRequest("Tipo do sensor e obrigatorio");

        if (!TIPOS_VALIDOS.contains(input.tipoSensor().toUpperCase()))
            throw badRequest("Tipo de sensor invalido. Use: TEMPERATURA ou UMIDADE");

        if (input.valor() == null)
            throw badRequest("Valor da leitura e obrigatorio");

        // valida limites conforme o tipo
        String tipo = input.tipoSensor().toUpperCase();
        if (tipo.equals("TEMPERATURA") && (input.valor() < TEMP_MIN || input.valor() > TEMP_MAX))
            throw badRequest("Temperatura fora do intervalo esperado (" + TEMP_MIN + " a " + TEMP_MAX + " graus)");

        if (tipo.equals("UMIDADE") && (input.valor() < UMIDADE_MIN || input.valor() > UMIDADE_MAX))
            throw badRequest("Umidade deve estar entre 0 e 100%");

        if (input.registradoEm() == null)
            throw badRequest("Data/hora do registro e obrigatoria");

        if (input.localizacao() != null && input.localizacao().length() > 200)
            throw badRequest("Localizacao deve ter no maximo 200 caracteres");
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
