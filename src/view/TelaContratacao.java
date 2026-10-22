package view;

import java.awt.*;
import javax.swing.*;

public class TelaContratacao extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextField id = new JTextField(7);
    private final JTextField candidato = new JTextField(60);
    private final JTextField cargo = new JTextField(30);
    private final JTextField dataAdmissao = new JTextField("**/**/**", 8);
    private final JTextField salario = new JTextField(30);
    

    private final JComboBox<String> tipoContrato = new JComboBox<>(
            new String[]{"Selecione o contrato","CLT", "Estagio", "Jovem Aprendiz", "Temporario"}
        );
    
    private final JButton btnAtualizar = new JButton("Atualizar");
    private final JButton btnSalvar = new JButton("Salvar");
    private final JButton btnExcluir = new JButton("Excluir");
    private final JButton btnLimpar = new JButton("Limpar");

    public TelaContratacao() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        montar();
    }

    private void montar() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(
            BorderFactory.createTitledBorder("Contratação")
        );

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);

        componente(formulario, g, 0, "Codigo:", id);
        componente(formulario, g, 1, "Candidato:", candidato);
        componente(formulario, g, 2, "Cargo:", cargo);
        componente(formulario, g, 3, "Data de admissão:", dataAdmissao);
        componente(formulario, g, 4, "Salário:", salario);
        componente(formulario, g, 5, "Tipo de Contrato:", tipoContrato);

        id.setEditable(false);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);

        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.add(formulario, BorderLayout.NORTH);
        conteudo.add(botoes, BorderLayout.SOUTH);

        add(conteudo, BorderLayout.NORTH);
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

    public JTextField getCandidato() {
        return candidato;
    }

    public JTextField getCargo() {
        return cargo;
    }

    public JTextField getDataAdmissao() {
        return dataAdmissao;
    }

    public JTextField getSalario() {
        return salario;
    }

    public JComboBox<String> getTipoContrato() {
        return tipoContrato;
    }

    public JButton getBtnAtualizar() {
        return btnAtualizar;
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
}