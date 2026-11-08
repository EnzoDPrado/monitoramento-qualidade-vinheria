package br.com.vinheria.qualidade.usecases;

import br.com.vinheria.qualidade.entities.AlertaQualidade;
import br.com.vinheria.qualidade.repository.AlertaQualidadeRepository;

import java.sql.SQLException;
import java.util.List;

public class ListarAlertasUseCase {

    private final AlertaQualidadeRepository alertaRepository;

    public ListarAlertasUseCase(AlertaQualidadeRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    public List<AlertaQualidade> execute() throws SQLException {
        return alertaRepository.findAll();
    }
}
