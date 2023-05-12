package br.com.bankil;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {
    public Connection retornarConexao() {
        try {
            return this.createDataSource().getConnection();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/bankil");
        config.setUsername("root");
        config.setPassword("root");
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }

}
