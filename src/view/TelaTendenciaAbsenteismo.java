
package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.TendenciaAbsenteismoController;
import model.TendenciaAbsenteismo;
import model.FuncionarioAbsenteismo;

public class TelaTendenciaAbsenteismo extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<TendenciaAbsenteismo> dados;
    private GraphPanel grafico;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private TendenciaAbsenteismoController controller;

    public TelaTendenciaAbsenteismo() {
        dados = criarDadosFicticios();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        montar();
        grafico.setDados(dados);

        controller = new TendenciaAbsenteismoController(this);
        controller.carregarDados();
    }

    private List<TendenciaAbsenteismo> criarDadosFicticios() {
        List<TendenciaAbsenteismo> lista = new ArrayList<>();

        double[] valores = {
            2.1, 2.8, 2.4, 3.2, 2.7, 3.5,
            3.1, 3.8, 3.3, 4.1, 3.6, 4.3
        };

        for (int i = 0; i < valores.length; i++) {
            TendenciaAbsenteismo tendencia = new TendenciaAbsenteismo();
            tendencia.setMes(i + 1);
            tendencia.setPercentual(valores[i]);
            lista.add(tendencia);
        }

        return lista;
    }

    private void montar() {
        JPanel painelGrafico = new JPanel(new BorderLayout());
        painelGrafico.setBackground(Color.WHITE);
        painelGrafico.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Tendência de Absenteísmo"
            )
        );

        grafico = new GraphPanel(dados);
        painelGrafico.add(grafico, BorderLayout.CENTER);

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Absenteísmo por Funcionário"
            )
        );

        String[] colunas = {
            "ID",
            "Nome",
            "Faltas",
            "Taxa de Absenteísmo"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(35);
        tabela.setBackground(Color.WHITE);
        tabela.setForeground(Color.BLACK);
        tabela.setGridColor(Color.BLACK);
        tabela.setShowGrid(true);
        tabela.setFillsViewportHeight(true);

        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getTableHeader().setBackground(Color.WHITE);
        tabela.getTableHeader().setForeground(Color.BLACK);
        tabela.getTableHeader().setFont(
            tabela.getTableHeader().getFont().deriveFont(Font.BOLD)
        );

        DefaultTableCellRenderer centralizador =
            new DefaultTableCellRenderer();

        centralizador.setHorizontalAlignment(JLabel.CENTER);

        tabela.getColumnModel().getColumn(0)
            .setCellRenderer(centralizador);
        tabela.getColumnModel().getColumn(2)
            .setCellRenderer(centralizador);
        tabela.getColumnModel().getColumn(3)
            .setCellRenderer(centralizador);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(70);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scrollTabela = new JScrollPane(tabela);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        JSplitPane divisao = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            painelGrafico,
            painelTabela
        );

        divisao.setResizeWeight(0.5);
        divisao.setDividerSize(5);
        divisao.setContinuousLayout(true);

        add(divisao, BorderLayout.CENTER);
    }

    public void atualizarGrafico(List<TendenciaAbsenteismo> novosDados) {
        Runnable tarefa = () -> {
            if (novosDados == null) {
                dados = new ArrayList<>();
            } else {
                dados = new ArrayList<>(novosDados);
            }

            grafico.setDados(dados);
        };

        if (SwingUtilities.isEventDispatchThread()) {
            tarefa.run();
        } else {
            SwingUtilities.invokeLater(tarefa);
        }
    }

    public void atualizarTabela(
        List<FuncionarioAbsenteismo> funcionarios
    ) {
        Runnable tarefa = () -> {
            modeloTabela.setRowCount(0);

            if (funcionarios == null) {
                return;
            }

            for (FuncionarioAbsenteismo funcionario : funcionarios) {
                modeloTabela.addRow(new Object[] {
                    funcionario.getId(),
                    funcionario.getNome(),
                    funcionario.getFaltas(),
                    String.format(
                        Locale.forLanguageTag("pt-BR"),
                        "%.2f%%",
                        funcionario.getTaxaAbs()
                    )
                });
            }

            tabela.revalidate();
            tabela.repaint();
        };

        if (SwingUtilities.isEventDispatchThread()) {
            tarefa.run();
        } else {
            SwingUtilities.invokeLater(tarefa);
        }
    }

    public void limparTabela() {
        modeloTabela.setRowCount(0);
    }

    public void recarregarDados() {
        if (controller != null) {
            controller.atualizarDados();
        }
    }

    public JTable getTabela() {
        return tabela;
    }

    public GraphPanel getGrafico() {
        return grafico;
    }

    public void setGrafico(GraphPanel grafico) {
        this.grafico = grafico;
    }
}

class GraphPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final int padding = 25;
    private final int labelPadding = 25;
    private final Color lineColor = Color.BLACK;
    private final Color pointColor = Color.BLACK;
    private final Color gridColor = new Color(50, 50, 50);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private final int pointWidth = 6;
    private final int numberYDivisions = 5;

    private List<TendenciaAbsenteismo> dados;

    private final String[] meses = {
        "Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
        "Jul", "Ago", "Set", "Out", "Nov", "Dez"
    };

    public GraphPanel(List<TendenciaAbsenteismo> dados) {
        this.dados = dados == null
            ? new ArrayList<>()
            : new ArrayList<>(dados);

        setPreferredSize(new Dimension(600, 300));
        setBackground(Color.WHITE);
    }

    public void setDados(List<TendenciaAbsenteismo> dados) {
        this.dados = dados == null
            ? new ArrayList<>()
            : new ArrayList<>(dados);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (dados == null || dados.isEmpty()) {
            g.setColor(Color.GRAY);
            g.drawString(
                "Nenhum dado disponível",
                getWidth() / 2 - 70,
                getHeight() / 2
            );
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        try {
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int larguraGrafico = getWidth() - 2 * padding - labelPadding;
            int alturaGrafico = getHeight() - 2 * padding - labelPadding;

            if (larguraGrafico <= 0 || alturaGrafico <= 0) {
                return;
            }

            double maxScore = getMaxPercentual();
            double minScore = Math.min(0, getMinPercentual());

            if (maxScore <= minScore) {
                maxScore = minScore + 1.0;
            }

            double intervalo = maxScore - minScore;
            double xScale = dados.size() > 1
                ? (double) larguraGrafico / (dados.size() - 1)
                : 0;

            double yScale = alturaGrafico / intervalo;

            int baseX = padding + labelPadding;
            int baseY = getHeight() - padding - labelPadding;

            g2.setColor(Color.WHITE);
            g2.fillRect(baseX, padding, larguraGrafico, alturaGrafico);

            FontMetrics metrics = g2.getFontMetrics();

            for (int i = 0; i <= numberYDivisions; i++) {
                int y = baseY - (int) (
                    (double) i / numberYDivisions * alturaGrafico
                );

                double valor = minScore
                    + intervalo * i / numberYDivisions;

                g2.setColor(gridColor);
                g2.drawLine(baseX, y, getWidth() - padding, y);

                g2.setColor(Color.BLACK);
                String rotulo = String.format(
                    Locale.US,
                    "%.1f%%",
                    valor
                );

                int larguraRotulo = metrics.stringWidth(rotulo);

                g2.drawString(
                    rotulo,
                    baseX - larguraRotulo - 5,
                    y + metrics.getHeight() / 2 - 3
                );
            }

            List<Point> pontos = new ArrayList<>();

            for (int i = 0; i < dados.size(); i++) {
                TendenciaAbsenteismo dado = dados.get(i);

                int x = baseX + (int) Math.round(i * xScale);
                int y = baseY - (int) Math.round(
                    (dado.getPercentual() - minScore) * yScale
                );

                pontos.add(new Point(x, y));

                g2.setColor(gridColor);
                g2.drawLine(x, padding, x, baseY);

                int mes = dado.getMes();
                String rotuloMes = mes >= 1 && mes <= 12
                    ? meses[mes - 1]
                    : String.valueOf(mes);

                int larguraMes = metrics.stringWidth(rotuloMes);

                g2.setColor(Color.BLACK);
                g2.drawString(
                    rotuloMes,
                    x - larguraMes / 2,
                    baseY + metrics.getHeight() + 3
                );
            }

            g2.setColor(Color.BLACK);
            g2.drawLine(baseX, baseY, baseX, padding);
            g2.drawLine(baseX, baseY, getWidth() - padding, baseY);

            Stroke strokeAnterior = g2.getStroke();
            g2.setColor(lineColor);
            g2.setStroke(GRAPH_STROKE);

            for (int i = 0; i < pontos.size() - 1; i++) {
                Point p1 = pontos.get(i);
                Point p2 = pontos.get(i + 1);

                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            g2.setStroke(strokeAnterior);
            g2.setColor(pointColor);

            for (Point ponto : pontos) {
                g2.fillOval(
                    ponto.x - pointWidth / 2,
                    ponto.y - pointWidth / 2,
                    pointWidth,
                    pointWidth
                );
            }
        } finally {
            g2.dispose();
        }
    }

    private double getMinPercentual() {
        double min = Double.MAX_VALUE;

        for (TendenciaAbsenteismo dado : dados) {
            min = Math.min(min, dado.getPercentual());
        }

        return min == Double.MAX_VALUE ? 0 : min;
    }

    private double getMaxPercentual() {
        double max = 0;

        for (TendenciaAbsenteismo dado : dados) {
            max = Math.max(max, dado.getPercentual());
        }

        return max;
    }

    public String[] getMeses() {
        return meses.clone();
    }

    public void setMeses(String[] meses) {
        if (meses != null && meses.length == 12) {
            System.arraycopy(meses, 0, this.meses, 0, 12);
            repaint();
        }
    }
}
