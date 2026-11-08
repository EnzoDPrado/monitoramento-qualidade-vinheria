package br.com.vinheria.qualidade.usecases;

import br.com.vinheria.qualidade.entities.LeituraSensor;
import br.com.vinheria.qualidade.repository.LeituraSensorRepository;

import java.sql.SQLException;
import java.util.List;

public class ListarLeiturasUseCase {

    private final LeituraSensorRepository leituraRepository;

    public ListarLeiturasUseCase(LeituraSensorRepository leituraRepository) {
        this.leituraRepository = leituraRepository;
    }

    public List<LeituraSensor> execute() throws SQLException {
        return leituraRepository.findAll();
    }
}
