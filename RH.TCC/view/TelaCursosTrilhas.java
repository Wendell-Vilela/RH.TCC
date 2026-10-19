package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TelaCursosTrilhas extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JComboBox<String> area = new JComboBox<>(
        new String[]{
            "Selecione a area",
            "Tecnologia",
            "Recursos Humanos",
            "Financeiro",
            "Marketing"
        }
    );

    private final JComboBox<String> funcionario = new JComboBox<>(
        new String[]{"Selecione o funcionario"}
    );

    private final JButton selecionar = new JButton("Selecionar");

    private final JLabel horasRealizadas =
        new JLabel("Horas realizadas: 0h");

    private final JLabel cursosConcluidos =
        new JLabel("Cursos concluidos: 0");

    private final JTable tabelaCursosInternos = new JTable();
    private final JTable tabelaCursosExternos = new JTable();

    public TelaCursosTrilhas() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        montar();
    }

    private void montar() {

        JPanel selecao = new JPanel(new GridBagLayout());

        selecao.setBorder(
            BorderFactory.createTitledBorder("Selecionar Funcionario")
        );

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);

        componente(selecao, g, 0, "Area:", area);
        componente(selecao, g, 1, "Funcionario:", funcionario);

        g.gridx = 2;
        g.gridy = 1;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;

        selecao.add(selecionar, g);

        JPanel resumo = new JPanel(new GridLayout(1, 2, 15, 5));

        resumo.setBorder(
            BorderFactory.createTitledBorder("Resumo de Cursos")
        );

        horasRealizadas.setHorizontalAlignment(SwingConstants.CENTER);
        cursosConcluidos.setHorizontalAlignment(SwingConstants.CENTER);

        resumo.add(horasRealizadas);
        resumo.add(cursosConcluidos);

        String[] colunas = {
            "Curso",
            "Carga Horaria",
            "Fornecedor",
            "Status"
        };

        DefaultTableModel modeloInternos =
            new DefaultTableModel(colunas, 0) {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int linha, int coluna) {
                    return false;
                }
            };

        tabelaCursosInternos.setModel(modeloInternos);
        tabelaCursosInternos.setRowHeight(30);
        tabelaCursosInternos.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tabelaCursosInternos.getColumnModel().getColumn(0)
            .setPreferredWidth(250);

        tabelaCursosInternos.getColumnModel().getColumn(1)
            .setPreferredWidth(120);

        tabelaCursosInternos.getColumnModel().getColumn(2)
            .setPreferredWidth(180);

        tabelaCursosInternos.getColumnModel().getColumn(3)
            .setPreferredWidth(150);

        JScrollPane scrollInternos =
            new JScrollPane(tabelaCursosInternos);

        JPanel painelInternos = new JPanel(new BorderLayout());

        painelInternos.setBorder(
            BorderFactory.createTitledBorder("Cursos Internos")
        );

        painelInternos.add(scrollInternos, BorderLayout.CENTER);

        DefaultTableModel modeloExternos =
            new DefaultTableModel(colunas, 0) {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int linha, int coluna) {
                    return false;
                }
            };

        tabelaCursosExternos.setModel(modeloExternos);
        tabelaCursosExternos.setRowHeight(30);
        tabelaCursosExternos.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tabelaCursosExternos.getColumnModel().getColumn(0)
            .setPreferredWidth(250);

        tabelaCursosExternos.getColumnModel().getColumn(1)
            .setPreferredWidth(120);

        tabelaCursosExternos.getColumnModel().getColumn(2)
            .setPreferredWidth(180);

        tabelaCursosExternos.getColumnModel().getColumn(3)
            .setPreferredWidth(150);

        JScrollPane scrollExternos =
            new JScrollPane(tabelaCursosExternos);

        JPanel painelExternos = new JPanel(new BorderLayout());

        painelExternos.setBorder(
            BorderFactory.createTitledBorder("Cursos Externos")
        );

        painelExternos.add(scrollExternos, BorderLayout.CENTER);

        JPanel tabelas = new JPanel(new GridLayout(2, 1, 5, 10));

        tabelas.add(painelInternos);
        tabelas.add(painelExternos);

        JPanel parteSuperior = new JPanel(new BorderLayout());

        parteSuperior.add(selecao, BorderLayout.NORTH);
        parteSuperior.add(resumo, BorderLayout.CENTER);

        JPanel conteudo = new JPanel(new BorderLayout());

        conteudo.add(parteSuperior, BorderLayout.NORTH);
        conteudo.add(tabelas, BorderLayout.CENTER);

        add(conteudo, BorderLayout.CENTER);
    }

    private void componente(
        JPanel painel,
        GridBagConstraints g,
        int linha,
        String texto,
        Component campo
    ) {
        g.gridx = 0;
        g.gridy = linha;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;

        painel.add(new JLabel(texto), g);

        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;

        painel.add(campo, g);
    }

    public JComboBox<String> getArea() {
        return area;
    }

    public JComboBox<String> getFuncionario() {
        return funcionario;
    }

    public JButton getSelecionar() {
        return selecionar;
    }

    public JLabel getHorasRealizadas() {
        return horasRealizadas;
    }

    public JLabel getCursosConcluidos() {
        return cursosConcluidos;
    }

    public JTable getTabelaCursosInternos() {
        return tabelaCursosInternos;
    }

    public JTable getTabelaCursosExternos() {
        return tabelaCursosExternos;
    }
}