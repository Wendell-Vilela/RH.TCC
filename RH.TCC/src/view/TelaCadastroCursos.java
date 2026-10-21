package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TelaCadastroCursos extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTextField id = new JTextField(7);
    private final JTextField nomeCurso = new JTextField(25);
    private final JTextField cargaHoraria = new JTextField(20);
    private final JTextField fornecedor = new JTextField(20);

    private final JComboBox<String> tipoCurso = new JComboBox<>(
        new String[]{
            "Selecione se o curso é interno ou externo",
            "Curso Interno",
            "Curso Externo"
        }
    );

    private final JComboBox<String> status = new JComboBox<>(
        new String[]{
            "Selecione o status",
            "Em andamento",
            "Concluída"
        }
    );

    private final JTextField dataInicio = new JTextField(20);
    private final JTextField dataConclusao = new JTextField(20);

    private final JComboBox<String> cursoAtribuicao = new JComboBox<>();
    private final JComboBox<String> setorAtribuicao = new JComboBox<>(
        new String[]{
            "Selecione o setor",
            "Tecnologia",
            "Recursos Humanos",
            "Financeiro",
            "Marketing"
        }
    );

    private final JComboBox<String> tipoAtribuicao = new JComboBox<>(
        new String[]{
            "Funcionário específico",
            "Todos os funcionários do setor"
        }
    );

    private final JComboBox<String> funcionarioAtribuicao = new JComboBox<>();

    private final JTable tabela = new JTable();

    private final JButton salvar = new JButton("Salvar");
    private final JButton atualizar = new JButton("Atualizar");
    private final JButton excluir = new JButton("Excluir");
    private final JButton limpar = new JButton("Limpar");

    private final JButton atribuirCurso = new JButton("Atribuir Curso");
    private final JButton limparAtribuicao = new JButton("Limpar Atribuição");

    public TelaCadastroCursos() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        cursoAtribuicao.setEditable(true);
        funcionarioAtribuicao.setEditable(true);

        id.setEditable(false);

        montar();
        configurarAtribuicao();
    }

    private void montar() {

        JPanel formulario = new JPanel(new GridBagLayout());

        formulario.setBorder(
            BorderFactory.createTitledBorder("Cadastro de Curso")
        );

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;

        componente(formulario, g, 0, "Código:", id);
        componente(formulario, g, 1, "Nome do curso:", nomeCurso);
        componente(formulario, g, 2, "Carga horária:", cargaHoraria);
        componente(formulario, g, 3, "Fornecedor:", fornecedor);
        componente(formulario, g, 4, "Tipo:", tipoCurso);
        componente(formulario, g, 5, "Status:", status);
        componente(formulario, g, 6, "Data de Início:", dataInicio);
        componente(formulario, g, 7, "Data de Conclusão:", dataConclusao);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        botoes.add(salvar);
        botoes.add(atualizar);
        botoes.add(excluir);
        botoes.add(limpar);

        JPanel cadastro = new JPanel(new BorderLayout(5, 5));

        cadastro.add(formulario, BorderLayout.CENTER);
        cadastro.add(botoes, BorderLayout.SOUTH);

        String[] colunas = {
            "Código",
            "Nome do Curso",
            "Carga Horária",
            "Fornecedor",
            "Tipo",
            "Status",
            "Data de Início",
            "Data de Conclusão"
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
        tabela.setRowHeight(25);

        JScrollPane scrollTabela = new JScrollPane(tabela);

        JPanel painelTabela = new JPanel(new BorderLayout());

        painelTabela.setBorder(
            BorderFactory.createTitledBorder("Cursos Cadastrados")
        );

        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        JPanel atribuicao = new JPanel(new GridBagLayout());

        atribuicao.setBorder(
            BorderFactory.createTitledBorder("Atribuir Curso a Funcionários")
        );

        GridBagConstraints a = new GridBagConstraints();
        a.insets = new Insets(5, 5, 5, 5);
        a.anchor = GridBagConstraints.WEST;

        componente(atribuicao, a, 0, "Curso:", cursoAtribuicao);
        componente(atribuicao, a, 1, "Setor:", setorAtribuicao);
        componente(atribuicao, a, 2, "Tipo de atribuição:", tipoAtribuicao);
        componente(atribuicao, a, 3, "Funcionário:", funcionarioAtribuicao);

        JPanel botoesAtribuicao = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        botoesAtribuicao.add(atribuirCurso);
        botoesAtribuicao.add(limparAtribuicao);

        JPanel painelAtribuicao = new JPanel(new BorderLayout());

        painelAtribuicao.add(atribuicao, BorderLayout.CENTER);
        painelAtribuicao.add(botoesAtribuicao, BorderLayout.SOUTH);

        JPanel parteSuperior = new JPanel(new BorderLayout(5, 10));

        parteSuperior.add(cadastro, BorderLayout.NORTH);
        parteSuperior.add(painelTabela, BorderLayout.CENTER);

        JPanel conteudo = new JPanel(new BorderLayout(5, 10));

        conteudo.add(parteSuperior, BorderLayout.CENTER);
        conteudo.add(painelAtribuicao, BorderLayout.SOUTH);

        add(conteudo, BorderLayout.CENTER);
    }

    private void configurarAtribuicao() {

        tipoAtribuicao.addActionListener(e -> {
            boolean atribuirParaTodos =
                tipoAtribuicao.getSelectedIndex() == 1;

            funcionarioAtribuicao.setEnabled(!atribuirParaTodos);

            if (atribuirParaTodos) {
                funcionarioAtribuicao.setSelectedItem(null);
            }
        });

        setorAtribuicao.addActionListener(e -> {
            if (tipoAtribuicao.getSelectedIndex() == 1) {
                funcionarioAtribuicao.setSelectedItem(null);
            }
        });

        funcionarioAtribuicao.setEnabled(true);
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

    public JTextField getNomeCurso() {
        return nomeCurso;
    }

    public JTextField getCargaHoraria() {
        return cargaHoraria;
    }

    public JTextField getFornecedor() {
        return fornecedor;
    }

    public JComboBox<String> getTipoCurso() {
        return tipoCurso;
    }

    public JComboBox<String> getStatus() {
        return status;
    }

    public JTextField getDataInicio() {
        return dataInicio;
    }

    public JTextField getDataConclusao() {
        return dataConclusao;
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

    public JComboBox<String> getCursoAtribuicao() {
        return cursoAtribuicao;
    }

    public JComboBox<String> getSetorAtribuicao() {
        return setorAtribuicao;
    }

    public JComboBox<String> getTipoAtribuicao() {
        return tipoAtribuicao;
    }

    public JComboBox<String> getFuncionarioAtribuicao() {
        return funcionarioAtribuicao;
    }

    public JButton getAtribuirCurso() {
        return atribuirCurso;
    }

    public JButton getLimparAtribuicao() {
        return limparAtribuicao;
    }
}

