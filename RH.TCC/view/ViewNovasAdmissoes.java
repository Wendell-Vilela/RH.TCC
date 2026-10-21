package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ViewNovasAdmissoes extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel lblTotalAdmissoes;
    private JTable tabelaAdmissoes;
    private DefaultTableModel modelTabela;
    private JPanel painelGraficoBarras;

    public ViewNovasAdmissoes() {
        setLayout(new BorderLayout(15, 15));

        TitledBorder borda = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "NOVAS ADMISSÕES E CONTRATAÇÕES",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14)
        );

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                borda
        ));


        JPanel painelConteudo = new JPanel(new GridLayout(1, 2, 15, 0));


        painelConteudo.add(criarPainelGraficoContainer());


        painelConteudo.add(criarPainelTabela());

        add(painelConteudo, BorderLayout.CENTER);


        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelGraficoContainer() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Distribuição de Admissões por Setor",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12)
        ));


        painelGraficoBarras = new JPanel(new GridLayout(1, 0, 15, 0));
        
        container.add(painelGraficoBarras, BorderLayout.CENTER);
        return container;
    }

    private JPanel criarPainelTabela() {
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Relação Detalhada de Colaboradores Admitidos",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12)
        ));


        String[] colunas = {"ID", "Nome Colaborador", "CPF", "Data Admissão", "Departamento", "Cargo / Função"};

        modelTabela = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela somente leitura
            }
        };

        tabelaAdmissoes = new JTable(modelTabela);
        tabelaAdmissoes.setFillsViewportHeight(true);
        tabelaAdmissoes.setRowHeight(24);
        tabelaAdmissoes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));

        // Renderização para centralizar datas, IDs e documentos
        DefaultTableCellRenderer rendererCentral = new DefaultTableCellRenderer();
        rendererCentral.setHorizontalAlignment(SwingConstants.CENTER);
        
        tabelaAdmissoes.getColumnModel().getColumn(0).setCellRenderer(rendererCentral); // ID
        tabelaAdmissoes.getColumnModel().getColumn(2).setCellRenderer(rendererCentral); // CPF
        tabelaAdmissoes.getColumnModel().getColumn(3).setCellRenderer(rendererCentral); // Data

        JScrollPane scroll = new JScrollPane(tabelaAdmissoes);
        scroll.setBorder(BorderFactory.createEtchedBorder());

        painelTabela.add(scroll, BorderLayout.CENTER);

        return painelTabela;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        lblTotalAdmissoes = new JLabel("Total acumulado no período: 0 novos colaboradores.", SwingConstants.CENTER);
        lblTotalAdmissoes.setFont(new Font("SansSerif", Font.BOLD, 12));

        rodape.add(lblTotalAdmissoes, BorderLayout.CENTER);

        return rodape;
    }



    public JLabel getLabelTotalAdmissoes() {
        return lblTotalAdmissoes;
    }

    public DefaultTableModel getModelTabela() {
        return modelTabela;
    }

    public JTable getTabelaAdmissoes() {
        return tabelaAdmissoes;
    }

    public void limparGraficoBarras() {
        painelGraficoBarras.removeAll();
    }
    public void adicionarBarraGrafico(String nomeSetor, int valorAdmissoes, int percentualPreenchimento) {
        JPanel coluna = new JPanel(new BorderLayout(0, 5));

        JLabel rotulo = new JLabel("<html><center>" + nomeSetor + "<br><b>" + valorAdmissoes + "</b></center></html>");
        rotulo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        rotulo.setHorizontalAlignment(SwingConstants.CENTER);

        JProgressBar barra = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
        barra.setValue(percentualPreenchimento);
        barra.setStringPainted(false);
        barra.setBorder(BorderFactory.createEtchedBorder());

        coluna.add(rotulo, BorderLayout.NORTH);
        coluna.add(barra, BorderLayout.CENTER);

        painelGraficoBarras.add(coluna);
    }

    public void revalidarEAtualizarGrafico() {
        painelGraficoBarras.revalidate();
        painelGraficoBarras.repaint();
    }

    public void limparTabela() {
        modelTabela.setRowCount(0);
    }
    public void adicionarLinhaTabela(Object[] dadosLinha) {
        modelTabela.addRow(dadosLinha);
    }
}
