package dao;

import model.Avaliacao;
import model.Feedback;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvaliacaoDAO {

    public Avaliacao buscarAvaliacaoPorFuncionario(String nomeFuncionario) {
      
        Avaliacao avaliacao = null;
      
        String sqlAval = "SELECT s.nm_setor, f.nm_completo, a.ds_autoaval, a.ds_aval_gestor, a.ds_aval_par, a.id_func " +
                         "FROM aval_desemp a " +
                         "JOIN funcionario func ON a.id_func = func.id_func " +
                         "JOIN dado_pessoal f ON func.id_func = f.id_func " +
                         "JOIN setor s ON func.id_setor = s.id_setor " +
                         "WHERE f.nm_completo = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmtAval = conn.prepareStatement(sqlAval)) {
         
            stmtAval.setString(1, nomeFuncionario);
            
            try (ResultSet rs = stmtAval.executeQuery()) {
                
                if (rs.next()) {
                    avaliacao = new Avaliacao();
                    
                    avaliacao.setArea(rs.getString("nm_setor"));
                    avaliacao.setFuncionario(rs.getString("nm_completo"));
                    
                    avaliacao.setAutoavaliacao(0.0); 
                    avaliacao.setAvaliacaoGestor(0.0);
                    avaliacao.setAvaliacaoPares(0.0);
                    
                    int idFunc = rs.getInt("id_func");
                    
                    String sqlFeed = "SELECT ds_competencia, dt_feedback, ds_feedback, id_avaliador " +
                                     "FROM feedback WHERE id_func = ?";
                                     
                    try (PreparedStatement stmtFeed = conn.prepareStatement(sqlFeed)) {
                        stmtFeed.setInt(1, idFunc);
                        
                        try (ResultSet rsFeed = stmtFeed.executeQuery()) {
                            while (rsFeed.next()) {
                                Feedback fb = new Feedback();
                                fb.setCompetencia(rsFeed.getString("ds_competencia"));
                                fb.setData(rsFeed.getString("dt_feedback"));
                                fb.setDescricao(rsFeed.getString("ds_feedback"));
                                
                                avaliacao.adicionarFeedback(fb);
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar avaliação por funcionário: " + e.getMessage());
        }

        return avaliacao;
    }
}
