package br.com.vinheria.qualidade.repository;

import br.com.vinheria.qualidade.entities.AlertaQualidade;
import br.com.vinheria.qualidade.infra.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlertaQualidadeRepository {

    private final Connection conexao;

    public AlertaQualidadeRepository() throws SQLException {
        this.conexao = DatabaseConnection.getConnection();
    }

    private AlertaQualidade parseAlerta(ResultSet rs) throws SQLException {
        AlertaQualidade a = new AlertaQualidade();
        a.setId(rs.getLong("id"));
        a.setIdLeituraSensor(rs.getLong("id_leitura_sensor"));
        a.setSeveridade(rs.getString("severidade"));
        a.setStatus(rs.getString("status"));
        a.setDescricao(rs.getString("descricao"));
        a.setCriadoEm(rs.getTimestamp("criado_em"));
        return a;
    }

    public List<AlertaQualidade> findAll() throws SQLException {
        PreparedStatement stm = conexao.prepareStatement(
                "SELECT * FROM tb_alertas_qualidade ORDER BY criado_em DESC"
        );
        ResultSet rs = stm.executeQuery();

        List<AlertaQualidade> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(parseAlerta(rs));
        }
        return lista;
    }

    public Optional<AlertaQualidade> findById(Long id) throws SQLException {
        PreparedStatement stm = conexao.prepareStatement(
                "SELECT * FROM tb_alertas_qualidade WHERE id = ?"
        );
        stm.setLong(1, id);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            return Optional.of(parseAlerta(rs));
        }
        return Optional.empty();
    }

    public void create(AlertaQualidade alerta) throws SQLException {
        String sql =
                "BEGIN " +
                "   INSERT INTO tb_alertas_qualidade (id, id_leitura_sensor, severidade, status, descricao, criado_em) " +
                "   VALUES (seq_tb_alertas_qualidade.nextval, ?, ?, ?, ?, ?) " +
                "   RETURNING id INTO ?; " +
                "END;";

        CallableStatement cs = conexao.prepareCall(sql);
        cs.setLong(1, alerta.getIdLeituraSensor());
        cs.setString(2, alerta.getSeveridade());
        cs.setString(3, alerta.getStatus());
        cs.setString(4, alerta.getDescricao());
        cs.setTimestamp(5, alerta.getCriadoEm());
        cs.registerOutParameter(6, Types.NUMERIC);

        cs.execute();
        alerta.setId(cs.getLong(6));
    }

    public void updateStatus(Long id, String novoStatus) throws SQLException {
        PreparedStatement stm = conexao.prepareStatement(
                "UPDATE tb_alertas_qualidade SET status = ? WHERE id = ?"
        );
        stm.setString(1, novoStatus);
        stm.setLong(2, id);

        int rows = stm.executeUpdate();
        if (rows == 0) {
            throw new RuntimeException("Alerta nao encontrado para atualizar");
        }
    }

    public void delete(Long id) throws SQLException {
        PreparedStatement stm = conexao.prepareStatement(
                "DELETE FROM tb_alertas_qualidade WHERE id = ?"
        );
        stm.setLong(1, id);
        int rows = stm.executeUpdate();
        if (rows == 0) {
            throw new RuntimeException("Alerta nao encontrado para deletar");
        }
    }

    public void closeConnection() throws SQLException {
        conexao.close();
    }
}
