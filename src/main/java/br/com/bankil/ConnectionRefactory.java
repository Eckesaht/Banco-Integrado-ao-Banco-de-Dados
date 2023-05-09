package br.com.bankil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionRefactory {
    public Connection retornarConexao() {
        try {
            return DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/bankil?user=root&password=root");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
