package dao;

import model.Historico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDAO {

    public void inserir(Historico historico, int idFuncionario) {
        
        String sql = "INSERT INTO hist_prof (id_func, dt_ano, ds_evento, ds_cargo, ds_obs) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            stmt.setString(2, historico.getAno());
            stmt.setString(3, historico.getEvento());
            stmt.setString(4, historico.getCargo());
            stmt.setString(5, historico.getObservacao());

            stmt.executeUpdate();
            
            System.out.println("Histórico profissional cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar histórico profissional: " + e.getMessage());
        }
    }

    public List<Historico> listarPorFuncionario(int idFuncionario) {
        
        List<Historico> lista = new ArrayList<>();
        
        String sql = "SELECT dt_ano, ds_evento, ds_cargo, ds_obs FROM hist_prof WHERE id_func = ?";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Historico hist = new Historico();
                    hist.setAno(rs.getString("dt_ano"));
                    hist.setEvento(rs.getString("ds_evento"));
                    hist.setCargo(rs.getString("ds_cargo"));
                    hist.setObservacao(rs.getString("ds_obs"));

                    lista.add(hist);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar histórico profissional: " + e.getMessage());
        }

        return lista;
    }
}
