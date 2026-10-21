package dao;

import model.TaxaTurnover;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaxaTurnoverDAO {

    public List<TaxaTurnover> listarTurnoverPorDepartamento() {
        
        List<TaxaTurnover> lista = new ArrayList<>();
        
        String sql = "SELECT s.nm_setor, " +
                     "SUM(CASE WHEN f.st_func = 'Ativo' THEN 1 ELSE 0 END) AS total_ativos, " +
                     "SUM(CASE WHEN f.st_func = 'Desligado' THEN 1 ELSE 0 END) AS total_desligados " +
                     "FROM setor s " +
                     "LEFT JOIN funcionario f ON s.id_setor = f.id_setor " +
                     "GROUP BY s.nm_setor";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                
                TaxaTurnover turnover = new TaxaTurnover();
                
                String departamento = rs.getString("nm_setor");
                int colaboradores = rs.getInt("total_ativos");
                int desligamentos = rs.getInt("total_desligados");
                
                double taxa = 0.0;
                if (colaboradores > 0) {
                    taxa = ((double) desligamentos / colaboradores) * 100.0;
                }

                turnover.setDepartamento(departamento != null ? departamento : "Geral");
                turnover.setColaboradores(colaboradores);
                turnover.setDesligamentos(desligamentos);
                turnover.setTaxaSetor(Math.round(taxa * 100.0) / 100.0);

                lista.add(turnover);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao calcular taxa de turnover por departamento: " + e.getMessage());
        }

        return lista;
    }
}
