package br.com.vinheria.qualidade.service;

import br.com.vinheria.qualidade.dto.CriarAlertaInputDTO;
import br.com.vinheria.qualidade.entities.AlertaQualidade;
import br.com.vinheria.qualidade.repository.AlertaQualidadeRepository;
import br.com.vinheria.qualidade.usecases.AtualizarStatusAlertaUseCase;
import br.com.vinheria.qualidade.usecases.CriarAlertaUseCase;
import br.com.vinheria.qualidade.usecases.DeletarAlertaUseCase;
import br.com.vinheria.qualidade.usecases.ListarAlertasUseCase;

import java.sql.SQLException;
import java.util.List;

public class AlertaService {

    private final AlertaQualidadeRepository alertaRepository;

    public AlertaService() throws SQLException {
        this.alertaRepository = new AlertaQualidadeRepository();
    }

    public List<AlertaQualidade> listarTodos() throws SQLException {
        ListarAlertasUseCase useCase = new ListarAlertasUseCase(alertaRepository);
        List<AlertaQualidade> lista = useCase.execute();
        alertaRepository.closeConnection();
        return lista;
    }

    public AlertaQualidade criar(CriarAlertaInputDTO input) throws SQLException {
        CriarAlertaUseCase useCase = new CriarAlertaUseCase(alertaRepository);
        AlertaQualidade alerta = useCase.execute(input);
        alertaRepository.closeConnection();
        return alerta;
    }

    public AlertaQualidade atualizarStatus(Long id, String novoStatus) throws SQLException {
        AtualizarStatusAlertaUseCase useCase = new AtualizarStatusAlertaUseCase(alertaRepository);
        AlertaQualidade alerta = useCase.execute(id, novoStatus);
        alertaRepository.closeConnection();
        return alerta;
    }

    public void deletar(Long id) throws SQLException {
        DeletarAlertaUseCase useCase = new DeletarAlertaUseCase(alertaRepository);
        useCase.execute(id);
        alertaRepository.closeConnection();
    }
}
