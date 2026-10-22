
package dao;

import model.Contratacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContratacaoDAO {

    public boolean inserir(Contratacao contratacao, int idCandidato, int idVaga) {
        String sql = "INSERT INTO contratacao (id_cand, id_vaga, dt_adm, vl_salario_final, tp_contrato, st_contratacao) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCandidato);
            stmt.setInt(2, idVaga);
            stmt.setDate(3, java.sql.Date.valueOf(contratacao.getDataAdmissao()));
            stmt.setBigDecimal(4, new java.math.BigDecimal(
                contratacao.getSalario().replace(",", ".")
            ));
            stmt.setString(5, contratacao.getTipoContrato());
            stmt.setString(6, "Efetivado");

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao registrar contratação: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Contratacao contratacao, int idCandidato, int idVaga) {
        String sql = "UPDATE contratacao SET id_cand = ?, id_vaga = ?, dt_adm = ?, vl_salario_final = ?, tp_contrato = ? WHERE id_contratacao = ?";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCandidato);
            stmt.setInt(2, idVaga);
            stmt.setDate(3, java.sql.Date.valueOf(contratacao.getDataAdmissao()));
            stmt.setBigDecimal(4, new java.math.BigDecimal(
                contratacao.getSalario().replace(",", ".")
            ));
            stmt.setString(5, contratacao.getTipoContrato());
            stmt.setInt(6, contratacao.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar contratação: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int idContratacao) {
        String sql = "DELETE FROM contratacao WHERE id_contratacao = ?";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idContratacao);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir contratação: " + e.getMessage());
            return false;
        }
    }

    public List<Contratacao> listarContratacoes() {
        List<Contratacao> lista = new ArrayList<>();

        String sql = "SELECT c.id_contratacao, cand.nm_completo AS candidato, " +
                     "v.ds_cargo AS cargo, c.dt_adm, c.vl_salario_final, " +
                     "c.tp_contrato FROM contratacao c " +
                     "JOIN candidato cand ON c.id_cand = cand.id_cand " +
                     "JOIN vaga v ON c.id_vaga = v.id_vaga";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Contratacao c = new Contratacao();

                c.setId(rs.getInt("id_contratacao"));
                c.setCandidato(rs.getString("candidato"));
                c.setCargo(rs.getString("cargo"));
                c.setDataAdmissao(rs.getDate("dt_adm").toString());
                c.setSalario(rs.getBigDecimal("vl_salario_final").toPlainString());
                c.setTipoContrato(rs.getString("tp_contrato"));

                lista.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar contratações: " + e.getMessage());
        }

        return lista;
    }

    public int buscarIdCandidato(String nome) {
        String sql = "SELECT id_cand FROM candidato WHERE nm_completo = ?";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_cand");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar candidato: " + e.getMessage());
        }

        return -1;
    }

    public int buscarIdVaga(String cargo) {
        String sql = "SELECT id_vaga FROM vaga WHERE ds_cargo = ?";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cargo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_vaga");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar vaga: " + e.getMessage());
        }

        return -1;
    }
}
