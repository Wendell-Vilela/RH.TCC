package dao;

import model.Feedback;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    public void inserir(Feedback feedback, int idFuncionario, int idAvaliador) {
        
        String sql = "INSERT INTO feedback (id_func, id_avaliador, ds_competencia, dt_feedback, ds_feedback) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            stmt.setInt(2, idAvaliador);
            stmt.setString(3, feedback.getCompetencia());
            
            stmt.setDate(4, java.sql.Date.valueOf(feedback.getData() != null ? feedback.getData() : LocalDate.now()));
            
            stmt.setString(5, feedback.getFeedback());

            stmt.executeUpdate();
            
            System.out.println("Feedback cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar feedback: " + e.getMessage());
        }
    }

    public void excluir(int idFeedback) {
        
        String sql = "DELETE FROM feedback WHERE id_feedback = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFeedback);
            
            stmt.executeUpdate();
            
            System.out.println("Feedback excluído com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir feedback: " + e.getMessage());
        }
    }

    public List<Feedback> listarFeedbacks() {
        
        List<Feedback> lista = new ArrayList<>();
        
        String sql = "SELECT ds_competencia, dt_feedback, ds_feedback FROM feedback";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                
                Feedback f = new Feedback();
                
                f.setCompetencia(rs.getString("ds_competencia"));
                
                java.sql.Date dataSql = rs.getDate("dt_feedback");
                if (dataSql != null) {
                    f.setData(dataSql.toLocalDate());
                }
                
                f.setFeedback(rs.getString("ds_feedback"));

                lista.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar feedbacks: " + e.getMessage());
        }

        return lista;
    }
}
