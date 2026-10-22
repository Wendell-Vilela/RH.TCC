package dao;

import model.CadastroCandidato;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CandidatoDAO {

    public boolean inserir(CadastroCandidato candidato) {
        String sql = "INSERT INTO candidato "
                + "(nm_completo, nr_cpf, nr_tel, ds_email, id_vaga_interesse) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, candidato.getNome());
            stmt.setString(2, candidato.getCpf());
            stmt.setString(3, candidato.getTelefone());
            stmt.setString(4, candidato.getEmail());
            stmt.setNull(5, Types.INTEGER);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar candidato: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(CadastroCandidato candidato) {
        String sql = "UPDATE candidato SET nm_completo = ?, nr_cpf = ?, "
                + "nr_tel = ?, ds_email = ? WHERE id_cand = ?";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, candidato.getNome());
            stmt.setString(2, candidato.getCpf());
            stmt.setString(3, candidato.getTelefone());
            stmt.setString(4, candidato.getEmail());
            stmt.setInt(5, candidato.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar candidato: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int idCandidato) {
        String sql = "DELETE FROM candidato WHERE id_cand = ?";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCandidato);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir candidato: " + e.getMessage());
            return false;
        }
    }

    public List<CadastroCandidato> listarCandidatos() {
        List<CadastroCandidato> lista = new ArrayList<>();

        String sql = "SELECT id_cand, nm_completo, nr_cpf, nr_tel, ds_email "
                + "FROM candidato ORDER BY id_cand";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CadastroCandidato candidato = new CadastroCandidato();

                candidato.setId(rs.getInt("id_cand"));
                candidato.setNome(rs.getString("nm_completo"));
                candidato.setCpf(rs.getString("nr_cpf"));
                candidato.setTelefone(rs.getString("nr_tel"));
                candidato.setEmail(rs.getString("ds_email"));
                candidato.setVaga("Geral");

                lista.add(candidato);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar candidatos: " + e.getMessage());
        }

        return lista;
    }

    public boolean cpfExiste(String cpf, int idIgnorado) {
        String sql = "SELECT id_cand FROM candidato WHERE nr_cpf = ? "
                + "AND id_cand <> ?";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.setInt(2, idIgnorado);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar CPF: " + e.getMessage());
            throw new RuntimeException("Não foi possível verificar o CPF.", e);
        }
    }
}