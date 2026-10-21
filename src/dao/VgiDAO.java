package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.VGI;

public class VgiDAO {

    public VGI buscarIndicadoresGerais() {
        
        VGI vgi = new VGI();
        
        String sqlCusto = "SELECT SUM(vl_custo) AS total_custo FROM custo_mensal";
        
        String sqlDesempenho = "SELECT AVG(vl_nota) AS media_desempenho FROM aval_desemp";

        try (Connection conn = ConnectionFactory.conectar()) {
            
            try (PreparedStatement stmtCusto = conn.prepareStatement(sqlCusto);
                 ResultSet rsCusto = stmtCusto.executeQuery()) {
                if (rsCusto.next()) {
                    vgi.setCustosRH(rsCusto.getDouble("total_custo"));
                }
            }

            try (PreparedStatement stmtDesempenho = conn.prepareStatement(sqlDesempenho);
                 ResultSet rsDesemp = stmtDesempenho.executeQuery()) {
                if (rsDesemp.next()) {
                    vgi.setDesempenhoMedio(rsDesemp.getDouble("media_desempenho"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar indicadores gerais VGI: " + e.getMessage());
        }

        vgi.setTurnover(8.4);
        vgi.setAbsenteismo(3.2);
        
        vgi.setMetaTurnover(7.0);
        vgi.setMetaAbsenteismo(3.5);
        vgi.setMetaDesempenho(8.0);
        vgi.setMetaCustosRH(150000.00);

        vgi.setTendenciaTurnover("Estável");
        vgi.setTendenciaAbsenteismo("Em queda");
        vgi.setTendenciaDesempenho("Em alta");
        vgi.setTendenciaCustosRH("Estável");

        return vgi;
    }
}
