package dao;

import model.DadosPessoais;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DadoPessoalDAO {

    public void inserir(DadosPessoais dados, int idFuncionario) {
        
        String sql = "INSERT INTO dado_pessoal (id_func, nm_completo, dt_nasc, tp_estado_civil, ds_naturalidade, ds_nacionalidade, ds_endereco, nm_cidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            stmt.setString(2, dados.getNome());
            
            java.sql.Date dataNasc;
            try {
                dataNasc = java.sql.Date.valueOf(dados.getNascimento());
            } catch (Exception e) {
                dataNasc = new java.sql.Date(System.currentTimeMillis());
            }
            stmt.setDate(3, dataNasc);

            stmt.setString(4, dados.getEstadoCivil());
            stmt.setString(5, dados.getNaturalidade());
            stmt.setString(6, dados.getNacionalidade());
            stmt.setString(7, dados.getEndereco());
            stmt.setString(8, dados.getCidade());

            stmt.executeUpdate();
            
            System.out.println("Dados pessoais cadastrados com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar dados pessoais: " + e.getMessage());
        }
    }

    public DadosPessoais buscarPorFuncionario(int idFuncionario) {
        
        DadosPessoais dados = null;
        
        String sql = "SELECT nm_completo, dt_nasc, tp_estado_civil, ds_naturalidade, ds_nacionalidade, ds_endereco, nm_cidade FROM dado_pessoal WHERE id_func = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dados = new DadosPessoais();
                    dados.setNome(rs.getString("nm_completo"));
                    dados.setNascimento(String.valueOf(rs.getDate("dt_nasc")));
                    dados.setEstadoCivil(rs.getString("tp_estado_civil"));
                    dados.setNaturalidade(rs.getString("ds_naturalidade"));
                    dados.setNacionalidade(rs.getString("ds_nacionalidade"));
                    dados.setEndereco(rs.getString("ds_endereco"));
                    dados.setCidade(rs.getString("nm_cidade"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar dados pessoais: " + e.getMessage());
        }

        return dados;
    }
}
