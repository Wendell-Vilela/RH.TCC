package dao;

import model.Contratacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContratacaoDAO {

    public void inserir(Contratacao contratacao, int idCandidato, int idVaga) {
        
        String sql = "INSERT INTO contratacao (id_cand, id_vaga, dt_adm, vl_salario_final, tp_contrato, st_contratacao) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCandidato);
            stmt.setInt(2, idVaga);
            
            java.sql.Date dataAdm;
            try {
                dataAdm = java.sql.Date.valueOf(contratacao.getDataAdmissao());
            } catch (Exception e) {
                dataAdm = new java.sql.Date(System.currentTimeMillis());
            }
            stmt.setDate(3, dataAdm);

            double salarioNum = 0.00;
            try {
                if (contratacao.getSalario() != null && !contratacao.getSalario().isEmpty()) {
                    salarioNum = Double.parseDouble(contratacao.getSalario().replace(",", "."));
                }
            } catch (NumberFormatException e) {
                salarioNum = 0.00;
            }
            stmt.setDouble(4, salarioNum);

            stmt.setString(5, contratacao.getTipoContrato());
            stmt.setString(6, "Efetivado");

            stmt.executeUpdate();
            
            System.out.println("Contratação registrada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao registrar contratação: " + e.getMessage());
        }
    }

    public void excluir(int idContratacao) {

        String sql = "DELETE FROM contratacao WHERE id_contratacao = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idContratacao);
            
            stmt.executeUpdate();
            
            System.out.println("Contratação excluída com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir contratação: " + e.getMessage());
        }
    }

    public List<Contratacao> listarContratacoes() {
        
        List<Contratacao> lista = new ArrayList<>();
        
        String sql = "SELECT c.id_contratacao, cand.nm_completo AS candidato, v.ds_cargo AS cargo, " +
                     "c.dt_adm, c.vl_salario_final, c.tp_contrato " +
                     "FROM contratacao c " +
                     "JOIN candidato cand ON c.id_cand = cand.id_cand " +
                     "JOIN vaga v ON c.id_vaga = v.id_vaga";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                
                Contratacao c = new Contratacao();
                
                c.setId(rs.getInt("id_contratacao"));
                c.setCandidato(rs.getString("candidato"));
                c.setCargo(rs.getString("cargo"));
                c.setDataAdmissao(String.valueOf(rs.getDate("dt_adm")));
                c.setSalario(String.valueOf(rs.getDouble("vl_salario_final")));
                c.setTipoContrato(rs.getString("tp_contrato"));

                lista.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar contratações: " + e.getMessage());
        }

        return lista;
    }
}
