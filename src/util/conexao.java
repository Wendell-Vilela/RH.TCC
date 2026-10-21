
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class conexao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/rh?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=America/Sao_Paulo";

    private static final String USUARIO = "root";
    private static final String SENHA = "";

    private conexao() {
    }

    public static Connection abrir() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "Driver MySQL nao encontrado. Adicione o MySQL Connector/J ao projeto.",
                    e
            );
        }

        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static void fechar(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
    }

    public static void fechar(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
    }

    public static void fechar(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
            }
        }
    }

    public static void fechar(Connection conexao, Statement stmt, ResultSet rs) {
        fechar(rs);
        fechar(stmt);
        fechar(conexao);
    }
}
