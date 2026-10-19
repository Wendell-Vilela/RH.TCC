
package controller;

import java.math.BigDecimal;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import view.TelaCadastroVaga;

public class CadastroVagaController {

    private final TelaCadastroVaga view;

    public CadastroVagaController(TelaCadastroVaga view) {
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
            return;
        }

        if (departamento.isEmpty()) {
            erro("Informe o departamento da vaga.");
            view.getDepartamento().requestFocusInWindow();
            return;
        }

        if (view.getTipoContrato().getSelectedIndex() == 0) {
            erro("Selecione o tipo de contrato.");
            view.getTipoContrato().requestFocusInWindow();
            return;
        }

        if (view.getModalidade().getSelectedIndex() == 0) {
            erro("Selecione a modalidade da vaga.");
            view.getModalidade().requestFocusInWindow();
            return;
        }

        if (salarioTexto.isEmpty()) {
            erro("Informe o salário da vaga.");
            view.getSalario().requestFocusInWindow();
            return;
        }

        if (!salarioTexto.matches("\\d+(?:[,.]\\d{1,2})?")) {
            erro("Informe um salário válido. Exemplo: 2500,00.");
            view.getSalario().requestFocusInWindow();
            return;
        }

        BigDecimal salario;

        try {
            salario = new BigDecimal(salarioTexto.replace(',', '.'));
        } catch (NumberFormatException e) {
            erro("O salário informado é inválido.");
            view.getSalario().requestFocusInWindow();
            return;
        }

        if (salario.compareTo(BigDecimal.ZERO) <= 0) {
            erro("O salário deve ser maior que zero.");
            view.getSalario().requestFocusInWindow();
            return;
        }

        if (view.getStatus().getSelectedIndex() == 0) {
            erro("Selecione o status da vaga.");
            view.getStatus().requestFocusInWindow();
            return;
        }

        if (descricao.isEmpty()) {
            erro("Informe a descrição da vaga.");
            view.getDescricao().requestFocusInWindow();
            return;
        }

        DefaultTableModel modelo = obterModelo();

        int codigo = proximoCodigo(modelo);

        modelo.addRow(new Object[] {
            codigo,
            cargo,
            departamento,
            contrato,
            modalidade,
            salario.setScale(2).toPlainString(),
            status,
            descricao
        });

        JOptionPane.showMessageDialog(
            view,
            "Vaga cadastrada com sucesso!",
            "Cadastro de Vaga",
            JOptionPane.INFORMATION_MESSAGE
        );

        limpar();
    }
    

	private void atualizar() {
	    JTable tabela = view.getTabelaVagas();
	    int linhaSelecionada = tabela.getSelectedRow();
	
	    if (linhaSelecionada == -1) {
	        erro("Selecione uma vaga na tabela para atualizar.");
	        return;
	    }
	
	    String cargo = view.getCargo().getText().trim();
	    String departamento = view.getDepartamento().getText().trim();
	    String contrato = (String) view.getTipoContrato().getSelectedItem();
	    String modalidade = (String) view.getModalidade().getSelectedItem();
	    String salarioTexto = view.getSalario().getText().trim();
	    String status = (String) view.getStatus().getSelectedItem();
	    String descricao = view.getDescricao().getText().trim();
	
	    if (cargo.isEmpty() || departamento.isEmpty()
	            || salarioTexto.isEmpty() || descricao.isEmpty()) {
	        erro("Preencha todos os campos obrigatórios.");
	        return;
	    }
	
	    if (view.getTipoContrato().getSelectedIndex() == 0
	            || view.getModalidade().getSelectedIndex() == 0
	            || view.getStatus().getSelectedIndex() == 0) {
	        erro("Selecione o contrato, a modalidade e o status.");
	        return;
	    }
	
	    if (!salarioTexto.matches("\\d+(?:[,.]\\d{1,2})?")) {
	        erro("Informe um salário válido. Exemplo: 2500,00.");
	        view.getSalario().requestFocusInWindow();
	        return;
	    }
	
	    BigDecimal salario;
	
	    try {
	        salario = new BigDecimal(salarioTexto.replace(',', '.'));
	    } catch (NumberFormatException e) {
	        erro("Informe um salário válido.");
	        return;
	    }
	
	    if (salario.compareTo(BigDecimal.ZERO) <= 0) {
	        erro("O salário deve ser maior que zero.");
	        return;
	    }
	
	    int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);
	    DefaultTableModel modelo = obterModelo();
	
	    modelo.setValueAt(cargo, linhaModelo, 1);
	    modelo.setValueAt(departamento, linhaModelo, 2);
	    modelo.setValueAt(contrato, linhaModelo, 3);
	    modelo.setValueAt(modalidade, linhaModelo, 4);
	    modelo.setValueAt(salario.setScale(2).toPlainString(), linhaModelo, 5);
	    modelo.setValueAt(status, linhaModelo, 6);
	    modelo.setValueAt(descricao, linhaModelo, 7);
	
	    JOptionPane.showMessageDialog(
	        view,
	        "Vaga atualizada com sucesso!",
	        "Atualização",
	        JOptionPane.INFORMATION_MESSAGE
	    );
	
	    limpar();
	}


    private void excluir() {
        JTable tabela = view.getTabelaVagas();
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
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

        if (confirmacao == JOptionPane.YES_OPTION) {
            int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);

            obterModelo().removeRow(linhaModelo);

            limpar();

            JOptionPane.showMessageDialog(
                view,
                "Vaga excluída com sucesso!",
                "Exclusão",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
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
        return (DefaultTableModel) view.getTabelaVagas().getModel();
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
