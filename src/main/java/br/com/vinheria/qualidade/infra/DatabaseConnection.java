package br.com.vinheria.qualidade.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DatabaseConnection {

    private static final String URL     = "your_oracle_db_url";
    private static final String USUARIO = "your_user";
    private static final String SENHA   = "your_password";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (Objects.isNull(connection))
            connection = DriverManager.getConnection(URL, USUARIO, SENHA);

        if (connection.isClosed())
            connection = DriverManager.getConnection(URL, USUARIO, SENHA);

        return connection;
    }
}
