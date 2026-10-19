package controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import view.TelaContratacao;

public class ContratacaoController {

    private final TelaContratacao view;
    private final List<Object[]> contratacoes = new ArrayList<>();

    private final DateTimeFormatter formatoData =
        DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private int proximoId = 1;

    public ContratacaoController(TelaContratacao view) {
        this.view = view;
        iniciarEventos();
    }

    private void iniciarEventos() {
        view.getBtnSalvar().addActionListener(e -> salvar());
        view.getBtnAtualizar().addActionListener(e -> atualizar());
        view.getBtnExcluir().addActionListener(e -> excluir());
        view.getBtnLimpar().addActionListener(e -> limpar());
    }

    private void salvar() {
        if (!validarCampos()) {
            return;
        }

        String candidato = view.getCandidato().getText().trim();
        String cargo = view.getCargo().getText().trim();
        String data = LocalDate.parse(
            view.getDataAdmissao().getText().trim(),
            formatoData
        ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        BigDecimal salario = new BigDecimal(
            view.getSalario().getText().trim().replace(',', '.')
        );

        String contrato =
            (String) view.getTipoContrato().getSelectedItem();

        int id = proximoId++;

        contratacoes.add(new Object[]{
            id,
            candidato,
            cargo,
            data,
            salario.setScale(2).toPlainString(),
            contrato
        });

        JOptionPane.showMessageDialog(
            view,
            "Contratação salva com sucesso! Código: " + id,
            "Cadastro de Contratação",
            JOptionPane.INFORMATION_MESSAGE
        );

        limpar();
    }

    private void atualizar() {
        String idTexto = view.getId().getText().trim();

        if (idTexto.isEmpty()) {
            erro("Informe o código da contratação que deseja atualizar.");
            return;
        }

        int id;

        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            erro("Código de contratação inválido.");
            return;
        }

        Object[] contratacao = buscarPorId(id);

        if (contratacao == null) {
            erro("Não foi encontrada uma contratação com esse código.");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        String candidato = view.getCandidato().getText().trim();
        String cargo = view.getCargo().getText().trim();
        String data = LocalDate.parse(
            view.getDataAdmissao().getText().trim(),
            formatoData
        ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        BigDecimal salario = new BigDecimal(
            view.getSalario().getText().trim().replace(',', '.')
        );

        String contrato =
            (String) view.getTipoContrato().getSelectedItem();

        contratacao[1] = candidato;
        contratacao[2] = cargo;
        contratacao[3] = data;
        contratacao[4] = salario.setScale(2).toPlainString();
        contratacao[5] = contrato;

        JOptionPane.showMessageDialog(
            view,
            "Contratação atualizada com sucesso!",
            "Atualização",
            JOptionPane.INFORMATION_MESSAGE
        );

        limpar();
    }

    private void excluir() {
        String idTexto = view.getId().getText().trim();

        if (idTexto.isEmpty()) {
            erro("Informe o código da contratação que deseja excluir.");
            return;
        }

        int id;

        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            erro("Código de contratação inválido.");
            return;
        }

        Object[] contratacao = buscarPorId(id);

        if (contratacao == null) {
            erro("Não foi encontrada uma contratação com esse código.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            view,
            "Deseja realmente excluir a contratação de "
                + contratacao[1] + "?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            contratacoes.remove(contratacao);

            JOptionPane.showMessageDialog(
                view,
                "Contratação excluída com sucesso!",
                "Exclusão",
                JOptionPane.INFORMATION_MESSAGE
            );

            limpar();
        }
    }

    private boolean validarCampos() {
        String candidato = view.getCandidato().getText().trim();
        String cargo = view.getCargo().getText().trim();
        String dataTexto = view.getDataAdmissao().getText().trim();
        String salarioTexto = view.getSalario().getText().trim();

        if (candidato.isEmpty()) {
            erro("Informe o nome do candidato.");
            view.getCandidato().requestFocusInWindow();
            return false;
        }

        if (cargo.isEmpty()) {
            erro("Informe o cargo.");
            view.getCargo().requestFocusInWindow();
            return false;
        }

        if (dataTexto.isEmpty()) {
            erro("Informe a data de admissão no formato dd/MM/aaaa.");
            view.getDataAdmissao().requestFocusInWindow();
            return false;
        }

        try {
            LocalDate.parse(dataTexto, formatoData);
        } catch (DateTimeParseException e) {
            erro("Data inválida. Utilize o formato dd/MM/aaaa.");
            view.getDataAdmissao().requestFocusInWindow();
            return false;
        }

        if (salarioTexto.isEmpty()) {
            erro("Informe o salário.");
            view.getSalario().requestFocusInWindow();
            return false;
        }

        if (!salarioTexto.matches("\\d+(?:[,.]\\d{1,2})?")) {
            erro("Informe um salário válido. Exemplo: 2500,00.");
            view.getSalario().requestFocusInWindow();
            return false;
        }

        BigDecimal salario;

        try {
            salario = new BigDecimal(salarioTexto.replace(',', '.'));
        } catch (NumberFormatException e) {
            erro("Informe um salário válido.");
            view.getSalario().requestFocusInWindow();
            return false;
        }

        if (salario.compareTo(BigDecimal.ZERO) <= 0) {
            erro("O salário deve ser maior que zero.");
            view.getSalario().requestFocusInWindow();
            return false;
        }

        if (view.getTipoContrato().getSelectedIndex() == 0) {
            erro("Selecione o tipo de contrato.");
            view.getTipoContrato().requestFocusInWindow();
            return false;
        }

        return true;
    }

    private Object[] buscarPorId(int id) {
        for (Object[] contratacao : contratacoes) {
            if ((int) contratacao[0] == id) {
                return contratacao;
            }
        }

        return null;
    }

    private void limpar() {
        view.getId().setText("");
        view.getCandidato().setText("");
        view.getCargo().setText("");
        view.getDataAdmissao().setText("");
        view.getSalario().setText("");
        view.getTipoContrato().setSelectedIndex(0);
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
