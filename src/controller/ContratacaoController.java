
package controller;

import dao.ContratacaoDAO;
import model.Contratacao;
import view.TelaContratacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

import javax.swing.JOptionPane;

public class ContratacaoController {

    private final TelaContratacao view;
    private final ContratacaoDAO dao = new ContratacaoDAO();

    private final DateTimeFormatter formatoData =
        DateTimeFormatter.ofPattern("dd/MM/aaaa")
            .withResolverStyle(ResolverStyle.STRICT);

    public ContratacaoController(TelaContratacao view) {
        this.view = view;
        iniciarEventos();
    }

    private void iniciarEventos() {
        view.getBtnSalvar().addActionListener(e -> salvar());
        view.getBtnAtualizar().addActionListener(e -> atualizar());
        view.getBtnExcluir().addActionListener(e -> excluir());
        view.getBtnLimpar().addActionListener(e -> limpar());
        view.getId().addActionListener(e -> carregarPorId());
    }

    private void salvar() {
        if (!validarCampos()) {
            return;
        }

        int idCandidato = dao.buscarIdCandidato(
            view.getCandidato().getText().trim()
        );

        int idVaga = dao.buscarIdVaga(
            view.getCargo().getText().trim()
        );

        if (idCandidato == -1) {
            erro("Candidato não encontrado no banco de dados.");
            return;
        }

        if (idVaga == -1) {
            erro("Vaga não encontrada no banco de dados.");
            return;
        }

        Contratacao contratacao = obterContratacao();

        if (dao.inserir(contratacao, idCandidato, idVaga)) {
            JOptionPane.showMessageDialog(
                view,
                "Contratação salva com sucesso!",
                "Cadastro de Contratação",
                JOptionPane.INFORMATION_MESSAGE
            );
            limpar();
        } else {
            erro("Não foi possível salvar a contratação.");
        }
    }

    private void atualizar() {
        int id = obterIdInformado();

        if (id == -1) {
            return;
        }

        if (!validarCampos()) {
            return;
        }

        int idCandidato = dao.buscarIdCandidato(
            view.getCandidato().getText().trim()
        );

        int idVaga = dao.buscarIdVaga(
            view.getCargo().getText().trim()
        );

        if (idCandidato == -1) {
            erro("Candidato não encontrado no banco de dados.");
            return;
        }

        if (idVaga == -1) {
            erro("Vaga não encontrada no banco de dados.");
            return;
        }

        Contratacao contratacao = obterContratacao();
        contratacao.setId(id);

        if (dao.atualizar(contratacao, idCandidato, idVaga)) {
            JOptionPane.showMessageDialog(
                view,
                "Contratação atualizada com sucesso!",
                "Atualização",
                JOptionPane.INFORMATION_MESSAGE
            );
            limpar();
        } else {
            erro("Não foi possível atualizar a contratação. Verifique o código.");
        }
    }

    private void excluir() {
        int id = obterIdInformado();

        if (id == -1) {
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            view,
            "Deseja realmente excluir a contratação de "
                + view.getCandidato().getText().trim() + "?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.excluir(id)) {
            JOptionPane.showMessageDialog(
                view,
                "Contratação excluída com sucesso!",
                "Exclusão",
                JOptionPane.INFORMATION_MESSAGE
            );
            limpar();
        } else {
            erro("Não foi possível excluir a contratação. Verifique o código.");
        }
    }

    private void carregarPorId() {
        String texto = view.getId().getText().trim();

        if (texto.isEmpty()) {
            return;
        }

        int id;

        try {
            id = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            erro("Código de contratação inválido.");
            return;
        }

        List<Contratacao> lista = dao.listarContratacoes();

        for (Contratacao c : lista) {
            if (c.getId() == id) {
                preencherCampos(c);
                return;
            }
        }

        erro("Não foi encontrada uma contratação com esse código.");
    }

    private void preencherCampos(Contratacao c) {
        view.getId().setText(String.valueOf(c.getId()));
        view.getCandidato().setText(c.getCandidato());
        view.getCargo().setText(c.getCargo());

        try {
            LocalDate data = LocalDate.parse(c.getDataAdmissao());
            view.getDataAdmissao().setText(
                data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        } catch (DateTimeParseException e) {
            view.getDataAdmissao().setText(c.getDataAdmissao());
        }

        view.getSalario().setText(c.getSalario());
        view.getTipoContrato().setSelectedItem(c.getTipoContrato());
    }

    private Contratacao obterContratacao() {
        LocalDate data = LocalDate.parse(
            view.getDataAdmissao().getText().trim(),
            formatoData
        );

        String salario = new BigDecimal(
            view.getSalario().getText().trim().replace(',', '.')
        ).setScale(2).toPlainString();

        return new Contratacao(
            0,
            view.getCandidato().getText().trim(),
            view.getCargo().getText().trim(),
            data.toString(),
            salario,
            (String) view.getTipoContrato().getSelectedItem()
        );
    }

    private int obterIdInformado() {
        String texto = view.getId().getText().trim();

        if (texto.isEmpty()) {
            erro("Informe o código da contratação.");
            return -1;
        }

        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            erro("Código de contratação inválido.");
            return -1;
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

        try {
            LocalDate.parse(dataTexto, formatoData);
        } catch (DateTimeParseException e) {
            erro("Data inválida. Utilize o formato dd/MM/aaaa.");
            view.getDataAdmissao().requestFocusInWindow();
            return false;
        }

        if (!salarioTexto.matches("\\d+(?:[,.]\\d{1,2})?")) {
            erro("Informe um salário válido. Exemplo: 2500,00.");
            view.getSalario().requestFocusInWindow();
            return false;
        }

        try {
            BigDecimal salario = new BigDecimal(
                salarioTexto.replace(',', '.')
            );

            if (salario.compareTo(BigDecimal.ZERO) <= 0) {
                erro("O salário deve ser maior que zero.");
                view.getSalario().requestFocusInWindow();
                return false;
            }
        } catch (NumberFormatException e) {
            erro("Informe um salário válido.");
            return false;
        }

        if (view.getTipoContrato().getSelectedIndex() == 0) {
            erro("Selecione o tipo de contrato.");
            view.getTipoContrato().requestFocusInWindow();
            return false;
        }

        return true;
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
            "Erro",
            JOptionPane.WARNING_MESSAGE
        );
    }
}
