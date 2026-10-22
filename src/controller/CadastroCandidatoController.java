
package controller;

import dao.CandidatoDAO;
import model.CadastroCandidato;
import view.TelaCadastroCandidato;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.regex.Pattern;

public class CadastroCandidatoController {

    private final TelaCadastroCandidato view;
    private final CandidatoDAO dao;

    private static final Pattern PADRAO_NOME = Pattern.compile(
            "^[\\p{L}\\p{M}]+(?:[ .'-][\\p{L}\\p{M}]+)*$"
    );

    private static final Pattern PADRAO_EMAIL = Pattern.compile(
            "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9-]+"
            + "(?:\\.[A-Za-z0-9-]+)+$"
    );

    public CadastroCandidatoController(TelaCadastroCandidato view) {
        this.view = view;
        this.dao = new CandidatoDAO();

        configurarEventos();
        listar();
    }

    private void configurarEventos() {
        view.getBtnSalvar().addActionListener(e -> salvar());
        view.getBtnAtualizar().addActionListener(e -> atualizar());
        view.getBtnExcluir().addActionListener(e -> excluir());
        view.getBtnLimpar().addActionListener(e -> limpar());

        view.getTabelaCandidatos().getSelectionModel()
                .addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        preencherFormulario();
                    }
                });
    }

    private void salvar() {
        CadastroCandidato candidato = obterDadosFormulario();

        if (candidato == null) {
            return;
        }

        try {
            if (dao.cpfExiste(candidato.getCpf(), 0)) {
                JOptionPane.showMessageDialog(
                        view,
                        "Já existe um candidato cadastrado com esse CPF."
                );
                return;
            }

            if (dao.inserir(candidato)) {
                JOptionPane.showMessageDialog(
                        view,
                        "Candidato cadastrado com sucesso!"
                );

                listar();
                limpar();
            } else {
                JOptionPane.showMessageDialog(
                        view,
                        "Não foi possível cadastrar o candidato."
                );
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    view,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void atualizar() {
        String textoId = view.getId().getText().trim();

        if (textoId.isEmpty()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Selecione um candidato na tabela para atualizar."
            );
            return;
        }

        CadastroCandidato candidato = obterDadosFormulario();

        if (candidato == null) {
            return;
        }

        candidato.setId(Integer.parseInt(textoId));

        try {
            if (dao.cpfExiste(candidato.getCpf(), candidato.getId())) {
                JOptionPane.showMessageDialog(
                        view,
                        "Outro candidato já está cadastrado com esse CPF."
                );
                return;
            }

            if (dao.atualizar(candidato)) {
                JOptionPane.showMessageDialog(
                        view,
                        "Candidato atualizado com sucesso!"
                );

                listar();
                limpar();
            } else {
                JOptionPane.showMessageDialog(
                        view,
                        "Não foi possível atualizar o candidato."
                );
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    view,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluir() {
        String textoId = view.getId().getText().trim();

        if (textoId.isEmpty()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Selecione um candidato na tabela para excluir."
            );
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                view,
                "Deseja realmente excluir este candidato?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        int id = Integer.parseInt(textoId);

        if (dao.excluir(id)) {
            JOptionPane.showMessageDialog(
                    view,
                    "Candidato excluído com sucesso!"
            );

            listar();
            limpar();
        } else {
            JOptionPane.showMessageDialog(
                    view,
                    "Não foi possível excluir o candidato."
            );
        }
    }

    private CadastroCandidato obterDadosFormulario() {
        String nome = view.getNome().getText().trim();
        String cpf = view.getCpf().getText().replaceAll("\\D", "");
        String telefone = view.getTelefone().getText().replaceAll("\\D", "");
        String email = view.getEmail().getText().trim();
        String vaga = view.getVaga().getText().trim();

        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty()
                || email.isEmpty() || vaga.isEmpty()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Preencha todos os campos."
            );
            return null;
        }

        if (!PADRAO_NOME.matcher(nome).matches()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Informe um nome válido."
            );
            return null;
        }

        if (!validarCPF(cpf)) {
            JOptionPane.showMessageDialog(
                    view,
                    "Informe um CPF válido."
            );
            return null;
        }

        if (!validarTelefone(telefone)) {
            JOptionPane.showMessageDialog(
                    view,
                    "Informe um telefone válido com DDD."
            );
            return null;
        }

        if (!PADRAO_EMAIL.matcher(email).matches()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Informe um e-mail válido."
            );
            return null;
        }

        CadastroCandidato candidato = new CadastroCandidato();
        candidato.setNome(nome);
        candidato.setCpf(cpf);
        candidato.setTelefone(telefone);
        candidato.setEmail(email);
        candidato.setVaga(vaga);

        return candidato;
    }

    private boolean validarCPF(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }

        int resto = (soma * 10) % 11;
        int digito1 = resto == 10 ? 0 : resto;

        if (digito1 != cpf.charAt(9) - '0') {
            return false;
        }

        soma = 0;

        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }

        resto = (soma * 10) % 11;
        int digito2 = resto == 10 ? 0 : resto;

        return digito2 == cpf.charAt(10) - '0';
    }

    private boolean validarTelefone(String telefone) {
        if (telefone == null
                || !telefone.matches("\\d{10,11}")) {
            return false;
        }

        int ddd = Integer.parseInt(telefone.substring(0, 2));

        if (ddd < 11 || ddd > 99) {
            return false;
        }

        if (telefone.length() == 11) {
            return telefone.charAt(2) == '9';
        }

        char primeiroDigito = telefone.charAt(2);

        return primeiroDigito >= '2' && primeiroDigito <= '5';
    }

    private void listar() {
        List<CadastroCandidato> candidatos = dao.listarCandidatos();

        DefaultTableModel modelo =
                (DefaultTableModel) view.getTabelaCandidatos().getModel();

        modelo.setRowCount(0);

        for (CadastroCandidato candidato : candidatos) {
            modelo.addRow(new Object[]{
                candidato.getId(),
                candidato.getNome(),
                candidato.getCpf(),
                candidato.getTelefone(),
                candidato.getEmail(),
                candidato.getVaga()
            });
        }
    }

    private void preencherFormulario() {
        int linha = view.getTabelaCandidatos().getSelectedRow();

        if (linha == -1) {
            return;
        }

        linha = view.getTabelaCandidatos().convertRowIndexToModel(linha);

        DefaultTableModel modelo =
                (DefaultTableModel) view.getTabelaCandidatos().getModel();

        view.getId().setText(String.valueOf(modelo.getValueAt(linha, 0)));
        view.getNome().setText(String.valueOf(modelo.getValueAt(linha, 1)));
        view.getCpf().setText(String.valueOf(modelo.getValueAt(linha, 2)));
        view.getTelefone().setText(String.valueOf(modelo.getValueAt(linha, 3)));
        view.getEmail().setText(String.valueOf(modelo.getValueAt(linha, 4)));
        view.getVaga().setText(String.valueOf(modelo.getValueAt(linha, 5)));
    }

    private void limpar() {
        view.getId().setText("");
        view.getNome().setText("");
        view.getCpf().setText("");
        view.getTelefone().setText("");
        view.getEmail().setText("");
        view.getVaga().setText("");
        view.getTabelaCandidatos().clearSelection();
        view.getNome().requestFocus();
    }
}
