package view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class TelaMenu extends JFrame {

    private static final long serialVersionUID = 1L;

    public TelaMenu() {

        setTitle("ERP - Dashboard/Kauã");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLocationRelativeTo(null);

        // Criação das abas
        JTabbedPane abas = new JTabbedPane();

        // Adiciona cada tela do módulo
        abas.addTab(
            "Avaliação e Desenvolvimento",
            new ViewFuncionariosAtivos()
        );

        abas.addTab(
            "Recrutamento e Seleção",
            new ViewNovasAdmissoes()
        );

        abas.addTab(
            "Dashboards",
            new ViewTaxaTurnover()
        );



        // Adiciona as abas na janela
        add(abas);
    }
}