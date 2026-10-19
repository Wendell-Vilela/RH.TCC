package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TelaCadastroCandidato extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTextField id = new JTextField(7);
    private final JTextField nome = new JTextField(60);
    private final JTextField cpf = new JTextField(11);
    private final JTextField telefone = new JTextField(11);
    private final JTextField email = new JTextField(60);
    private final JTextField vaga = new JTextField(50);

    private final JTable tabelaCandidatos = new JTable();
    
    private final JButton btnSalvar = new JButton("Salvar");
    private final JButton btnExcluir = new JButton("Excluir");
    private final JButton btnLimpar = new JButton("Limpar");
    private final JButton btnAtualizar = new JButton("Atualizar");

    public TelaCadastroCandidato() {

        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        montar();
    }

    private void montar() {

        JPanel formulario = new JPanel(new GridBagLayout());

        formulario.setBorder(BorderFactory.createTitledBorder("Cadastro do Candidato"));

        GridBagConstraints g = new GridBagConstraints();

        g.insets = new Insets(5, 5, 5, 5);

        componente(formulario, g, 0, "Código:", id);
        componente(formulario, g, 1, "Nome Completo:", nome);
        componente(formulario, g, 2, "CPF:", cpf);
        componente(formulario, g, 3, "Telefone:", telefone);
        componente(formulario, g, 4, "E-mail:", email);
        componente(formulario, g, 5, "Vaga Desejada:", vaga);

        id.setEditable(false);

        String[] colunas = {
            "Código",
            "Nome",
            "CPF",
            "Telefone",
            "E-mail",
            "Vaga"
        };

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);

        tabelaCandidatos.setModel(modelo);

        tabelaCandidatos.setRowHeight(40);

        tabelaCandidatos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaCandidatos.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaCandidatos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelaCandidatos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelaCandidatos.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabelaCandidatos.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scrollTabela = new JScrollPane(tabelaCandidatos);

        JPanel painelCandidatos = new JPanel(new BorderLayout());

        painelCandidatos.setBorder(BorderFactory.createTitledBorder("Candidatos Cadastrados"));

        painelCandidatos.add(scrollTabela, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        botoes.add(btnSalvar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);
        botoes.add(btnAtualizar);        
        
        JPanel conteudo = new JPanel(new BorderLayout());

        conteudo.add(formulario, BorderLayout.NORTH);
        conteudo.add(painelCandidatos, BorderLayout.CENTER);
        conteudo.add(botoes, BorderLayout.SOUTH);

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

public JTextField getNome() {
    return nome;
}

public JTextField getCpf() {
    return cpf;
}

public JTextField getTelefone() {
    return telefone;
}

public JTextField getEmail() {
    return email;
}

public JTextField getVaga() {
    return vaga;
}

public JTable getTabelaCandidatos() {
    return tabelaCandidatos;
}

public JButton getBtnSalvar() {
    return btnSalvar;
}

public JButton getBtnExcluir() {
    return btnExcluir;
}

public JButton getBtnLimpar() {
    return btnLimpar;
}

public JButton getBtnAtualizar() {
    return btnAtualizar;
}
}