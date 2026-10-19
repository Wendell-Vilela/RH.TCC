package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TelaCadastroFeedback extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTextField id = new JTextField(7);
    private final JTextField funcionario = new JTextField(20);

    private final JComboBox<String> competenciaFeedback = new JComboBox<>(
        new String[]{
            "Escolha a competência avaliada",
            "Comunicação",
            "Entrega",
            "Qualidade",
            "Liderança"
        }
    );

    private final JTextField nomeAvaliador = new JTextField(20);

    private final JComboBox<String> tipoAvaliador = new JComboBox<>(
        new String[]{
            "Selecione o tipo da avaliação",
            "Auto avaliação",
            "Avaliação do Gestor",
            "Avaliação de Pares"
        }
    );

    private final JTextField notaAvaliacao = new JTextField(20);

    private final JTable tabela = new JTable();

    private final JButton salvar = new JButton("Salvar");
    private final JButton atualizar = new JButton("Atualizar");
    private final JButton excluir = new JButton("Excluir");
    private final JButton limpar = new JButton("Limpar");

    public TelaCadastroFeedback() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        montar();
    }

    private void montar() {

        JPanel formulario = new JPanel(new GridBagLayout());

        formulario.setBorder(
            BorderFactory.createTitledBorder("Cadastro de Feedback")
        );

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);

        componente(formulario, g, 0, "Código:", id);
        componente(formulario, g, 1, "Funcionário:", funcionario);
        componente(formulario, g, 2, "Competência:", competenciaFeedback);
        componente(formulario, g, 3, "Nome do avaliador:", nomeAvaliador);
        componente(formulario, g, 4, "Tipo da avaliação:", tipoAvaliador);
        componente(formulario, g, 5, "Nota:", notaAvaliacao);

        id.setEditable(false);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        botoes.add(salvar);
        botoes.add(atualizar);
        botoes.add(excluir);
        botoes.add(limpar);

        String[] colunas = {
            "Código",
            "Funcionário",
            "Competência avaliada",
            "Avaliador",
            "Tipo da avaliação",
            "Nota"
        };

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela.setModel(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabela = new JScrollPane(tabela);

        JPanel painelTabela = new JPanel(new BorderLayout());

        painelTabela.setBorder(
            BorderFactory.createTitledBorder("Feedbacks Cadastrados")
        );

        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        JPanel parteSuperior = new JPanel(new BorderLayout());

        parteSuperior.add(formulario, BorderLayout.NORTH);
        parteSuperior.add(botoes, BorderLayout.SOUTH);

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

    public JTextField getId() {
        return id;
    }

    public JTextField getFuncionario() {
        return funcionario;
    }

    public JComboBox<String> getCompetenciaFeedback() {
        return competenciaFeedback;
    }

    public JTextField getAvaliador() {
        return nomeAvaliador;
    }

    public JComboBox<String> getTipoAvaliador() {
        return tipoAvaliador;
    }

    public JTextField getNotaAvaliacao() {
        return notaAvaliacao;
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
