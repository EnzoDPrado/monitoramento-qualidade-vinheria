package br.com.vinheria.qualidade.service;

import br.com.vinheria.qualidade.dto.CriarLeituraInputDTO;
import br.com.vinheria.qualidade.entities.LeituraSensor;
import br.com.vinheria.qualidade.repository.LeituraSensorRepository;
import br.com.vinheria.qualidade.usecases.CriarLeituraUseCase;
import br.com.vinheria.qualidade.usecases.ListarLeiturasUseCase;

import java.sql.SQLException;
import java.util.List;

public class LeituraService {

    private final LeituraSensorRepository leituraRepository;

    public LeituraService() throws SQLException {
        this.leituraRepository = new LeituraSensorRepository();
    }

    public List<LeituraSensor> listarTodas() throws SQLException {
        ListarLeiturasUseCase useCase = new ListarLeiturasUseCase(leituraRepository);
        List<LeituraSensor> lista = useCase.execute();
        leituraRepository.closeConnection();
        return lista;
    }

    public LeituraSensor criar(CriarLeituraInputDTO input) throws SQLException {
        CriarLeituraUseCase useCase = new CriarLeituraUseCase(leituraRepository);
        LeituraSensor leitura = useCase.execute(input);
        leituraRepository.closeConnection();
        return leitura;
    }
}
