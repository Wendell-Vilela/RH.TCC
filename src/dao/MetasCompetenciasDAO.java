package dao;

import model.MetasCompetencias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetasCompetenciasDAO {

    public void inserir(MetasCompetencias meta, int idFuncionario) {
        
        String sql = "INSERT INTO meta_indiv (id_func, ds_meta, dt_prazo, st_meta) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            stmt.setString(2, meta.getMeta());
            
            java.sql.Date dataPrazo;
            try {
                dataPrazo = java.sql.Date.valueOf(meta.getPrazo());
            } catch (Exception e) {
                dataPrazo = new java.sql.Date(System.currentTimeMillis());
            }
            stmt.setDate(3, dataPrazo);

            stmt.setString(4, meta.getStatus());

            stmt.executeUpdate();
            
            System.out.println("Meta cadastrada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar meta: " + e.getMessage());
        }
    }

    public void excluir(int idMeta) {
        
        String sql = "DELETE FROM meta_indiv WHERE id_meta = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMeta);
            
            stmt.executeUpdate();
            
            System.out.println("Meta excluída com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir meta: " + e.getMessage());
        }
    }

    public List<MetasCompetencias> listarMetas() {
        
        List<MetasCompetencias> lista = new ArrayList<>();
        
        String sql = "SELECT mi.id_meta, dp.nm_completo, f.ds_cargo, s.nm_setor, mi.ds_meta, mi.dt_prazo, mi.st_meta " +
                     "FROM meta_indiv mi " +
                     "JOIN funcionario f ON mi.id_func = f.id_func " +
                     "JOIN dado_pessoal dp ON f.id_func = dp.id_func " +
                     "JOIN setor s ON f.id_setor = s.id_setor";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MetasCompetencias mc = new MetasCompetencias();
                
                mc.setId(rs.getInt("id_meta"));
                mc.setFuncionario(rs.getString("nm_completo"));
                mc.setCargo(rs.getString("ds_cargo"));
                mc.setGestor("Gestor Responsável"); 
                mc.setArea(rs.getString("nm_setor"));
                mc.setMeta(rs.getString("ds_meta"));
                mc.setPrazo(String.valueOf(rs.getDate("dt_prazo")));
                mc.setStatus(rs.getString("st_meta"));

                lista.add(mc);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar metas: " + e.getMessage());
        }

        return lista;
    }
}
