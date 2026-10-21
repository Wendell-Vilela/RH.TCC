package controller;

import javax.swing.*;
import model.CustoMensal;
import view.TelaCustoMensal;
import dao.CustoMensalDAO;

public class CustoMensalController {
    private TelaCustoMensal tela;
    private CustoMensalDAO dao;

    public CustoMensalController(TelaCustoMensal tela) {
        this.tela = tela;
        this.dao = new CustoMensalDAO();
    }

    public void carregarDados() {
        try {
            CustoMensal custo = dao.buscarCustoMensal();

            if(custo == null) {
                JOptionPane.showMessageDialog(
                    tela, "Nenhum dado encontrado.", "Aviso",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            tela.atualizarDados(custo);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(
                tela, "Erro ao carregar custos mensais: "
                + e.getMessage(), "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void atualizarDados() {
        if(SwingUtilities.isEventDispatchThread()) {
            carregarDados();
        } else {
            SwingUtilities.invokeLater(this::carregarDados);
        }
    }
}
