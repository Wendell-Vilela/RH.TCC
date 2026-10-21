package dao;

import model.CadastroVaga;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VagaDAO {

    public void inserir(CadastroVaga vaga) {
        
        String sql = "INSERT INTO vaga (id_setor, ds_cargo, tp_contrato, tp_modalidade, vl_salario_oferecido, st_vaga, ds_vaga) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, 1);
            stmt.setString(2, vaga.getCargo());
            stmt.setString(3, vaga.getTipoContrato());
            stmt.setString(4, vaga.getModalidade());
            
            double salarioNum = 0.00;
            try {
                if (vaga.getSalario() != null && !vaga.getSalario().isEmpty()) {
                    salarioNum = Double.parseDouble(vaga.getSalario().replace(",", "."));
                }
            } catch (NumberFormatException e) {
                salarioNum = 0.00;
            }
            stmt.setDouble(5, salarioNum);
            
            stmt.setString(6, vaga.getStatus());
            stmt.setString(7, vaga.getDescricao());

            stmt.executeUpdate();
            
            System.out.println("Vaga cadastrada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar vaga: " + e.getMessage());
        }
    }

    public void excluir(int idVaga) {
        
        String sql = "DELETE FROM vaga WHERE id_vaga = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVaga);
            
            stmt.executeUpdate();
            
            System.out.println("Vaga excluída com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir vaga: " + e.getMessage());
        }
    }

    public List<CadastroVaga> listarVagas() {
        
        List<CadastroVaga> lista = new ArrayList<>();
        
        String sql = "SELECT v.id_vaga, v.ds_cargo, s.nm_setor, v.tp_contrato, v.tp_modalidade, v.vl_salario_oferecido, v.st_vaga, v.ds_vaga " +
                     "FROM vaga v JOIN setor s ON v.id_setor = s.id_setor";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                
                CadastroVaga v = new CadastroVaga();
                
                v.setId(rs.getInt("id_vaga"));
                v.setCargo(rs.getString("ds_cargo"));
                v.setDepartamento(rs.getString("nm_setor"));
                v.setTipoContrato(rs.getString("tp_contrato"));
                v.setModalidade(rs.getString("tp_modalidade"));
                v.setSalario(String.valueOf(rs.getDouble("vl_salario_oferecido")));
                v.setStatus(rs.getString("st_vaga"));
                v.setDescricao(rs.getString("ds_vaga"));

                lista.add(v);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar vagas: " + e.getMessage());
        }

        return lista;
    }
}
