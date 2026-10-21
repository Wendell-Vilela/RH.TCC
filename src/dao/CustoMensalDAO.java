
package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CustoMensal;

public class CustoMensalDAO {

    public CustoMensal buscarCustoMensal(int mes, int ano) {
        CustoMensal custoMensal = new CustoMensal();
        List<CustoMensal.Item> listaItens = new ArrayList<>();

        BigDecimal totalSalario = BigDecimal.ZERO;
        BigDecimal totalEncargo = BigDecimal.ZERO;
        BigDecimal totalBeneficio = BigDecimal.ZERO;
        BigDecimal totalOutrasDespesas = BigDecimal.ZERO;

        String sqlTotais =
            "SELECT " +
            "COALESCE(SUM(vl_tot_folha), 0) AS total_salarios, " +
            "COALESCE(SUM(vl_encargo), 0) AS total_encargos, " +
            "COALESCE(SUM(vl_tot_benef), 0) AS total_beneficios, " +
            "COALESCE(SUM(vl_outra_despesa), 0) AS total_outras_despesas " +
            "FROM custo_mensal " +
            "WHERE mes_comp = ? AND ano_comp = ?";

        String sqlItens =
            "SELECT f.id_func, dp.nm_completo, f.vl_salario_atual " +
            "FROM funcionario f " +
            "JOIN dado_pessoal dp ON f.id_func = dp.id_func " +
            "WHERE f.st_func = 'Ativo' " +
            "AND f.dt_adm <= LAST_DAY(?) " +
            "ORDER BY dp.nm_completo";

        try (Connection conn = ConnectionFactory.conectar()) {

            try (PreparedStatement stmt = conn.prepareStatement(sqlTotais)) {
                stmt.setInt(1, mes);
                stmt.setInt(2, ano);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        totalSalario = rs.getBigDecimal("total_salarios");
                        totalEncargo = rs.getBigDecimal("total_encargos");
                        totalBeneficio = rs.getBigDecimal("total_beneficios");
                        totalOutrasDespesas = rs.getBigDecimal("total_outras_despesas");

                        if (totalSalario == null) totalSalario = BigDecimal.ZERO;
                        if (totalEncargo == null) totalEncargo = BigDecimal.ZERO;
                        if (totalBeneficio == null) totalBeneficio = BigDecimal.ZERO;
                        if (totalOutrasDespesas == null) totalOutrasDespesas = BigDecimal.ZERO;
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlItens)) {
                stmt.setDate(
                    1,
                    java.sql.Date.valueOf(
                        java.time.LocalDate.of(ano, mes, 1)
                    )
                );

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Long id = rs.getLong("id_func");
                        String nome = rs.getString("nm_completo");
                        BigDecimal salario = rs.getBigDecimal("vl_salario_atual");

                        if (salario == null) {
                            salario = BigDecimal.ZERO;
                        }

                        CustoMensal.Item item = new CustoMensal.Item(
                            id,
                            nome,
                            salario,
                            BigDecimal.ZERO,
                            BigDecimal.ZERO
                        );

                        listaItens.add(item);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Erro ao buscar os custos mensais: " + e.getMessage(),
                e
            );
        }

        custoMensal.setSalarioTotal(totalSalario);
        custoMensal.setEncargoTotal(totalEncargo);
        custoMensal.setBeneficioTotal(totalBeneficio);

        BigDecimal investimentoTotal = totalSalario
            .add(totalEncargo)
            .add(totalBeneficio)
            .add(totalOutrasDespesas);

        custoMensal.setInvestimentoTotal(investimentoTotal);
        custoMensal.setItens(listaItens);

        return custoMensal;
    }

}
