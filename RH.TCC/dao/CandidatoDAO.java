package dao;

import model.CadastroCandidato;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CandidatoDAO {

    public void inserir(CadastroCandidato candidato) {
        
        String sql = "INSERT INTO candidato (nm_completo, nr_cpf, nr_tel, ds_email, id_vaga_interesse) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, candidato.getNome());
            stmt.setString(2, candidato.getCpf());
            stmt.setString(3, candidato.getTelefone());
            stmt.setString(4, candidato.getEmail());
            
            stmt.setNull(5, java.sql.Types.INTEGER); 

            stmt.executeUpdate();
            
            System.out.println("Candidato cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar candidato: " + e.getMessage());
        }
    }

    public void excluir(int idCandidato) {
        
        String sql = "DELETE FROM candidato WHERE id_cand = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCandidato);
            
            stmt.executeUpdate();
            
            System.out.println("Candidato excluído com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir candidato: " + e.getMessage());
        }
    }

    public List<CadastroCandidato> listarCandidatos() {
        
        List<CadastroCandidato> lista = new ArrayList<>();
        
        String sql = "SELECT id_cand, nm_completo, nr_cpf, nr_tel, ds_email FROM candidato";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                
                CadastroCandidato c = new CadastroCandidato();
                c.setId(rs.getInt("id_cand"));
                c.setNome(rs.getString("nm_completo"));
                c.setCpf(rs.getString("nr_cpf"));
                c.setTelefone(rs.getString("nr_tel"));
                c.setEmail(rs.getString("ds_email"));
                c.setVaga("Geral");

                lista.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar candidatos: " + e.getMessage());
        }

        return lista;
    }
}
