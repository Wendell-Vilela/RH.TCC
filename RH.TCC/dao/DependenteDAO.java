package dao;

import model.Dependentes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DependenteDAO {

    public void inserir(Dependentes dependente, int idFuncionario) {
        
        String sql = "INSERT INTO dependente (id_func, nm_dependente, tp_parentesco, dt_nasc) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            stmt.setString(2, dependente.getNome());
            stmt.setString(3, dependente.getParentesco());
            
            java.sql.Date dataNasc;
            try {
                dataNasc = java.sql.Date.valueOf(dependente.getNascimento());
            } catch (Exception e) {
                dataNasc = new java.sql.Date(System.currentTimeMillis());
            }
            stmt.setDate(4, dataNasc);

            stmt.executeUpdate();
            
            System.out.println("Dependente cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar dependente: " + e.getMessage());
        }
    }

    public List<Dependentes> listarPorFuncionario(int idFuncionario) {
        
        List<Dependentes> lista = new ArrayList<>();
        
        String sql = "SELECT nm_dependente, tp_parentesco, dt_nasc FROM dependente WHERE id_func = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    
                    Dependentes dep = new Dependentes();
                    
                    dep.setNome(rs.getString("nm_dependente"));
                    dep.setParentesco(rs.getString("tp_parentesco"));
                    dep.setNascimento(String.valueOf(rs.getDate("dt_nasc")));
                    dep.setAssistencia("Padrão");

                    lista.add(dep);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar dependentes: " + e.getMessage());
        }

        return lista;
    }
}
