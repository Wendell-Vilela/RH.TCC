package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TelaCadastroProximasAcoes extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTextField id = new JTextField(7);
    private final JTextField nomeAcao = new JTextField(25);
    private final JTextArea descricaoAcao = new JTextArea(4, 20);
    private final JTextField prazoAcao = new JTextField(20);

    private final JComboBox<String> funcionario = new JComboBox<>();

    private final JComboBox<String> status = new JComboBox<>(
        new String[]{
            "Selecione o status",
            "Em andamento",
            "Concluído",
            "Pendente"
        }
    );

    private final JTable tabela = new JTable();

    private final JButton salvar = new JButton("Salvar");
    private final JButton atualizar = new JButton("Atualizar");
    private final JButton excluir = new JButton("Excluir");
    private final JButton limpar = new JButton("Limpar");

    public TelaCadastroProximasAcoes() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        funcionario.setEditable(true);
        id.setEditable(false);
        descricaoAcao.setLineWrap(true);
        descricaoAcao.setWrapStyleWord(true);

        montar();
    }

    private void montar() {

        JPanel formulario = new JPanel(new GridBagLayout());

        formulario.setBorder(
            BorderFactory.createTitledBorder("Cadastro de Ação")
        );

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;

        componente(formulario, g, 0, "Código:", id);
        componente(formulario, g, 1, "Ação:", nomeAcao);
        componente(formulario, g, 2, "Descrição:", new JScrollPane(descricaoAcao));
        componente(formulario, g, 3, "Funcionário:", funcionario);
        componente(formulario, g, 4, "Prazo:", prazoAcao);
        componente(formulario, g, 5, "Status:", status);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        botoes.add(salvar);
        botoes.add(atualizar);
        botoes.add(excluir);
        botoes.add(limpar);

        String[] colunas = {
            "Código",
            "Funcionário",
            "Ação",
            "Descrição",
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
        tabela.setRowHeight(30);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFillsViewportHeight(true);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(300);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(120);

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setPreferredSize(
            new Dimension(800, tabela.getRowHeight() * 6 + 24)
        );

        JPanel painelTabela = new JPanel(new BorderLayout());

        painelTabela.setBorder(
            BorderFactory.createTitledBorder("Ações Cadastradas")
        );

        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        JPanel parteSuperior = new JPanel(new BorderLayout());

        parteSuperior.add(formulario, BorderLayout.CENTER);
        parteSuperior.add(botoes, BorderLayout.SOUTH);

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        parteSuperior.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelTabela.setAlignmentX(Component.LEFT_ALIGNMENT);

        parteSuperior.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                parteSuperior.getPreferredSize().height
            )
        );

        conteudo.add(parteSuperior);
        conteudo.add(Box.createVerticalStrut(10));
        conteudo.add(painelTabela);
        conteudo.add(Box.createVerticalStrut(15));

        JScrollPane scrollTela = new JScrollPane(
            conteudo,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollTela.setBorder(BorderFactory.createEmptyBorder());
        scrollTela.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollTela, BorderLayout.CENTER);
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

    public JTextField getId() {
        return id;
    }

    public JTextField getNomeAcao() {
        return nomeAcao;
    }

    public JTextArea getDescricaoAcao() {
        return descricaoAcao;
    }

    public JTextField getPrazoAcao() {
        return prazoAcao;
    }

    public JComboBox<String> getFuncionario() {
        return funcionario;
    }

    public JComboBox<String> getStatus() {
        return status;
    }

    public JTable getTabela() {
        return tabela;
    }

    public JButton getSalvar() {
        return salvar;
    }

    public JButton getAtualizar() {
        return atualizar;
    }

    public JButton getExcluir() {
        return excluir;
    }

    public JButton getLimpar() {
        return limpar;
    }
}
