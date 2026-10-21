
package dao;

import model.GestaoFerias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestaoFeriasDAO {

    public List<GestaoFerias> listarGestaoFeriasPorDepartamento() {
        List<GestaoFerias> lista = new ArrayList<>();

        String sql =
            "SELECT s.nm_setor, " +
            "SUM(CASE " +
            "WHEN TIMESTAMPDIFF(MONTH, f.dt_adm, CURDATE()) <= 12 " +
            "THEN 1 ELSE 0 END) AS ate_12, " +
            "SUM(CASE " +
            "WHEN TIMESTAMPDIFF(MONTH, f.dt_adm, CURDATE()) > 12 " +
            "AND TIMESTAMPDIFF(MONTH, f.dt_adm, CURDATE()) <= 18 " +
            "THEN 1 ELSE 0 END) AS de_12_18, " +
            "SUM(CASE " +
            "WHEN TIMESTAMPDIFF(MONTH, f.dt_adm, CURDATE()) > 18 " +
            "THEN 1 ELSE 0 END) AS mais_18 " +
            "FROM funcionario f " +
            "INNER JOIN setor s ON f.id_setor = s.id_setor " +
            "WHERE f.st_func = 'Ativo' " +
            "GROUP BY s.id_setor, s.nm_setor " +
            "ORDER BY s.nm_setor";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                GestaoFerias gestao = new GestaoFerias();

                gestao.setDepartamento(rs.getString("nm_setor"));
                gestao.setAteDozeMeses(rs.getInt("ate_12"));
                gestao.setDozeADezoitoMeses(rs.getInt("de_12_18"));
                gestao.setMaisDezoitoMeses(rs.getInt("mais_18"));

                lista.add(gestao);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Erro ao consultar dados de Gestão de Férias: "
                + e.getMessage(),
                e
            );
        }

        return lista;
    }
}
