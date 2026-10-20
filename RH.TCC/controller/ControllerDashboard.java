package controller;

import java.sql.*;
import java.text.*;
import javax.swing.JOptionPane;
import view.*;

public class ControllerDashboard {

    private final ViewTaxaTurnover viewTurnover;
    private final ViewFuncionariosAtivos viewAtivos;
    private final ViewNovasAdmissoes viewAdmissoes;
    private final Connection conexao;

    private final DecimalFormat dfMoeda = new DecimalFormat("R$ #,##0.00");
    private final DecimalFormat dfPercentual = new DecimalFormat("0.0'%");
    private final SimpleDateFormat dfData = new SimpleDateFormat("dd/MM/yyyy");

    public ControllerDashboard(ViewTaxaTurnover vTurnover, ViewFuncionariosAtivos vAtivos, ViewNovasAdmissoes vAdmissoes, Connection con) {
        this.viewTurnover = vTurnover;
        this.viewAtivos = vAtivos;
        this.viewAdmissoes = vAdmissoes;
        this.conexao = con;
    }

    // Carrega o dashboard completo de uma só vez (sem filtros de data)
    public void carregarTodosOsDados() {
        carregarModuloTurnover();
        carregarModuloFuncionariosAtivos();
        carregarModuloNovasAdmissoes();
    }

    // 1. MÓDULO TURNOVER
    public void carregarModuloTurnover() {
        if (viewTurnover == null) return;
        viewTurnover.limparTabela();

        String sqlGlobais = "SELECT (SELECT COUNT(*) FROM funcionario WHERE st_func = 'Ativo') AS total_ativos, "
                + "COUNT(CASE WHEN tp_desligamento = 'Voluntário' THEN 1 END) AS vol, "
                + "COUNT(CASE WHEN tp_desligamento = 'Involuntário' THEN 1 END) AS invol FROM funcionario";

        String sqlSetores = "SELECT s.nm_setor AS dep, COUNT(f.id_func) AS total, "
                + "SUM(CASE WHEN f.tp_desligamento = 'Voluntário' THEN 1 ELSE 0 END) AS vol, "
                + "SUM(CASE WHEN f.tp_desligamento = 'Involuntário' THEN 1 ELSE 0 END) AS invol "
                + "FROM setor s LEFT JOIN funcionario f ON s.id_setor = f.id_setor GROUP BY s.id_setor, s.nm_setor";

        try {
            int totalAtivos = 0, vol = 0, invol = 0;
            try (PreparedStatement stmt = conexao.prepareStatement(sqlGlobais); 
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalAtivos = rs.getInt("total_ativos");
                    vol = rs.getInt("vol");
                    invol = rs.getInt("invol");
                }
            }

            int saidas = vol + invol;
            double taxaTurnover = totalAtivos > 0 ? ((double) saidas / totalAtivos) * 100 : 0.0;
            double estabilidade = Math.max(0.0, 100.0 - taxaTurnover);

            // Atualiza o Gráfico de Pizza
            viewTurnover.atualizarGrafico(
                saidas > 0 ? ((double) vol / saidas) * 100 : 0, 
                saidas > 0 ? ((double) invol / saidas) * 100 : 0, 
                estabilidade
            );

            // Preenche a Tabela por Departamento
            try (PreparedStatement stmt = conexao.prepareStatement(sqlSetores); 
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int totalSetor = rs.getInt("total"), vSetor = rs.getInt("vol"), iSetor = rs.getInt("invol");
                    double txSetor = totalSetor > 0 ? ((double) (vSetor + iSetor) / totalSetor) * 100 : 0.0;
                    
                    viewTurnover.adicionarLinhaTabela(new Object[]{
                        rs.getString("dep"), totalSetor, vSetor, iSetor, dfPercentual.format(txSetor)
                    });
                }
            }
            viewTurnover.atualizarAvisoRodape("Dados consolidados com sucesso.");

        } catch (SQLException e) {
            viewTurnover.atualizarAvisoRodape("Erro ao carregar dados de Turnover.");
            exibirErro("Erro no Módulo Turnover", e);
        }
    }

    // 2. MÓDULO FUNCIONÁRIOS ATIVOS
    public void carregarModuloFuncionariosAtivos() {
        if (viewAtivos == null) return;
        viewAtivos.limparTabela();

        String sqlTotais = "SELECT COUNT(id_func) AS total, COALESCE(SUM(vl_salario_atual), 0) AS folha FROM funcionario WHERE st_func = 'Ativo'";
        String sqlSetores = "SELECT s.nm_setor AS dep, COUNT(f.id_func) AS ativos, COALESCE(SUM(f.vl_salario_atual), 0) AS folha "
                          + "FROM setor s LEFT JOIN funcionario f ON s.id_setor = f.id_setor AND f.st_func = 'Ativo' GROUP BY s.id_setor, s.nm_setor";

        try {
            int totalEmpresa = 0;
            try (PreparedStatement stmt = conexao.prepareStatement(sqlTotais); 
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalEmpresa = rs.getInt("total");
                    viewAtivos.getLabelTotalAtivos().setText(String.valueOf(totalEmpresa));
                    viewAtivos.getLabelCustoTotalFolha().setText(dfMoeda.format(rs.getDouble("folha")));
                }
            }

            try (PreparedStatement stmt = conexao.prepareStatement(sqlSetores); 
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int ativosSetor = rs.getInt("ativos");
                    double rep = totalEmpresa > 0 ? ((double) ativosSetor / totalEmpresa) * 100 : 0.0;
                    viewAtivos.adicionarLinhaTabela(new Object[]{
                        rs.getString("dep"), ativosSetor, dfPercentual.format(rep), dfMoeda.format(rs.getDouble("folha"))
                    });
                }
            }
        } catch (SQLException e) {
            exibirErro("Erro no Módulo Funcionários Ativos", e);
        }
    }

    // 3. MÓDULO NOVAS ADMISSÕES (GERAL / SEM DATA)
    public void carregarModuloNovasAdmissoes() {
        if (viewAdmissoes == null) return;
        viewAdmissoes.limparTabela();
        viewAdmissoes.limparGraficoBarras();

        String sqlTabela = "SELECT f.id_func, dp.nm_completo, COALESCE(df.nr_doc, 'N/I') AS cpf, f.dt_adm, s.nm_setor, c.nm_cargo "
                         + "FROM funcionario f "
                         + "INNER JOIN dado_pessoal dp ON f.id_func = dp.id_func "
                         + "INNER JOIN setor s ON f.id_setor = s.id_setor "
                         + "INNER JOIN cargo c ON f.id_cargo = c.id_cargo "
                         + "LEFT JOIN doc_func df ON f.id_func = df.id_func AND df.tp_doc = 'CPF' "
                         + "ORDER BY f.dt_adm DESC";

        String sqlGrafico = "SELECT s.nm_setor AS dep, COUNT(f.id_func) AS qtd "
                          + "FROM setor s LEFT JOIN funcionario f ON s.id_setor = f.id_setor "
                          + "GROUP BY s.id_setor, s.nm_setor";

        try {
            int totalAdmitidos = 0;
            try (PreparedStatement stmt = conexao.prepareStatement(sqlTabela); 
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    totalAdmitidos++;
                    viewAdmissoes.adicionarLinhaTabela(new Object[]{
                        rs.getInt("id_func"), rs.getString("nm_completo"), rs.getString("cpf"),
                        dfData.format(rs.getDate("dt_adm")), rs.getString("nm_setor"), rs.getString("nm_cargo")
                    });
                }
            }
            viewAdmissoes.getLabelTotalAdmissoes().setText("Total geral de contratações: " + totalAdmitidos);

            try (PreparedStatement stmt = conexao.prepareStatement(sqlGrafico); 
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int qtd = rs.getInt("qtd");
                    int pct = totalAdmitidos > 0 ? (int) (((double) qtd / totalAdmitidos) * 100) : 0;
                    viewAdmissoes.adicionarBarraGrafico(rs.getString("dep"), qtd, pct);
                }
            }
            viewAdmissoes.revalidarEAtualizarGrafico();
        } catch (SQLException e) {
            exibirErro("Erro no Módulo Novas Admissões", e);
        }
    }

    // EXIBIÇÃO DE ERRO PADRONIZADA
    private void exibirErro(String titulo, Exception e) {
        JOptionPane.showMessageDialog(null, "Falha ao processar dados no banco:\n" + e.getMessage(), titulo, JOptionPane.ERROR_MESSAGE);
    }
}