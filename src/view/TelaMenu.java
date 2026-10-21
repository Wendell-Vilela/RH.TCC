package view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class TelaMenu extends JFrame {

    private static final long serialVersionUID = 1L;

    public TelaMenu() {

        setTitle("ERP - Gestão de Funcionários");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLocationRelativeTo(null);

        // Criação das abas
        JTabbedPane abas = new JTabbedPane();


        abas.addTab(
            "Gestão de Funcionários",
            new TelaCadastroFuncionario()
        );



        // Adiciona as abas na janela
        add(abas);
    }
}