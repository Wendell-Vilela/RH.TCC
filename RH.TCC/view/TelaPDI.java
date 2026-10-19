package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TelaPDI extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JComboBox<String> area = new JComboBox<>(
        new String[]{
            "Selecione a area",
            "Tecnologia",
            "Recursos Humanos",
            "Financeiro",
            "Marketing"
        }
    );

    private final JComboBox<String> funcionario = new JComboBox<>(
        new String[]{"Selecione o funcionario"}
    );

    private final JButton selecionar = new JButton("Selecionar");

    private final JProgressBar progresso = new JProgressBar(0, 100);

    private final JTable tabela = new JTable();

    public TelaPDI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        montar();
    }

    private void montar() {

        JPanel selecao = new JPanel(new GridBagLayout());

        selecao.setBorder(
            BorderFactory.createTitledBorder("Selecionar Funcionario")
        );

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);

        componente(selecao, g, 0, "Area:", area);
        componente(selecao, g, 1, "Funcionario:", funcionario);

        g.gridx = 2;
        g.gridy = 1;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;

        selecao.add(selecionar, g);

        JPanel painelProgresso = new JPanel(new BorderLayout());

        painelProgresso.setBorder(
            BorderFactory.createTitledBorder("Progresso do Plano")
        );

        progresso.setValue(0);
        progresso.setStringPainted(true);
        progresso.setForeground(Color.BLACK);

        painelProgresso.add(progresso, BorderLayout.CENTER);

        String[] colunas = {
            "Proxima Acao",
            "Descricao",
            "Prazo",
            "Status"
        };

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela.setModel(modelo);
        tabela.setRowHeight(35);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(400);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scrollTabela = new JScrollPane(tabela);

        JPanel painelTabela = new JPanel(new BorderLayout());

        painelTabela.setBorder(
            BorderFactory.createTitledBorder(
                "Acoes do Plano de Desenvolvimento"
            )
        );

        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        JPanel parteSuperior = new JPanel(new BorderLayout());

        parteSuperior.add(selecao, BorderLayout.NORTH);
        parteSuperior.add(painelProgresso, BorderLayout.CENTER);

        JPanel conteudo = new JPanel(new BorderLayout());

        conteudo.add(parteSuperior, BorderLayout.NORTH);
        conteudo.add(painelTabela, BorderLayout.CENTER);

        add(conteudo, BorderLayout.CENTER);
    }

    private void componente(
        JPanel painel,
        GridBagConstraints g,
        int linha,
        String texto,
        Component campo
    ) {
        g.gridx = 0;
        g.gridy = linha;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;

        painel.add(new JLabel(texto), g);

        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;

        painel.add(campo, g);
    }

    public JComboBox<String> getArea() {
        return area;
    }

    public JComboBox<String> getFuncionario() {
        return funcionario;
    }

    public JButton getSelecionar() {
        return selecionar;
    }

    public JProgressBar getProgresso() {
        return progresso;
    }

    public JTable getTabela() {
        return tabela;
    }
}
