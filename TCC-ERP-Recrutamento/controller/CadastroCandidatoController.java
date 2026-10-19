
package controller;

import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import view.TelaCadastroCandidato;

public class CadastroCandidatoController {

    private final TelaCadastroCandidato view;

    private static final Pattern PADRAO_NOME =
        Pattern.compile("^[\\p{L}\\p{M}]+(?:[ .'-][\\p{L}\\p{M}]+)*$");

    private static final Pattern PADRAO_EMAIL =
        Pattern.compile("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@"
            + "[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?"
            + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$");

    public CadastroCandidatoController(TelaCadastroCandidato view) {
        this.view = view;
        iniciarEventos();
    }

    private void iniciarEventos() {
        view.getBtnSalvar().addActionListener(e -> salvar());
        view.getBtnExcluir().addActionListener(e -> excluir());
        view.getBtnLimpar().addActionListener(e -> limpar());
        view.getBtnAtualizar().addActionListener(e -> atualizar());
    }

    private void salvar() {
        String nome = view.getNome().getText().trim();
        String cpf = view.getCpf().getText().replaceAll("\\D", "");
        String telefone = view.getTelefone().getText().replaceAll("\\D", "");
        String email = view.getEmail().getText().trim();
        String vaga = view.getVaga().getText().trim();

        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty()
                || email.isEmpty() || vaga.isEmpty()) {
            erro("Preencha todos os campos obrigatórios.");
            return;
        }

        if (!PADRAO_NOME.matcher(nome).matches()
                || nome.replaceAll("[^\\p{L}\\p{M}]", "").length() < 2
                || !nome.trim().matches(".*[\\p{L}\\p{M}].*")) {
            erro("Informe um nome válido, usando letras.");
            return;
        }

        if (!validarCPF(cpf)) {
            erro("CPF inválido. Confira os 11 dígitos informados.");
            return;
        }

        if (!validarTelefone(telefone)) {
            erro("Telefone inválido. Informe DDD e número com 10 ou 11 dígitos.");
            return;
        }

        if (email.length() > 254
                || !PADRAO_EMAIL.matcher(email).matches()
                || email.length() > 0 && email.contains("..")) {
            erro("Informe um endereço de e-mail válido.");
            return;
        }

        if (vaga.length() < 2) {
            erro("Informe uma vaga desejada válida.");
            return;
        }

        DefaultTableModel modelo = obterModelo();

        if (cpfJaCadastrado(modelo, cpf)) {
            erro("Já existe um candidato cadastrado com esse CPF.");
            return;
        }

        int codigo = proximoCodigo(modelo);

        modelo.addRow(new Object[] {
            codigo,
            nome,
            cpf,
            telefone,
            email,
            vaga
        });

        view.getId().setText(String.valueOf(codigo));

        JOptionPane.showMessageDialog(
            view,
            "Candidato cadastrado com sucesso!",
            "Cadastro",
            JOptionPane.INFORMATION_MESSAGE
        );

        limpar();
    }
    

private void atualizar() {
    JTable tabela = view.getTabelaCandidatos();
    int linhaSelecionada = tabela.getSelectedRow();

    if (linhaSelecionada == -1) {
        erro("Selecione um candidato na tabela para atualizar.");
        return;
    }

    String nome = view.getNome().getText().trim();
    String cpf = view.getCpf().getText().replaceAll("\\D", "");
    String telefone = view.getTelefone().getText().replaceAll("\\D", "");
    String email = view.getEmail().getText().trim();
    String vaga = view.getVaga().getText().trim();

    if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty()
            || email.isEmpty() || vaga.isEmpty()) {
        erro("Preencha todos os campos obrigatórios.");
        return;
    }

    if (!PADRAO_NOME.matcher(nome).matches()) {
        erro("Informe um nome válido.");
        view.getNome().requestFocusInWindow();
        return;
    }

    if (!validarCPF(cpf)) {
        erro("Informe um CPF válido.");
        view.getCpf().requestFocusInWindow();
        return;
    }

    if (!validarTelefone(telefone)) {
        erro("Informe um telefone válido.");
        view.getTelefone().requestFocusInWindow();
        return;
    }

    if (!PADRAO_EMAIL.matcher(email).matches()) {
        erro("Informe um e-mail válido.");
        view.getEmail().requestFocusInWindow();
        return;
    }

    DefaultTableModel modelo = obterModelo();
    int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);

    for (int i = 0; i < modelo.getRowCount(); i++) {
        if (i == linhaModelo) {
            continue;
        }

        String cpfExistente = String.valueOf(modelo.getValueAt(i, 2))
                .replaceAll("\\D", "");

        if (cpfExistente.equals(cpf)) {
            erro("Já existe outro candidato cadastrado com esse CPF.");
            view.getCpf().requestFocusInWindow();
            return;
        }
    }

    modelo.setValueAt(nome, linhaModelo, 1);
    modelo.setValueAt(cpf, linhaModelo, 2);
    modelo.setValueAt(telefone, linhaModelo, 3);
    modelo.setValueAt(email, linhaModelo, 4);
    modelo.setValueAt(vaga, linhaModelo, 5);

    JOptionPane.showMessageDialog(
        view,
        "Candidato atualizado com sucesso!",
        "Atualização",
        JOptionPane.INFORMATION_MESSAGE
    );

    limpar();
}


    private void excluir() {
        JTable tabela = view.getTabelaCandidatos();
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            erro("Selecione um candidato na tabela para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            view,
            "Deseja realmente excluir o candidato selecionado?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);
            obterModelo().removeRow(linhaModelo);
            limpar();

            JOptionPane.showMessageDialog(
                view,
                "Candidato excluído com sucesso.",
                "Exclusão",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void limpar() {
        view.getId().setText("");
        view.getNome().setText("");
        view.getCpf().setText("");
        view.getTelefone().setText("");
        view.getEmail().setText("");
        view.getVaga().setText("");
        view.getNome().requestFocusInWindow();
        view.getTabelaCandidatos().clearSelection();
    }

    private boolean validarTelefone(String telefone) {
        if (!telefone.matches("\\d{10,11}")) {
            return false;
        }

        int ddd = Integer.parseInt(telefone.substring(0, 2));

        if (ddd < 11 || ddd > 99) {
            return false;
        }

        String numero = telefone.substring(2);

        if (numero.length() == 9) {
            return numero.charAt(0) == '9';
        }

        return numero.charAt(0) >= '2' && numero.charAt(0) <= '5';
    }

    private boolean validarCPF(String cpf) {
        if (!cpf.matches("\\d{11}")) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }

        int primeiroDigito = (soma * 10) % 11;

        if (primeiroDigito == 10) {
            primeiroDigito = 0;
        }

        if (primeiroDigito != cpf.charAt(9) - '0') {
            return false;
        }

        soma = 0;

        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }

        int segundoDigito = (soma * 10) % 11;

        if (segundoDigito == 10) {
            segundoDigito = 0;
        }

        return segundoDigito == cpf.charAt(10) - '0';
    }

    private boolean cpfJaCadastrado(DefaultTableModel modelo, String cpf) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String cpfTabela = String.valueOf(modelo.getValueAt(i, 2))
                .replaceAll("\\D", "");

            if (cpfTabela.equals(cpf)) {
                return true;
            }
        }

        return false;
    }

    private int proximoCodigo(DefaultTableModel modelo) {
        int maiorCodigo = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object valor = modelo.getValueAt(i, 0);

            try {
                maiorCodigo = Math.max(
                    maiorCodigo,
                    Integer.parseInt(String.valueOf(valor))
                );
            } catch (NumberFormatException e) {
            }
        }

        return maiorCodigo + 1;
    }

    private DefaultTableModel obterModelo() {
        return (DefaultTableModel) view.getTabelaCandidatos().getModel();
    }

    private void erro(String mensagem) {
        JOptionPane.showMessageDialog(
            view,
            mensagem,
            "Erro de validação",
            JOptionPane.WARNING_MESSAGE
        );
    }
}
