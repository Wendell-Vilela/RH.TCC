package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ViewTaxaTurnover extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel lblTurnoverGlobal = new JLabel("0.0%");
    private JLabel lblSaidasVoluntarias = new JLabel("0");
    private JLabel lblSaidasInvoluntarias = new JLabel("0");
    private JLabel lblEstabilidade = new JLabel("0.0%");
    private JLabel lblAvisoRodape;

    private DefaultTableModel modelTabela;
    private JTable tabelaSetores;
    private GraficoPizza graficoPizza;

    public ViewTaxaTurnover() {
        setLayout(new BorderLayout(15, 15));

        TitledBorder borda = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "ANÁLISE DA TAXA DE TURNOVER",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14)
        );

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                borda
        ));

        add(criarConteudoCentro(), BorderLayout.CENTER);
    }

    private JPanel criarConteudoCentro() {
        // 1 linha, 2 colunas com espaçamento horizontal de 10px (50% / 50%)
        JPanel centro = new JPanel(new GridLayout(1, 2, 10, 0));

        // Lado Esquerdo: Gráfico
        centro.add(criarCardGrafico());

        // Lado Direito: Tabela
        centro.add(criarCardDetalhamentoSetores());

        return centro;
    }

    private JPanel criarCardGrafico() {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Distribuição de Saídas e Retenção",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12)
        ));

        graficoPizza = new GraficoPizza();
        lblAvisoRodape = new JLabel("Aguardando carregamento de dados...", SwingConstants.CENTER);
        lblAvisoRodape.setFont(new Font("SansSerif", Font.ITALIC, 11));

        card.add(graficoPizza, BorderLayout.CENTER);
        card.add(lblAvisoRodape, BorderLayout.SOUTH);

        return card;
    }

    private JPanel criarCardDetalhamentoSetores() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Turnover Detalhado por Departamento",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12)
        ));

        String[] colunas = {"Departamento", "Colaboradores", "Voluntárias", "Involuntárias", "Taxa Setor (%)"};
        
        modelTabela = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabelaSetores = new JTable(modelTabela);
        tabelaSetores.setFillsViewportHeight(true);
        tabelaSetores.setRowHeight(24);
        tabelaSetores.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        
        DefaultTableCellRenderer rendererCentralizado = new DefaultTableCellRenderer();
        rendererCentralizado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < colunas.length; i++) {
            tabelaSetores.getColumnModel().getColumn(i).setCellRenderer(rendererCentralizado);
        }

        JScrollPane sp = new JScrollPane(tabelaSetores);
        sp.setBorder(BorderFactory.createEtchedBorder());

        card.add(sp, BorderLayout.CENTER);
        return card;
    }


    public void atualizarMetrics(String turnoverGlobal, String voluntarias, String involuntarias, String estabilidade) {
        lblTurnoverGlobal.setText(turnoverGlobal);
        lblSaidasVoluntarias.setText(voluntarias);
        lblSaidasInvoluntarias.setText(involuntarias);
        lblEstabilidade.setText(estabilidade);
    }

    public void atualizarAvisoRodape(String texto) {
        lblAvisoRodape.setText(texto);
    }

    public void limparTabela() {
        modelTabela.setRowCount(0);
    }

    public void adicionarLinhaTabela(Object[] linha) {
        modelTabela.addRow(linha);
    }

    public void atualizarGrafico(double pctVoluntarias, double pctInvoluntarias, double pctEstabilidade) {
        graficoPizza.setDados(pctVoluntarias, pctInvoluntarias, pctEstabilidade);
    }

    public JLabel getLabelTurnoverGlobal() {
        return lblTurnoverGlobal;
    }

    public JLabel getLabelSaidasVoluntarias() {
        return lblSaidasVoluntarias;
    }

    public JLabel getLabelSaidasInvoluntarias() {
        return lblSaidasInvoluntarias;
    }

    public JLabel getLabelEstabilidade() {
        return lblEstabilidade;
    }

    public GraficoPizza getGraficoRosca() {
        return graficoPizza;
    }

    public JTable getTabelaSetores() {
        return tabelaSetores;
    }

    public DefaultTableModel getModelTabela() {
        return modelTabela;
    }

    public void exibirMensagemErro(String mensagem) {
        lblAvisoRodape.setText(mensagem);
    }


    public class GraficoPizza extends JPanel {
        private static final long serialVersionUID = 1L;

        private double pctVoluntarias = 0.0;
        private double pctInvoluntarias = 0.0;
        private double pctEstabilidade = 100.0;

        public GraficoPizza() {
            setOpaque(false);
        }

        public void setPercentualTurnover(double percentual) {
            this.pctVoluntarias = percentual;
            this.pctEstabilidade = 100.0 - percentual;
            repaint();
        }

        public void setDados(double pctVol, double pctInvol, double pctEst) {
            this.pctVoluntarias = pctVol;
            this.pctInvoluntarias = pctInvol;
            this.pctEstabilidade = pctEst;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int tamanho = Math.min(getWidth(), getHeight()) - 20;
            if (tamanho <= 0) return;

            int x = (getWidth() - tamanho) / 2;
            int y = (getHeight() - tamanho) / 2;

            int anguloVol = (int) Math.round((pctVoluntarias / 100.0) * 360);
            int anguloInvol = (int) Math.round((pctInvoluntarias / 100.0) * 360);
            int anguloEst = 360 - (anguloVol + anguloInvol);

            int anguloAtual = 90;

            g2d.setColor(getForeground().brighter());
            g2d.fillArc(x, y, tamanho, tamanho, anguloAtual, anguloEst);
            anguloAtual += anguloEst;

            g2d.setColor(getForeground().darker());
            g2d.fillArc(x, y, tamanho, tamanho, anguloAtual, anguloInvol);
            anguloAtual += anguloInvol;

            g2d.setColor(getForeground());
            g2d.fillArc(x, y, tamanho, tamanho, anguloAtual, anguloVol);

            g2d.drawOval(x, y, tamanho, tamanho);
        }
    }
}
