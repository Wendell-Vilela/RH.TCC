
package dao;

import model.FuncionarioAbsenteismo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioAbsenteismoDAO {

    public List<FuncionarioAbsenteismo> listarAbsenteismoFuncionarios() {
        List<FuncionarioAbsenteismo> lista = new ArrayList<>();

        String sql =
            "SELECT f.id_func, dp.nm_completo, " +
            "COUNT(ra.id_ausencia) AS total_faltas, " +
            "COALESCE(SUM(ra.qtd_hora_ausencia), 0) AS total_horas " +
            "FROM funcionario f " +
            "INNER JOIN dado_pessoal dp ON f.id_func = dp.id_func " +
            "LEFT JOIN reg_ausencia ra " +
            "ON f.id_func = ra.id_func " +
            "AND YEAR(ra.dt_reg) = YEAR(CURDATE()) " +
            "WHERE f.st_func = 'Ativo' " +
            "GROUP BY f.id_func, dp.nm_completo " +
            "ORDER BY dp.nm_completo";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FuncionarioAbsenteismo funcionario =
                    new FuncionarioAbsenteismo();

                funcionario.setId(rs.getInt("id_func"));
                funcionario.setNome(rs.getString("nm_completo"));
                funcionario.setFaltas(rs.getInt("total_faltas"));

                double totalHoras = rs.getDouble("total_horas");
                double taxa = (totalHoras / 2112.0) * 100.0;

                funcionario.setTaxaAbs(
                    Math.round(taxa * 100.0) / 100.0
                );

                lista.add(funcionario);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Erro ao consultar absenteísmo dos funcionários: "
                    + e.getMessage(),
                e
            );
        }

        if (lista.isEmpty()) {
            lista.add(new FuncionarioAbsenteismo(
                1, "Funcionário Demonstração 1", 3, 1.14
            ));

            lista.add(new FuncionarioAbsenteismo(
                2, "Funcionário Demonstração 2", 1, 0.38
            ));

            lista.add(new FuncionarioAbsenteismo(
                3, "Funcionário Demonstração 3", 5, 1.89
            ));
        }

        return lista;
    }
}
