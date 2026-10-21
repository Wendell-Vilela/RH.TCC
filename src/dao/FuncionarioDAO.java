package dao;

import model.CadastroFuncionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public void inserir(CadastroFuncionario func) {
        
        String sql = "INSERT INTO funcionario (id_setor, id_cargo, nr_matricula, vl_salario_atual, st_func) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, 1);
            stmt.setInt(1, 1);
            stmt.setString(3, func.getMatricula());
            stmt.setDouble(4, 0.00);
            stmt.setString(5, func.getStatus());

            stmt.executeUpdate();
            
            System.out.println("Funcionário cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    public void excluir(int idFuncionario) {
        
        String sql = "DELETE FROM funcionario WHERE id_func = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            
            stmt.executeUpdate();
            
            System.out.println("Funcionário excluído com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir funcionário: " + e.getMessage());
        }
    }

    public List<CadastroFuncionario> listarFuncionarios() {
        
        List<CadastroFuncionario> lista = new ArrayList<>();
        
        String sql = "SELECT f.id_func, f.nr_matricula, f.st_func, d.nm_completo, d.ds_email, d.nr_tel " +
                     "FROM funcionario f " +
                     "JOIN dado_pessoal d ON f.id_func = d.id_func";

                     try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                
                CadastroFuncionario func = new CadastroFuncionario();
                
                func.setId(rs.getInt("id_func"));
                func.setMatricula(rs.getString("nr_matricula"));
                func.setStatus(rs.getString("st_func"));
                func.setNome(rs.getString("nm_completo"));
                func.setEmail(rs.getString("ds_email"));
                func.setTelefone(rs.getString("nr_tel"));

                lista.add(func);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }

        return lista;
    }
}
