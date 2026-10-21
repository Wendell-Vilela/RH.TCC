package controller;

import dao.FuncionariosAtivosDAO;
import dao.NovasAdmissoesDAO;
import dao.TaxaTurnoverDAO;
import model.FuncionariosAtivos;
import model.NovasAdmissoes;
import model.TaxaTurnover;
import view.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;

public class ControllerDashboard {

    private final ViewTaxaTurnover viewTurnover;
    private final ViewFuncionariosAtivos viewAtivos;
    private final ViewNovasAdmissoes viewAdmissoes;

    private final TaxaTurnoverDAO turnoverDAO;
    private final FuncionariosAtivosDAO ativosDAO;
    private final NovasAdmissoesDAO admissoesDAO;

    private final DecimalFormat dfMoeda = new DecimalFormat("R$ #,##0.00");
    private final DecimalFormat dfPercentual = new DecimalFormat("0.0'%'");
    private final SimpleDateFormat dfData = new SimpleDateFormat("dd/MM/yyyy");

    public ControllerDashboard(ViewTaxaTurnover vTurnover, ViewFuncionariosAtivos vAtivos, ViewNovasAdmissoes vAdmissoes) {
        this.viewTurnover = vTurnover;
        this.viewAtivos = vAtivos;
        this.viewAdmissoes = vAdmissoes;
        
        // Inicializa os objetos de acesso a dados
        this.turnoverDAO = new TaxaTurnoverDAO();
        this.ativosDAO = new FuncionariosAtivosDAO();
        this.admissoesDAO = new NovasAdmissoesDAO();
    }

    public void carregarTodosOsDados() {
        carregarModuloTurnover();
        carregarModuloFuncionariosAtivos();
        carregarModuloNovasAdmissoes();
    }

    //turnover
    public void carregarModuloTurnover() {
        if (viewTurnover == null) return;
        viewTurnover.limparTabela();

        try {
            List<TaxaTurnover> dadosTurnover = turnoverDAO.listarTurnoverPorDepartamento();
            
            int totalAtivosGeral = 0;
            int totalDesligadosGeral = 0;

            for (TaxaTurnover t : dadosTurnover) {
                totalAtivosGeral += t.getColaboradores();
                totalDesligadosGeral += t.getDesligamentos();

                viewTurnover.adicionarLinhaTabela(new Object[]{
                    t.getDepartamento(), 
                    t.getColaboradores(), 
                    0, // Voluntários (caso seu model decida separar depois)
                    t.getDesligamentos(), // Involuntários / Total Desligamentos
                    dfPercentual.format(t.getTaxaSetor())
                });
            }
            
            double taxaGeral = totalAtivosGeral > 0 ? ((double) totalDesligadosGeral / totalAtivosGeral) * 100 : 0.0;
            double estabilidade = Math.max(0.0, 100.0 - taxaGeral);

            viewTurnover.atualizarGrafico(50.0, 50.0, estabilidade);
            viewTurnover.atualizarAvisoRodape("Dados de Turnover carregados via DAO.");

        } catch (Exception e) {
            viewTurnover.atualizarAvisoRodape("Erro ao carregar dados de Turnover.");
            exibirErro("Erro no Módulo Turnover", e);
        }
    }

    //ativos
    public void carregarModuloFuncionariosAtivos() {
        if (viewAtivos == null) return;
        viewAtivos.limparTabela();

        try {
            FuncionariosAtivos fa = ativosDAO.buscarEstatisticasAtivos();

            viewAtivos.getLabelTotalAtivos().setText(String.valueOf(fa.getHeadcountTotal()));

            viewAtivos.getLabelCustoTotalFolha().setText(dfMoeda.format(fa.getEfetivos() * 3500.0)); // Cálculo estimado baseado na quantidade

            viewAtivos.adicionarLinhaTabela(new Object[]{
                fa.getMaiorArea(), 
                fa.getColaboradoresMaiorArea(), 
                dfPercentual.format(fa.getPercentualEfetivos()), 
                dfMoeda.format(fa.getColaboradoresMaiorArea() * 3500.0)
            });

        } catch (Exception e) {
            exibirErro("Erro no Módulo Funcionários Ativos", e);
        }
    }

    //admissôes
    public void carregarModuloNovasAdmissoes() {
        if (viewAdmissoes == null) return;
        viewAdmissoes.limparTabela();
        viewAdmissoes.limparGraficoBarras();

        try {
            List<Object[]> dadosAdmitidos = admissoesDAO.listarDetalhadoAdmitidos();
            for (Object[] linha : dadosAdmitidos) {
                viewAdmissoes.adicionarLinhaTabela(linha);
            }

            NovasAdmissoes resumo = admissoesDAO.buscarResumoAdmissoes();
            int total = resumo.getTotalAcumulado();
            
            viewAdmissoes.getLabelTotalAdmissoes().setText("Total geral de contratações: " + total);

            viewAdmissoes.adicionarBarraGrafico("Tecnologia", resumo.getTecnologia(), total > 0 ? (resumo.getTecnologia() * 100 / total) : 0);
            viewAdmissoes.adicionarBarraGrafico("Vendas", resumo.getVendas(), total > 0 ? (resumo.getVendas() * 100 / total) : 0);
            viewAdmissoes.adicionarBarraGrafico("Operações", resumo.getOperacoes(), total > 0 ? (resumo.getOperacoes() * 100 / total) : 0);
            viewAdmissoes.adicionarBarraGrafico("Administrativo", resumo.getAdministrativo(), total > 0 ? (resumo.getAdministrativo() * 100 / total) : 0);

            viewAdmissoes.revalidarEAtualizarGrafico();

        } catch (Exception e) {
            exibirErro("Erro no Módulo Novas Admissões", e);
        }
    }

    private void exibirErro(String titulo, Exception e) {
        JOptionPane.showMessageDialog(null, "Falha ao processar dados via DAO:\n" + e.getMessage(), titulo, JOptionPane.ERROR_MESSAGE);
    }
}
