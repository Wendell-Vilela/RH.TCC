package dao;

import java.sql.Connection;
import java.sql.SQLException;
import util.conexao;

public class ConnectionFactory {

    public static Connection conectar() throws SQLException {
        return conexao.abrir();
    }
}