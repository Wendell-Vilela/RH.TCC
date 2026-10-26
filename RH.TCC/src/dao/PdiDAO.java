package dao;

import model.PDI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PdiDAO {

    public List<PDI> listarPdiPorFuncionario(String nomeFuncionario) {
        
        List<PDI> lista = new ArrayList<>();
        
        String sql = "SELECT s.nm_setor, dp.nm_completo, ap.ds_acao, ap.dt_prazo, ap.st_pdi, p.vl_progresso " +
                     "FROM pdi p " +
                     "JOIN funcionario f ON p.id_func = f.id_func " +
                     "JOIN dado_pessoal dp ON f.id_func = dp.id_func " +
                     "JOIN setor s ON f.id_setor = s.id_setor " +
                     "LEFT JOIN acao_pdi ap ON p.id_pdi = ap.id_pdi " +
                     "WHERE dp.nm_completo = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeFuncionario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                while (rs.next()) {
                    
                    PDI pdi = new PDI();
                    
                    pdi.setArea(rs.getString("nm_setor"));
                    pdi.setFuncionario(rs.getString("nm_completo"));
                    pdi.setProximaAcao(rs.getString("ds_acao") != null ? rs.getString("ds_acao") : "Ação Padrão");
                    pdi.setDescricao(rs.getString("ds_acao"));
                    pdi.setPrazo(String.valueOf(rs.getDate("dt_prazo")));
                    pdi.setStatus(rs.getString("st_pdi") != null ? rs.getString("st_pdi") : "Pendente");
                    pdi.setProgresso(rs.getInt("vl_progresso"));

                    lista.add(pdi);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar PDI do funcionário: " + e.getMessage());
        }

        return lista;
    }
}
