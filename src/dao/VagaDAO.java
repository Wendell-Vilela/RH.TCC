
package dao;

import model.CadastroVaga;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VagaDAO {

    public boolean inserir(CadastroVaga vaga) {
        String sql = "INSERT INTO vaga "
                + "(id_setor, ds_cargo, tp_contrato, tp_modalidade, "
                + "vl_salario_oferecido, st_vaga, ds_vaga) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = util.conexao.abrir()) {
            int idSetor = obterIdSetor(conn, vaga.getDepartamento());

            if (idSetor == -1) {
                System.err.println("Departamento não encontrado.");
                return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idSetor);
                stmt.setString(2, vaga.getCargo());
                stmt.setString(3, vaga.getTipoContrato());
                stmt.setString(4, vaga.getModalidade());
                stmt.setBigDecimal(5, new BigDecimal(
                        vaga.getSalario().replace(",", ".")));
                stmt.setString(6, vaga.getStatus());
                stmt.setString(7, vaga.getDescricao());

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException | NumberFormatException e) {
            System.err.println("Erro ao cadastrar vaga: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(CadastroVaga vaga) {
        String sql = "UPDATE vaga SET id_setor = ?, ds_cargo = ?, "
                + "tp_contrato = ?, tp_modalidade = ?, "
                + "vl_salario_oferecido = ?, st_vaga = ?, ds_vaga = ? "
                + "WHERE id_vaga = ?";

        try (Connection conn = util.conexao.abrir()) {
            int idSetor = obterIdSetor(conn, vaga.getDepartamento());

            if (idSetor == -1) {
                System.err.println("Departamento não encontrado.");
                return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idSetor);
                stmt.setString(2, vaga.getCargo());
                stmt.setString(3, vaga.getTipoContrato());
                stmt.setString(4, vaga.getModalidade());
                stmt.setBigDecimal(5, new BigDecimal(
                        vaga.getSalario().replace(",", ".")));
                stmt.setString(6, vaga.getStatus());
                stmt.setString(7, vaga.getDescricao());
                stmt.setInt(8, vaga.getId());

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException | NumberFormatException e) {
            System.err.println("Erro ao atualizar vaga: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int idVaga) {
        String sql = "DELETE FROM vaga WHERE id_vaga = ?";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVaga);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir vaga: " + e.getMessage());
            return false;
        }
    }

    public List<CadastroVaga> listarVagas() {
        List<CadastroVaga> lista = new ArrayList<>();

        String sql = "SELECT v.id_vaga, v.ds_cargo, s.nm_setor, "
                + "v.tp_contrato, v.tp_modalidade, "
                + "v.vl_salario_oferecido, v.st_vaga, v.ds_vaga "
                + "FROM vaga v JOIN setor s ON v.id_setor = s.id_setor "
                + "ORDER BY v.id_vaga";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CadastroVaga vaga = new CadastroVaga();

                vaga.setId(rs.getInt("id_vaga"));
                vaga.setCargo(rs.getString("ds_cargo"));
                vaga.setDepartamento(rs.getString("nm_setor"));
                vaga.setTipoContrato(rs.getString("tp_contrato"));
                vaga.setModalidade(rs.getString("tp_modalidade"));
                vaga.setSalario(rs.getBigDecimal(
                        "vl_salario_oferecido").toPlainString());
                vaga.setStatus(rs.getString("st_vaga"));
                vaga.setDescricao(rs.getString("ds_vaga"));

                lista.add(vaga);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar vagas: " + e.getMessage());
        }

        return lista;
    }

    private int obterIdSetor(Connection conn, String departamento)
            throws SQLException {

        String sql = "SELECT id_setor FROM setor WHERE nm_setor = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departamento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_setor");
                }
            }
        }

        return -1;
    }
}
