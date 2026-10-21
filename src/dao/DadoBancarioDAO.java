package dao;

import model.DadosBancarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DadoBancarioDAO {

    public void inserir(DadosBancarios dados, int idFuncionario) {
        
        String sql = "INSERT INTO dado_bancario (id_func, nm_banco, nr_agencia, nr_conta, tp_conta, cd_pix) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            stmt.setString(2, dados.getBanco());
            stmt.setString(3, dados.getAgencia());
            stmt.setString(4, dados.getConta());
            stmt.setString(5, dados.getTipoConta());
            stmt.setString(6, dados.getPix());

            stmt.executeUpdate();
            
            System.out.println("Dados bancários cadastrados com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar dados bancários: " + e.getMessage());
        }
    }

    public DadosBancarios buscarPorFuncionario(int idFuncionario) {
        
        DadosBancarios dados = null;
        
        String sql = "SELECT nm_banco, nr_agencia, nr_conta, tp_conta, cd_pix FROM dado_bancario WHERE id_func = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dados = new DadosBancarios();
                    dados.setBanco(rs.getString("nm_banco"));
                    dados.setAgencia(rs.getString("nr_agencia"));
                    dados.setConta(rs.getString("nr_conta"));
                    dados.setTipoConta(rs.getString("tp_conta"));
                    dados.setPix(rs.getString("cd_pix"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar dados bancários: " + e.getMessage());
        }

        return dados;
    }
}
