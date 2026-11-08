package br.com.vinheria.qualidade.repository;

import br.com.vinheria.qualidade.entities.LeituraSensor;
import br.com.vinheria.qualidade.infra.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeituraSensorRepository {

    private final Connection conexao;

    public LeituraSensorRepository() throws SQLException {
        this.conexao = DatabaseConnection.getConnection();
    }

    private LeituraSensor parseLeitura(ResultSet rs) throws SQLException {
        LeituraSensor l = new LeituraSensor();
        l.setId(rs.getLong("id"));
        l.setTipoSensor(rs.getString("tipo_sensor"));
        l.setValor(rs.getDouble("valor"));
        l.setLocalizacao(rs.getString("localizacao"));
        l.setRegistradoEm(rs.getTimestamp("registrado_em"));
        return l;
    }

    public List<LeituraSensor> findAll() throws SQLException {
        PreparedStatement stm = conexao.prepareStatement(
                "SELECT * FROM tb_leituras_sensor ORDER BY registrado_em DESC"
        );
        ResultSet rs = stm.executeQuery();

        List<LeituraSensor> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(parseLeitura(rs));
        }
        return lista;
    }

    public Optional<LeituraSensor> findById(Long id) throws SQLException {
        PreparedStatement stm = conexao.prepareStatement(
                "SELECT * FROM tb_leituras_sensor WHERE id = ?"
        );
        stm.setLong(1, id);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            return Optional.of(parseLeitura(rs));
        }
        return Optional.empty();
    }

    public void create(LeituraSensor leitura) throws SQLException {
        String sql =
                "BEGIN " +
                "   INSERT INTO tb_leituras_sensor (id, tipo_sensor, valor, localizacao, registrado_em) " +
                "   VALUES (seq_tb_leituras_sensor.nextval, ?, ?, ?, ?) " +
                "   RETURNING id INTO ?; " +
                "END;";

        CallableStatement cs = conexao.prepareCall(sql);
        cs.setString(1, leitura.getTipoSensor());
        cs.setDouble(2, leitura.getValor());
        cs.setString(3, leitura.getLocalizacao());
        cs.setTimestamp(4, leitura.getRegistradoEm());
        cs.registerOutParameter(5, Types.NUMERIC);

        cs.execute();
        leitura.setId(cs.getLong(5));
    }

    public void closeConnection() throws SQLException {
        conexao.close();
    }
}
