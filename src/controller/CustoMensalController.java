
package controller;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.time.LocalDate;

import model.CustoMensal;
import view.TelaCustoMensal;
import dao.CustoMensalDAO;

public class CustoMensalController {

    private final TelaCustoMensal tela;
    private final CustoMensalDAO dao;

    public CustoMensalController(TelaCustoMensal tela) {
        this.tela = tela;
        this.dao = new CustoMensalDAO();
    }

    public void carregarDados() {
        LocalDate hoje = LocalDate.now();
        int mes = hoje.getMonthValue();
        int ano = hoje.getYear();

        new Thread(() -> {
            try {
                CustoMensal custo = dao.buscarCustoMensal(mes, ano);

                SwingUtilities.invokeLater(() -> {
                    if (custo == null) {
                        JOptionPane.showMessageDialog(
                            tela,
                            "Nenhum dado encontrado para o mês atual.",
                            "Aviso",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        return;
                    }

                    tela.atualizarDados(custo);
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                        tela,
                        "Erro ao carregar custos mensais: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    )
                );
            }
        }).start();
    }

    public void atualizarDados() {
        carregarDados();
    }
}
