package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    public void inserir(String nomeCurso, int cargaHoraria, String fornecedor, String tipoCurso) {
        
        String sql = "INSERT INTO curso (nm_curso, qtd_carga_horaria, nm_fornecedor, tp_categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeCurso);
            stmt.setInt(2, cargaHoraria);
            stmt.setString(3, fornecedor);
            stmt.setString(4, tipoCurso);

            stmt.executeUpdate();
            
            System.out.println("Curso cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar curso: " + e.getMessage());
        }
    }

    public void excluir(int idCurso) {
        
        String sql = "DELETE FROM curso WHERE id_curso = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCurso);
            
            stmt.executeUpdate();
            
            System.out.println("Curso excluído com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir curso: " + e.getMessage());
        }
    }
}
