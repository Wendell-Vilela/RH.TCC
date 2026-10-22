
package controller;

import dao.ProcessoSeletivoDAO;
import model.ProcessoSeletivo;
import view.TelaProcessoSeletivo;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ProcessoSeletivoController {

    private final TelaProcessoSeletivo view;
    private final ProcessoSeletivoDAO dao = new ProcessoSeletivoDAO();

    public ProcessoSeletivoController(TelaProcessoSeletivo view) {
        this.view = view;
        iniciarEventos();
        listar();
    }

    private void iniciarEventos() {
        view.getBtnSalvar().addActionListener(e -> salvar());
        view.getBtnAtualizar().addActionListener(e -> atualizar());
        view.getBtnExcluir().addActionListener(e -> excluir());
        view.getBtnLimpar().addActionListener(e -> limpar());

        view.getTabelaSelecao().getSelectionModel()
            .addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    preencherFormulario();
                }
            });
    }

    private void salvar() {
        if (!validarCampos()) {
            return;
        }

        int idCandidato = dao.buscarIdCandidato(
            view.getCandidato().getText().trim()
        );

        int idVaga = dao.buscarIdVaga(
            view.getVaga().getText().trim()
        );

        if (idCandidato == -1) {
            erro("Candidato não encontrado no banco de dados.");
            return;
        }

        if (idVaga == -1) {
            erro("Vaga não encontrada no banco de dados.");
            return;
        }

        ProcessoSeletivo ps = obterProcesso();

        if (dao.inserir(ps, idCandidato, idVaga)) {
            JOptionPane.showMessageDialog(
                view,
                "Processo seletivo cadastrado com sucesso!",
                "Cadastro",
                JOptionPane.INFORMATION_MESSAGE
            );

            listar();
            limpar();
        } else {
            erro("Não foi possível cadastrar o processo seletivo.");
        }
    }

    private void atualizar() {
        JTable tabela = view.getTabelaSelecao();
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            erro("Selecione um processo seletivo na tabela para atualizar.");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        int idCandidato = dao.buscarIdCandidato(
            view.getCandidato().getText().trim()
        );

        int idVaga = dao.buscarIdVaga(
            view.getVaga().getText().trim()
        );

        if (idCandidato == -1) {
            erro("Candidato não encontrado no banco de dados.");
            return;
        }

        if (idVaga == -1) {
            erro("Vaga não encontrada no banco de dados.");
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);
        int id = Integer.parseInt(
            String.valueOf(obterModelo().getValueAt(linhaModelo, 0))
        );

        ProcessoSeletivo ps = obterProcesso();
        ps.setId(id);

        if (dao.atualizar(ps, idCandidato, idVaga)) {
            JOptionPane.showMessageDialog(
                view,
                "Processo seletivo atualizado com sucesso!",
                "Atualização",
                JOptionPane.INFORMATION_MESSAGE
            );

            listar();
            limpar();
        } else {
            erro("Não foi possível atualizar o processo seletivo.");
        }
    }

    private void excluir() {
        JTable tabela = view.getTabelaSelecao();
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            erro("Selecione um processo seletivo na tabela para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            view,
            "Deseja realmente excluir o processo seletivo selecionado?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);
        int id = Integer.parseInt(
            String.valueOf(obterModelo().getValueAt(linhaModelo, 0))
        );

        if (dao.excluir(id)) {
            JOptionPane.showMessageDialog(
                view,
                "Processo seletivo excluído com sucesso!",
                "Exclusão",
                JOptionPane.INFORMATION_MESSAGE
            );

            listar();
            limpar();
        } else {
            erro("Não foi possível excluir o processo seletivo.");
        }
    }

    private void listar() {
        DefaultTableModel modelo = obterModelo();
        modelo.setRowCount(0);

        List<ProcessoSeletivo> processos = dao.listarProcessos();

        for (ProcessoSeletivo ps : processos) {
            modelo.addRow(new Object[]{
                ps.getId(),
                ps.getCandidato(),
                ps.getVaga(),
                ps.getRecrutador(),
                ps.getEtapaAtual(),
                ps.getStatus()
            });
        }
    }

    private void preencherFormulario() {
        JTable tabela = view.getTabelaSelecao();
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);
        DefaultTableModel modelo = obterModelo();

        view.getId().setText(
            String.valueOf(modelo.getValueAt(linhaModelo, 0))
        );

        view.getCandidato().setText(
            String.valueOf(modelo.getValueAt(linhaModelo, 1))
        );

        view.getVaga().setText(
            String.valueOf(modelo.getValueAt(linhaModelo, 2))
        );

        view.getRecrutador().setText(
            String.valueOf(modelo.getValueAt(linhaModelo, 3))
        );

        view.getEtapaAtual().setSelectedItem(
            modelo.getValueAt(linhaModelo, 4)
        );

        view.getStatus().setSelectedItem(
            modelo.getValueAt(linhaModelo, 5)
        );
    }

    private ProcessoSeletivo obterProcesso() {
        ProcessoSeletivo ps = new ProcessoSeletivo();

        ps.setCandidato(view.getCandidato().getText().trim());
        ps.setVaga(view.getVaga().getText().trim());
        ps.setRecrutador(view.getRecrutador().getText().trim());
        ps.setEtapaAtual(
            (String) view.getEtapaAtual().getSelectedItem()
        );
        ps.setStatus(
            (String) view.getStatus().getSelectedItem()
        );

        return ps;
    }

    private boolean validarCampos() {
        if (view.getCandidato().getText().trim().isEmpty()) {
            erro("Informe o nome do candidato.");
            view.getCandidato().requestFocusInWindow();
            return false;
        }

        if (view.getVaga().getText().trim().isEmpty()) {
            erro("Informe a vaga do processo seletivo.");
            view.getVaga().requestFocusInWindow();
            return false;
        }

        if (view.getRecrutador().getText().trim().isEmpty()) {
            erro("Informe o nome do recrutador.");
            view.getRecrutador().requestFocusInWindow();
            return false;
        }

        if (view.getEtapaAtual().getSelectedIndex() == 0) {
            erro("Selecione a etapa atual.");
            view.getEtapaAtual().requestFocusInWindow();
            return false;
        }

        if (view.getStatus().getSelectedIndex() == 0) {
            erro("Selecione o status do processo seletivo.");
            view.getStatus().requestFocusInWindow();
            return false;
        }

        return true;
    }

    private void limpar() {
        view.getId().setText("");
        view.getCandidato().setText("");
        view.getVaga().setText("");
        view.getRecrutador().setText("");
        view.getEtapaAtual().setSelectedIndex(0);
        view.getStatus().setSelectedIndex(0);
        view.getTabelaSelecao().clearSelection();
        view.getCandidato().requestFocusInWindow();
    }

    private DefaultTableModel obterModelo() {
        return (DefaultTableModel) view.getTabelaSelecao().getModel();
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
