package dao;

import model.CursosTrilhas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursosTrilhasDAO {

    public List<CursosTrilhas> listarCursosPorFuncionario(String nomeFuncionario) {
        
        List<CursosTrilhas> lista = new ArrayList<>();
        
        String sql = "SELECT s.nm_setor, d.nm_completo, c.nm_curso, c.qtd_carga_horaria, c.nm_fornecedor, fc.st_curso " +
                     "FROM func_curso fc " +
                     "JOIN funcionario f ON fc.id_func = f.id_func " +
                     "JOIN dado_pessoal d ON f.id_func = d.id_func " +
                     "JOIN setor s ON f.id_setor = s.id_setor " +
                     "JOIN curso c ON fc.id_curso = c.id_curso " +
                     "WHERE d.nm_completo = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeFuncionario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                while (rs.next()) {
                    
                    CursosTrilhas ct = new CursosTrilhas();
                    
                    ct.setArea(rs.getString("nm_setor"));
                    ct.setFuncionario(rs.getString("nm_completo"));
                    ct.setCurso(rs.getString("nm_curso"));
                    ct.setCargaHoraria(rs.getInt("qtd_carga_horaria") + "h");
                    ct.setFornecedor(rs.getString("nm_fornecedor"));
                    ct.setStatus(rs.getString("st_curso"));

                    lista.add(ct);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar cursos e trilhas do funcionário: " + e.getMessage());
        }

        return lista;
    }
}
