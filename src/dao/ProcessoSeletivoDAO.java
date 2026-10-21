package dao;

import model.ProcessoSeletivo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProcessoSeletivoDAO {

    public void inserir(ProcessoSeletivo ps, int idCandidato, int idVaga) {
        
        String sql = "INSERT INTO proc_seletivo (id_cand, id_vaga, ds_etapa, st_proc) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCandidato);
            stmt.setInt(2, idVaga);
            stmt.setString(3, ps.getEtapaAtual());
            stmt.setString(4, ps.getStatus());

            stmt.executeUpdate();
            
            System.out.println("Processo seletivo cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar processo seletivo: " + e.getMessage());
        }
    }

    public void atualizar(ProcessoSeletivo ps, int idProcesso) {
        
        String sql = "UPDATE proc_seletivo SET ds_etapa = ?, st_proc = ? WHERE id_proc = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ps.getEtapaAtual());
            stmt.setString(2, ps.getStatus());
            stmt.setInt(3, idProcesso);

            stmt.executeUpdate();
            
            System.out.println("Processo seletivo atualizado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar processo seletivo: " + e.getMessage());
        }
    }

    public void excluir(int idProcesso) {
        
        String sql = "DELETE FROM proc_seletivo WHERE id_proc = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProcesso);
            stmt.executeUpdate();
            
            System.out.println("Processo seletivo excluído com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir processo seletivo: " + e.getMessage());
        }
    }

    public List<ProcessoSeletivo> listarProcessos() {
        
        List<ProcessoSeletivo> lista = new ArrayList<>();
        
        String sql = "SELECT ps.id_proc, c.nm_completo AS candidato, v.ds_cargo AS vaga, " +
                     "ps.ds_etapa, ps.st_proc " +
                     "FROM proc_seletivo ps " +
                     "JOIN candidato c ON ps.id_cand = c.id_cand " +
                     "JOIN vaga v ON ps.id_vaga = v.id_vaga";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProcessoSeletivo ps = new ProcessoSeletivo();
                
                ps.setId(rs.getInt("id_proc"));
                ps.setCandidato(rs.getString("candidato"));
                ps.setVaga(rs.getString("vaga"));
                ps.setRecrutador("Equipe RH");
                ps.setEtapaAtual(rs.getString("ds_etapa"));
                ps.setStatus(rs.getString("st_proc"));

                lista.add(ps);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar processos seletivos: " + e.getMessage());
        }

        return lista;
    }
}
