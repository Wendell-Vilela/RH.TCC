
package controller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.GestaoFerias;
import view.TelaGestaoFerias;
import dao.GestaoFeriasDAO;

public class GestaoFeriasController {

    private final TelaGestaoFerias tela;
    private final GestaoFeriasDAO dao;

    public GestaoFeriasController(TelaGestaoFerias tela) {
        this.tela = tela;
        this.dao = new GestaoFeriasDAO();
    }

    public void carregarDados() {
        new Thread(() -> {
            try {
                List<GestaoFerias> lista =
                    dao.listarGestaoFeriasPorDepartamento();

                SwingUtilities.invokeLater(() -> {
                    tela.AtualizarTabela(lista);

                    if (lista.isEmpty()) {
                        JOptionPane.showMessageDialog(
                            tela,
                            "Nenhum dado de férias encontrado.",
                            "Aviso",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                        tela,
                        "Erro ao carregar dados de Gestão de Férias: "
                            + e.getMessage(),
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
