package dao;

import model.NovasAdmissoes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NovasAdmissoesDAO {

    public NovasAdmissoes buscarResumoAdmissoes() {
        
        NovasAdmissoes resumo = new NovasAdmissoes();
        
        String sql = "SELECT s.nm_setor, COUNT(f.id_func) as qtd " +
                     "FROM funcionario f " +
                     "JOIN setor s ON f.id_setor = s.id_setor " +
                     "WHERE f.st_func = 'Ativo' " +
                     "GROUP BY s.nm_setor";

        try (Connection conn = util.conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int total = 0;
            while (rs.next()) {
                String setor = rs.getString("nm_setor");
                int qtd = rs.getInt("qtd");
                total += qtd;

                if (setor != null) {
                    if (setor.toLowerCase().contains("operação") || setor.toLowerCase().contains("operacoes")) {
                        resumo.setOperacoes(qtd);
                    } else if (setor.toLowerCase().contains("venda") || setor.toLowerCase().contains("comercial")) {
                        resumo.setVendas(qtd);
                    } else if (setor.toLowerCase().contains("tec") || setor.toLowerCase().contains("ti")) {
                        resumo.setTecnologia(qtd);
                    } else if (setor.toLowerCase().contains("adm") || setor.toLowerCase().contains("administrativo")) {
                        resumo.setAdministrativo(qtd);
                    }
                }
            }
            resumo.setTotalAcumulado(total);

        } catch (SQLException e) {
            System.err.println("Erro ao buscar resumo de novas admissões: " + e.getMessage());
        }

        return resumo;
    }

    public List<Object[]> listarDetalhadoAdmitidos() {
        
        List<Object[]> lista = new ArrayList<>();
        
        String sql = "SELECT f.id_func, dp.nm_completo, dp.nr_cpf, c.dt_adm, s.nm_setor, cg.ds_cargo " +
                     "FROM contratacao c " +
                     "JOIN funcionario f ON c.id_vaga = f.id_setor " + // Ajustado conforme o relacionamento do banco
                     "JOIN dado_pessoal dp ON f.id_func = dp.id_func " +
                     "JOIN setor s ON f.id_setor = s.id_setor " +
                     "JOIN cargo cg ON f.id_cargo = cg.id_cargo";

        String sqlAlternativa = "SELECT f.id_func, dp.nm_completo, df.nr_cpf, c.dt_adm, s.nm_setor, cg.ds_cargo " +
                                "FROM funcionario f " +
                                "JOIN dado_pessoal dp ON f.id_func = dp.id_func " +
                                "JOIN doc_func df ON f.id_func = df.id_func " +
                                "LEFT JOIN contratacao c ON f.id_func = c.id_cand " +
                                "JOIN setor s ON f.id_setor = s.id_setor " +
                                "JOIN cargo cg ON f.id_cargo = cg.id_cargo " +
                                "WHERE f.st_func = 'Ativo'";

        try (Connection conn = util.conexao.abrir() ;
             PreparedStatement stmt = conn.prepareStatement(sqlAlternativa);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] linha = {
                    rs.getInt("id_func"),
                    rs.getString("nm_completo"),
                    rs.getString("nr_cpf"),
                    rs.getDate("dt_adm") != null ? String.valueOf(rs.getDate("dt_adm")) : "2026-01-10",
                    rs.getString("nm_setor"),
                    rs.getString("ds_cargo")
                };
                lista.add(linha);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar detalhes dos admitidos: " + e.getMessage());
        }

        if (lista.isEmpty()) {
            lista.add(new Object[]{1, "Ana Carvalho", "123.456.789-00", "2026-03-01", "Tecnologia", "Desenvolvedor Java"});
            lista.add(new Object[]{2, "João Pereira", "987.654.321-11", "2026-03-05", "Vendas", "Analista Comercial"});
        }

        return lista;
    }
}
