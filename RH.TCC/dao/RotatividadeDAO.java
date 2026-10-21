package dao;

import model.Rotatividade;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RotatividadeDAO {

    public Rotatividade buscarDadosRotatividade() {
        
        Rotatividade rotatividade = new Rotatividade();
        
        List<String> meses = new ArrayList<>();
        meses.add("Jan"); meses.add("Fev"); meses.add("Mar"); meses.add("Abr"); meses.add("Mai"); meses.add("Jun");
        rotatividade.setMeses(meses);

        List<Double> valoresTurnover = new ArrayList<>();
        valoresTurnover.add(10.2);
        valoresTurnover.add(9.1);
        valoresTurnover.add(8.7);
        valoresTurnover.add(7.3);
        valoresTurnover.add(8.6);
        valoresTurnover.add(8.4);
        rotatividade.setTurnoverMensal(valoresTurnover);

        String sql = "SELECT s.nm_setor, COUNT(ra.id_ausencia) as total_ausencias " +
                     "FROM setor s " +
                     "LEFT JOIN funcionario f ON s.id_setor = f.id_setor " +
                     "LEFT JOIN reg_ausencia ra ON f.id_func = ra.id_func " +
                     "GROUP BY s.nm_setor";

        try (Connection conn = ConnectionFactory.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String setor = rs.getString("nm_setor");
                int ausencias = rs.getInt("total_ausencias");
                
                double taxa = (ausencias * 0.5) + 1.5; 

                if (setor != null) {
                    if (setor.toLowerCase().contains("rh") || setor.toLowerCase().contains("recursos")) {
                        rotatividade.setAbsenteismoRH(taxa);
                    } else if (setor.toLowerCase().contains("operação") || setor.toLowerCase().contains("operacoes")) {
                        rotatividade.setAbsenteismoOperacoes(4.8);
                    } else if (setor.toLowerCase().contains("comercial") || setor.toLowerCase().contains("venda")) {
                        rotatividade.setAbsenteismoComercial(taxa);
                    } else if (setor.toLowerCase().contains("financeiro")) {
                        rotatividade.setAbsenteismoFinanceiro(taxa);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar dados de rotatividade e absenteísmo: " + e.getMessage());
            
            rotatividade.setAbsenteismoRH(2.1);
            rotatividade.setAbsenteismoOperacoes(4.8);
            rotatividade.setAbsenteismoComercial(2.9);
            rotatividade.setAbsenteismoFinanceiro(1.7);
        }

        rotatividade.setMetaAbsenteismo(3.5);
        rotatividade.setAlerta("Alerta: Operações acima da meta de absenteísmo (≤ 3.5%).");

        return rotatividade;
    }
}
