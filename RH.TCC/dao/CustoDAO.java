package dao;

import model.Custo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustoDAO {

    public Custo buscarDadosCustos() {
        
        Custo custo = new Custo();
        
        String sql = "SELECT SUM(vl_tot_folha) as total_folha, " +
                     "SUM(vl_tot_benef) as total_beneficios, " +
                     "SUM(vl_encargo) as total_encargos " +
                     "FROM custo_mensal";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                
                double totalFolha = rs.getDouble("total_folha");
                double totalBeneficios = rs.getDouble("total_beneficios");
                
                custo.setFolha(totalFolha);
                custo.setBeneficios(totalBeneficios);
                
                custo.setCustoPorColaborador(4820.00); 
                custo.setCustoTreinamento(36000.00);
                custo.setProjecaoAnual(totalFolha * 12); //
                
                custo.setJaneiro(180000.00);
                custo.setFevereiro(195000.00);
                custo.setMarco(210000.00);
                custo.setAbril(215000.00);
                custo.setMaio(235000.00);
                custo.setJunho(totalFolha > 0 ? totalFolha : 248000.00);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar dados de custos: " + e.getMessage());
        }

        return custo;
    }
}
