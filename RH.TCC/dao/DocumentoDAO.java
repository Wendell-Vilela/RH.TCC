package dao;

import model.Documentos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DocumentoDAO {

    public void inserir(Documentos doc, int idFuncionario) {
        
        String sql = "INSERT INTO doc_func (id_func, nr_cpf, nr_rg, nr_passaporte, dt_val_passaporte, nr_pis, nr_ctps) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            stmt.setString(2, doc.getCpf());
            stmt.setString(3, doc.getRg());
            stmt.setString(4, doc.getPassaporte());
            
            java.sql.Date valPassaporte = null;
            try {
                if (doc.getValidadePassaporte() != null && !doc.getValidadePassaporte().isEmpty() && !doc.getValidadePassaporte().contains("*")) {
                    valPassaporte = java.sql.Date.valueOf(doc.getValidadePassaporte());
                }
            } catch (Exception e) {
                valPassaporte = null;
            }
            stmt.setDate(5, valPassaporte);

            stmt.setString(6, doc.getPis());
            stmt.setString(7, doc.getCtps());

            stmt.executeUpdate();
            
            System.out.println("Documentos cadastrados com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar documentos: " + e.getMessage());
        }
    }

    public Documentos buscarPorFuncionario(int idFuncionario) {
        
        Documentos doc = null;
        
        String sql = "SELECT nr_cpf, nr_rg, nr_passaporte, dt_val_passaporte, nr_pis, nr_ctps FROM doc_func WHERE id_func = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    doc = new Documentos();
                    doc.setCpf(rs.getString("nr_cpf"));
                    doc.setRg(rs.getString("nr_rg"));
                    doc.setPassaporte(rs.getString("nr_passaporte"));
                    
                    java.sql.Date dataVal = rs.getDate("dt_val_passaporte");
                    doc.setValidadePassaporte(dataVal != null ? String.valueOf(dataVal) : "");
                    
                    doc.setPis(rs.getString("nr_pis"));
                    doc.setCtps(rs.getString("nr_ctps"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar documentos: " + e.getMessage());
        }

        return doc;
    }
}
