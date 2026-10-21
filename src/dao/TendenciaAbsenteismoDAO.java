
package dao;

import model.TendenciaAbsenteismo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TendenciaAbsenteismoDAO {

    public List<TendenciaAbsenteismo> listarTendenciaMensal() {
        List<TendenciaAbsenteismo> lista = new ArrayList<>();

        for (int mes = 1; mes <= 12; mes++) {
            TendenciaAbsenteismo tendencia = new TendenciaAbsenteismo();
            tendencia.setMes(mes);
            tendencia.setPercentual(0.0);
            lista.add(tendencia);
        }

        String sql =
            "SELECT MONTH(ra.dt_reg) AS mes, " +
            "SUM(ra.qtd_hora_ausencia) AS total_horas, " +
            "(SELECT COUNT(*) FROM funcionario " +
            "WHERE st_func = 'Ativo') AS total_funcionarios " +
            "FROM reg_ausencia ra " +
            "WHERE YEAR(ra.dt_reg) = YEAR(CURDATE()) " +
            "GROUP BY MONTH(ra.dt_reg) " +
            "ORDER BY mes";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int mes = rs.getInt("mes");
                double totalHoras = rs.getDouble("total_horas");
                int totalFuncionarios = rs.getInt("total_funcionarios");

                if (mes >= 1 && mes <= 12 && totalFuncionarios > 0) {
                    double horasPrevistas =
                        totalFuncionarios * 22.0 * 8.0;

                    double percentual =
                        (totalHoras / horasPrevistas) * 100.0;

                    lista.get(mes - 1).setPercentual(
                        Math.round(percentual * 100.0) / 100.0
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Erro ao consultar tendência mensal: " + e.getMessage(),
                e
            );
        }

        return lista;
    }
}
