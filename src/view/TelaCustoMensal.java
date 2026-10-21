package view;

import controller.CustoMensalController;
import model.CustoMensal;

import java.awt.BorderLayout;

import java.awt.Color;

import java.awt.Dimension;

import java.awt.Font;

import java.awt.Graphics;

import java.awt.Graphics2D;

import java.awt.GridLayout;

import java.awt.RenderingHints;

import java.math.BigDecimal;

import javax.swing.BorderFactory;

import javax.swing.JLabel;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JSplitPane;

import javax.swing.JTable;

import javax.swing.SwingUtilities;

import javax.swing.table.DefaultTableModel;

import javax.swing.border.TitledBorder;

public class TelaCustoMensal extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel investimentoTotallbl;

    private JLabel investimentoTotaltxt;

    private JLabel salarioTotallbl;

    private JLabel salarioTotaltxt;

    private JLabel encargoTotallbl;

    private JLabel encargoTotaltxt;

    private JLabel beneficioTotallbl;

    private JLabel beneficioTotaltxt;

    private JTable tabela;

    private DefaultTableModel tableModel;

    private GraficoPizza graficoPizza;
    private CustoMensalController controller;

    public TelaCustoMensal() {

        setLayout(new BorderLayout());

        setBackground(Color.WHITE);

        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        montar();
        controller = new CustoMensalController(this);
        controller.carregarDados();

    }

    private void montar() {

        JPanel painelEsquerdo = new JPanel(new BorderLayout(10, 10));

        painelEsquerdo.setBackground(Color.WHITE);

        JPanel painelValores = criarPainelValores();

        JPanel painelGrafico = criarPainelGrafico();

        painelEsquerdo.add(painelValores, BorderLayout.NORTH);

        painelEsquerdo.add(painelGrafico, BorderLayout.CENTER);

        String[] colunas = {

            "ID",

            "Nome",

            "Salário",

            "Encargos",

            "Benefícios"

        };

        tableModel = new DefaultTableModel(colunas, 0) {

            private static final long serialVersionUID = 1L;

            @Override

            public boolean isCellEditable(int row, int column) {

                return false;

            }

        };

        tabela = new JTable(tableModel);

        tabela.setRowHeight(25);

        tabela.setFont(new Font("SansSerif", Font.PLAIN, 11));

        tabela.setFillsViewportHeight(true);

        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabela.getTableHeader().setFont(

            new Font("SansSerif", Font.BOLD, 11)

        );

        tabela.getTableHeader().setReorderingAllowed(false);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(35);

        tabela.getColumnModel().getColumn(1).setPreferredWidth(90);

        tabela.getColumnModel().getColumn(2).setPreferredWidth(65);

        tabela.getColumnModel().getColumn(3).setPreferredWidth(65);

        tabela.getColumnModel().getColumn(4).setPreferredWidth(70);

        JScrollPane scrollPane = new JScrollPane(tabela);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel painelTabela = new JPanel(new BorderLayout(5, 5));

        painelTabela.setBackground(Color.WHITE);

        painelTabela.setBorder(BorderFactory.createTitledBorder(

            BorderFactory.createEtchedBorder(),

            "Detalhamento dos Custos",

            TitledBorder.LEFT,

            TitledBorder.TOP,

            new Font("SansSerif", Font.BOLD, 12)

        ));

        painelTabela.add(scrollPane, BorderLayout.CENTER);

        JSplitPane divisao = new JSplitPane(

            JSplitPane.HORIZONTAL_SPLIT,

            painelEsquerdo,

            painelTabela

        );

        divisao.setResizeWeight(0.5);

        divisao.setDividerSize(6);

        divisao.setContinuousLayout(true);

        divisao.setBorder(null);

        add(divisao, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {

            divisao.setDividerLocation(0.5);

        });

    }

    private JPanel criarPainelValores() {

        JPanel painel = new JPanel(new GridLayout(4, 2, 8, 8));

        painel.setBackground(Color.WHITE);

        painel.setBorder(BorderFactory.createTitledBorder(

            BorderFactory.createEtchedBorder(),

            "Resumo dos Custos",

            TitledBorder.LEFT,

            TitledBorder.TOP,

            new Font("SansSerif", Font.BOLD, 12)

        ));

        investimentoTotallbl = criarLabel("Investimento total:");

        investimentoTotaltxt = criarValor("R$ 0,00");

        salarioTotallbl = criarLabel("Salários:");

        salarioTotaltxt = criarValor("R$ 0,00");

        encargoTotallbl = criarLabel("Encargos:");

        encargoTotaltxt = criarValor("R$ 0,00");

        beneficioTotallbl = criarLabel("Benefícios:");

        beneficioTotaltxt = criarValor("R$ 0,00");

        painel.add(investimentoTotallbl);

        painel.add(investimentoTotaltxt);

        painel.add(salarioTotallbl);

        painel.add(salarioTotaltxt);

        painel.add(encargoTotallbl);

        painel.add(encargoTotaltxt);

        painel.add(beneficioTotallbl);

        painel.add(beneficioTotaltxt);

        painel.setBorder(BorderFactory.createCompoundBorder(

            painel.getBorder(),

            BorderFactory.createEmptyBorder(8, 8, 8, 8)

        ));

        return painel;

    }

    private JLabel criarLabel(String texto) {

        JLabel label = new JLabel(texto);

        label.setFont(new Font("SansSerif", Font.PLAIN, 12));

        label.setForeground(Color.BLACK);

        return label;

    }

    private JLabel criarValor(String texto) {

        JLabel label = new JLabel(texto);

        label.setFont(new Font("SansSerif", Font.BOLD, 13));

        label.setForeground(Color.BLACK);

        return label;

    }

    private JPanel criarPainelGrafico() {

        JPanel painel = new JPanel(new BorderLayout(5, 5));

        painel.setBackground(Color.WHITE);

        painel.setBorder(BorderFactory.createTitledBorder(

            BorderFactory.createEtchedBorder(),

            "Distribuição dos Custos",

            TitledBorder.LEFT,

            TitledBorder.TOP,

            new Font("SansSerif", Font.BOLD, 12)

        ));

        graficoPizza = new GraficoPizza();

        JPanel legenda = new JPanel(new GridLayout(3, 1, 3, 3));

        legenda.setBackground(Color.WHITE);

        legenda.setBorder(

            BorderFactory.createEmptyBorder(5, 15, 8, 5)

        );

        legenda.add(criarItemLegenda(new Color(65, 105, 225), "Salários"));

        legenda.add(criarItemLegenda(new Color(220, 80, 80), "Encargos"));

        legenda.add(criarItemLegenda(new Color(60, 160, 100), "Benefícios"));

        painel.add(graficoPizza, BorderLayout.CENTER);

        painel.add(legenda, BorderLayout.SOUTH);

        return painel;

    }

    private JPanel criarItemLegenda(Color cor, String texto) {

        JPanel item = new JPanel(new BorderLayout(8, 0));

        item.setBackground(Color.WHITE);

        JPanel marcador = new JPanel();

        marcador.setBackground(cor);

        marcador.setPreferredSize(new Dimension(14, 14));

        JLabel label = new JLabel(texto);

        label.setFont(new Font("SansSerif", Font.PLAIN, 11));

        item.add(marcador, BorderLayout.WEST);

        item.add(label, BorderLayout.CENTER);

        return item;

    }

    public void atualizarDados(CustoMensal custo) {

        if (custo == null) {

            return;

        }

        BigDecimal investimento = valorOuZero(custo.getInvestimentoTotal());

        BigDecimal salarios = valorOuZero(custo.getSalarioTotal());

        BigDecimal encargos = valorOuZero(custo.getEncargoTotal());

        BigDecimal beneficios = valorOuZero(custo.getBeneficioTotal());

        investimentoTotaltxt.setText(formatarValor(investimento));

        salarioTotaltxt.setText(formatarValor(salarios));

        encargoTotaltxt.setText(formatarValor(encargos));

        beneficioTotaltxt.setText(formatarValor(beneficios));

        graficoPizza.setDados(

            salarios.doubleValue(),

            encargos.doubleValue(),

            beneficios.doubleValue()

        );

        limparTabela();

        if (custo.getItens() != null) {

            for (CustoMensal.Item item : custo.getItens()) {

                Object[] linha = {

                    item.getId(),

                    item.getNome(),

                    item.getSalario(),

                    item.getEncargos(),

                    item.getBeneficios()

                };

                adicionarLinhaTabela(linha);

            }

        }

        revalidate();

        repaint();

    }

    private BigDecimal valorOuZero(BigDecimal valor) {

        return valor == null ? BigDecimal.ZERO : valor;

    }

    private String formatarValor(BigDecimal valor) {

        return String.format(

            java.util.Locale.forLanguageTag("pt-BR"),

            "R$ %,.2f",

            valor

        );

    }

    public void adicionarLinhaTabela(Object[] dados) {

        tableModel.addRow(dados);

    }

    public void limparTabela() {

        tableModel.setRowCount(0);

    }

    public JTable getTabela() {

        return tabela;

    }

    public DefaultTableModel getTableModel() {

        return tableModel;

    }

    public GraficoPizza getGraficoPizza() {

        return graficoPizza;

    }

    public class GraficoPizza extends JPanel {

        private static final long serialVersionUID = 1L;

        private double salarios;

        private double encargos;

        private double beneficios;

        private final Color corSalarios = new Color(65, 105, 225);

        private final Color corEncargos = new Color(220, 80, 80);

        private final Color corBeneficios = new Color(60, 160, 100);

        public GraficoPizza() {

            setBackground(Color.WHITE);

            setPreferredSize(new Dimension(200, 180));

            setMinimumSize(new Dimension(100, 100));

        }

        public void setDados(double salarios, double encargos, double beneficios) {

            this.salarios = Math.max(0, salarios);

            this.encargos = Math.max(0, encargos);

            this.beneficios = Math.max(0, beneficios);

            repaint();

        }

        @Override

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(

                RenderingHints.KEY_ANTIALIASING,

                RenderingHints.VALUE_ANTIALIAS_ON

            );

            double total = salarios + encargos + beneficios;

            int tamanho = Math.min(

                getWidth() - 30,

                getHeight() - 30

            );

            if (tamanho <= 0) {

                g2d.dispose();

                return;

            }

            int x = (getWidth() - tamanho) / 2;

            int y = (getHeight() - tamanho) / 2;

            if (total <= 0) {

                g2d.setColor(Color.LIGHT_GRAY);

                g2d.fillOval(x, y, tamanho, tamanho);

                g2d.setColor(Color.DARK_GRAY);

                g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));

                String texto = "Sem dados";

                int largura = g2d.getFontMetrics().stringWidth(texto);

                g2d.drawString(

                    texto,

                    (getWidth() - largura) / 2,

                    getHeight() / 2

                );

                g2d.dispose();

                return;

            }

            int anguloSalarios = (int) Math.round((salarios / total) * 360);

            int anguloEncargos = (int) Math.round((encargos / total) * 360);

            int anguloBeneficios = 360 - anguloSalarios - anguloEncargos;

            int inicio = 90;

            g2d.setColor(corSalarios);

            g2d.fillArc(x, y, tamanho, tamanho, inicio, anguloSalarios);

            inicio += anguloSalarios;

            g2d.setColor(corEncargos);

            g2d.fillArc(x, y, tamanho, tamanho, inicio, anguloEncargos);

            inicio += anguloEncargos;

            g2d.setColor(corBeneficios);

            g2d.fillArc(x, y, tamanho, tamanho, inicio, anguloBeneficios);

            g2d.setColor(Color.WHITE);

            g2d.drawOval(x, y, tamanho, tamanho);

            g2d.dispose();

        }

    }

}