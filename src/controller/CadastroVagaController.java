
package controller;

import dao.VagaDAO;
import model.CadastroVaga;
import view.TelaCadastroVaga;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CadastroVagaController {

    private final TelaCadastroVaga view;
    private final VagaDAO dao;

    public CadastroVagaController(TelaCadastroVaga view) {
        this.view = view;
        this.dao = new VagaDAO();

        iniciarEventos();
        listar();
    }

    private void iniciarEventos() {
        view.getBtnSalvar().addActionListener(e -> salvar());
        view.getBtnExcluir().addActionListener(e -> excluir());
        view.getBtnLimpar().addActionListener(e -> limpar());
        view.getBtnAtualizar().addActionListener(e -> atualizar());

        view.getTabelaVagas().getSelectionModel()
                .addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        preencherFormulario();
                    }
                });
    }

    private CadastroVaga obterDadosFormulario() {
        String cargo = view.getCargo().getText().trim();
        String departamento = view.getDepartamento().getText().trim();
        String contrato = (String) view.getTipoContrato().getSelectedItem();
        String modalidade = (String) view.getModalidade().getSelectedItem();
        String salarioTexto = view.getSalario().getText().trim();
        String status = (String) view.getStatus().getSelectedItem();
        String descricao = view.getDescricao().getText().trim();

        if (cargo.isEmpty()) {
            erro("Informe o cargo da vaga.");
            view.getCargo().requestFocusInWindow();
            return null;
        }

        if (departamento.isEmpty()) {
            erro("Informe o departamento da vaga.");
            view.getDepartamento().requestFocusInWindow();
            return null;
        }

        if (view.getTipoContrato().getSelectedIndex() == 0) {
            erro("Selecione o tipo de contrato.");
            view.getTipoContrato().requestFocusInWindow();
            return null;
        }

        if (view.getModalidade().getSelectedIndex() == 0) {
            erro("Selecione a modalidade da vaga.");
            view.getModalidade().requestFocusInWindow();
            return null;
        }

        if (salarioTexto.isEmpty()
                || !salarioTexto.matches("\\d+(?:[,.]\\d{1,2})?")) {
            erro("Informe um salário válido. Exemplo: 2500,00.");
            view.getSalario().requestFocusInWindow();
            return null;
        }

        BigDecimal salario;

        try {
            salario = new BigDecimal(salarioTexto.replace(',', '.'));
        } catch (NumberFormatException e) {
            erro("O salário informado é inválido.");
            view.getSalario().requestFocusInWindow();
            return null;
        }

        if (salario.compareTo(BigDecimal.ZERO) <= 0) {
            erro("O salário deve ser maior que zero.");
            view.getSalario().requestFocusInWindow();
            return null;
        }

        if (view.getStatus().getSelectedIndex() == 0) {
            erro("Selecione o status da vaga.");
            view.getStatus().requestFocusInWindow();
            return null;
        }

        if (descricao.isEmpty()) {
            erro("Informe a descrição da vaga.");
            view.getDescricao().requestFocusInWindow();
            return null;
        }

        CadastroVaga vaga = new CadastroVaga();
        vaga.setCargo(cargo);
        vaga.setDepartamento(departamento);
        vaga.setTipoContrato(contrato);
        vaga.setModalidade(modalidade);
        vaga.setSalario(salario.setScale(2).toPlainString());
        vaga.setStatus(status);
        vaga.setDescricao(descricao);

        return vaga;
    }

    private void salvar() {
        CadastroVaga vaga = obterDadosFormulario();

        if (vaga == null) {
            return;
        }

        if (dao.inserir(vaga)) {
            JOptionPane.showMessageDialog(
                    view,
                    "Vaga cadastrada com sucesso!",
                    "Cadastro de Vaga",
                    JOptionPane.INFORMATION_MESSAGE
            );

            listar();
            limpar();
        } else {
            erro("Não foi possível cadastrar a vaga. Verifique os dados e o departamento.");
        }
    }

    private void atualizar() {
        JTable tabela = view.getTabelaVagas();
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            erro("Selecione uma vaga na tabela para atualizar.");
            return;
        }

        String textoId = view.getId().getText().trim();

        if (textoId.isEmpty()) {
            erro("Não foi possível identificar a vaga selecionada.");
            return;
        }

        CadastroVaga vaga = obterDadosFormulario();

        if (vaga == null) {
            return;
        }

        try {
            vaga.setId(Integer.parseInt(textoId));
        } catch (NumberFormatException e) {
            erro("O código da vaga é inválido.");
            return;
        }

        if (dao.atualizar(vaga)) {
            JOptionPane.showMessageDialog(
                    view,
                    "Vaga atualizada com sucesso!",
                    "Atualização",
                    JOptionPane.INFORMATION_MESSAGE
            );

            listar();
            limpar();
        } else {
            erro("Não foi possível atualizar a vaga. Verifique os dados e o departamento.");
        }
    }

    private void excluir() {
        String textoId = view.getId().getText().trim();

        if (view.getTabelaVagas().getSelectedRow() == -1
                || textoId.isEmpty()) {
            erro("Selecione uma vaga na tabela para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                view,
                "Deseja realmente excluir a vaga selecionada?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        int id;

        try {
            id = Integer.parseInt(textoId);
        } catch (NumberFormatException e) {
            erro("O código da vaga é inválido.");
            return;
        }

        if (dao.excluir(id)) {
            JOptionPane.showMessageDialog(
                    view,
                    "Vaga excluída com sucesso!",
                    "Exclusão",
                    JOptionPane.INFORMATION_MESSAGE
            );

            listar();
            limpar();
        } else {
            erro("Não foi possível excluir a vaga. Verifique se ela está vinculada a outros registros.");
        }
    }

    private void listar() {
        List<CadastroVaga> vagas = dao.listarVagas();
        DefaultTableModel modelo = obterModelo();

        modelo.setRowCount(0);

        for (CadastroVaga vaga : vagas) {
            modelo.addRow(new Object[]{
                vaga.getId(),
                vaga.getCargo(),
                vaga.getDepartamento(),
                vaga.getTipoContrato(),
                vaga.getModalidade(),
                vaga.getSalario(),
                vaga.getStatus(),
                vaga.getDescricao()
            });
        }
    }

    private void preencherFormulario() {
        int linha = view.getTabelaVagas().getSelectedRow();

        if (linha == -1) {
            return;
        }

        linha = view.getTabelaVagas().convertRowIndexToModel(linha);
        DefaultTableModel modelo = obterModelo();

        view.getId().setText(String.valueOf(modelo.getValueAt(linha, 0)));
        view.getCargo().setText(String.valueOf(modelo.getValueAt(linha, 1)));
        view.getDepartamento().setText(String.valueOf(modelo.getValueAt(linha, 2)));
        view.getTipoContrato().setSelectedItem(modelo.getValueAt(linha, 3));
        view.getModalidade().setSelectedItem(modelo.getValueAt(linha, 4));
        view.getSalario().setText(String.valueOf(modelo.getValueAt(linha, 5)));
        view.getStatus().setSelectedItem(modelo.getValueAt(linha, 6));
        view.getDescricao().setText(String.valueOf(modelo.getValueAt(linha, 7)));
    }

    private void limpar() {
        view.getId().setText("");
        view.getCargo().setText("");
        view.getDepartamento().setText("");
        view.getTipoContrato().setSelectedIndex(0);
        view.getModalidade().setSelectedIndex(0);
        view.getSalario().setText("");
        view.getStatus().setSelectedIndex(0);
        view.getDescricao().setText("");

        view.getTabelaVagas().clearSelection();
        view.getCargo().requestFocusInWindow();
    }

    private DefaultTableModel obterModelo() {
        return (DefaultTableModel) view.getTabelaVagas().getModel();
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
