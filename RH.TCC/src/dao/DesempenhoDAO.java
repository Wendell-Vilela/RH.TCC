package dao;

import model.Desempenho;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DesempenhoDAO {

    public Desempenho buscarDadosDesempenho() {
        
        Desempenho desempenho = new Desempenho();
        
        String sqlMediaEquipes = "SELECT AVG(vl_nota) as media_geral FROM aval_desemp";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlMediaEquipes);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                double media = rs.getDouble("media_geral");
                if (media == 0) media = 8.2;

                desempenho.setDesempenhoRH(88.0);
                desempenho.setDesempenhoComercial(79.0);
                desempenho.setDesempenhoFinanceiro(84.0);
                desempenho.setDesempenhoOperacoes(76.0);

                desempenho.setComunicacao(82.0);
                desempenho.setEntrega(80.0);
                desempenho.setLideranca(78.0);
                desempenho.setQualidade(85.0);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar médias de desempenho: " + e.getMessage());
        }

        String sqlColaboradores = "SELECT dp.nm_completo, ad.vl_nota, ad.st_aval " +
                                  "FROM aval_desemp ad " +
                                  "JOIN funcionario f ON ad.id_func = f.id_func " +
                                  "JOIN dado_pessoal dp ON f.id_func = dp.id_func";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlColaboradores);
             ResultSet rs = stmt.executeQuery()) {

            boolean encontrouColaboradores = false;

            while (rs.next()) {
                encontrouColaboradores = true;
                String nome = rs.getString("nm_completo");
                String nota = String.valueOf(rs.getDouble("vl_nota"));
                String status = rs.getString("st_aval");
                if (status == null) status = "Estável";

                desempenho.adicionarColaborador(nome, nota, "• " + status);
            }

            if (!encontrouColaboradores) {
                desempenho.adicionarColaborador("Ana Carvalho", "4,5", "• Destaque");
                desempenho.adicionarColaborador("João Pereira", "3,8", "• Acompanhar");
                desempenho.adicionarColaborador("Marina Costa", "4,2", "• Estável");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar colaboradores para o desempenho: " + e.getMessage());
        }

        return desempenho;
    }
}
