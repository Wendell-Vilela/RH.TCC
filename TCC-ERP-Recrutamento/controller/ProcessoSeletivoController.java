
package controller;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import view.TelaProcessoSeletivo;

public class ProcessoSeletivoController {

    private final TelaProcessoSeletivo view;

    public ProcessoSeletivoController(TelaProcessoSeletivo view) {
        this.view = view;
        iniciarEventos();
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
        String candidato = view.getCandidato().getText().trim();
        String vaga = view.getVaga().getText().trim();
        String recrutador = view.getRecrutador().getText().trim();
        String etapa = (String) view.getEtapaAtual().getSelectedItem();
        String status = (String) view.getStatus().getSelectedItem();

        if (candidato.isEmpty()) {
            erro("Informe o nome do candidato.");
            view.getCandidato().requestFocusInWindow();
            return;
        }

        if (vaga.isEmpty()) {
            erro("Informe a vaga do processo seletivo.");
            view.getVaga().requestFocusInWindow();
            return;
        }

        if (recrutador.isEmpty()) {
            erro("Informe o nome do recrutador.");
            view.getRecrutador().requestFocusInWindow();
            return;
        }

        if (view.getEtapaAtual().getSelectedIndex() == 0) {
            erro("Selecione a etapa atual.");
            view.getEtapaAtual().requestFocusInWindow();
            return;
        }

        if (view.getStatus().getSelectedIndex() == 0) {
            erro("Selecione o status do processo seletivo.");
            view.getStatus().requestFocusInWindow();
            return;
        }

        DefaultTableModel modelo = obterModelo();
        int codigo = proximoCodigo(modelo);

        modelo.addRow(new Object[]{
            codigo,
            candidato,
            vaga,
            recrutador,
            etapa,
            status
        });

        JOptionPane.showMessageDialog(
            view,
            "Processo seletivo cadastrado com sucesso!",
            "Cadastro",
            JOptionPane.INFORMATION_MESSAGE
        );

        limpar();
    }

    private void atualizar() {
        JTable tabela = view.getTabelaSelecao();
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            erro("Selecione um processo seletivo na tabela para atualizar.");
            return;
        }

        String candidato = view.getCandidato().getText().trim();
        String vaga = view.getVaga().getText().trim();
        String recrutador = view.getRecrutador().getText().trim();
        String etapa = (String) view.getEtapaAtual().getSelectedItem();
        String status = (String) view.getStatus().getSelectedItem();

        if (candidato.isEmpty()) {
            erro("Informe o nome do candidato.");
            view.getCandidato().requestFocusInWindow();
            return;
        }

        if (vaga.isEmpty()) {
            erro("Informe a vaga do processo seletivo.");
            view.getVaga().requestFocusInWindow();
            return;
        }

        if (recrutador.isEmpty()) {
            erro("Informe o nome do recrutador.");
            view.getRecrutador().requestFocusInWindow();
            return;
        }

        if (view.getEtapaAtual().getSelectedIndex() == 0) {
            erro("Selecione a etapa atual.");
            view.getEtapaAtual().requestFocusInWindow();
            return;
        }

        if (view.getStatus().getSelectedIndex() == 0) {
            erro("Selecione o status do processo seletivo.");
            view.getStatus().requestFocusInWindow();
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);
        DefaultTableModel modelo = obterModelo();

        modelo.setValueAt(candidato, linhaModelo, 1);
        modelo.setValueAt(vaga, linhaModelo, 2);
        modelo.setValueAt(recrutador, linhaModelo, 3);
        modelo.setValueAt(etapa, linhaModelo, 4);
        modelo.setValueAt(status, linhaModelo, 5);

        JOptionPane.showMessageDialog(
            view,
            "Processo seletivo atualizado com sucesso!",
            "Atualização",
            JOptionPane.INFORMATION_MESSAGE
        );

        limpar();
    }

    private void excluir() {
        JTable tabela = view.getTabelaSelecao();
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
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

        if (confirmacao == JOptionPane.YES_OPTION) {
            int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);
            obterModelo().removeRow(linhaModelo);

            limpar();

            JOptionPane.showMessageDialog(
                view,
                "Processo seletivo excluído com sucesso!",
                "Exclusão",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void preencherFormulario() {
        JTable tabela = view.getTabelaSelecao();
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);
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

    private int proximoCodigo(DefaultTableModel modelo) {
        int maiorCodigo = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            int codigo = Integer.parseInt(
                String.valueOf(modelo.getValueAt(i, 0))
            );

            if (codigo > maiorCodigo) {
                maiorCodigo = codigo;
            }
        }

        return maiorCodigo + 1;
    }

    private DefaultTableModel obterModelo() {
        return (DefaultTableModel) view.getTabelaSelecao().getModel();
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
