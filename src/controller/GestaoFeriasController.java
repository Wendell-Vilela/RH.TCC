package controller;

import java.util.*;

import javax.swing.*;

import model.GestaoFerias;

import view.TelaGestaoFerias;

import dao.GestaoFeriasDAO;

public class GestaoFeriasController {
    private TelaGestaoFerias tela;
    private GestaoFeriasDAO dao;

    public GestaoFeriasController(TelaGestaoFerias tela) {
        this.tela = tela;
        this.dao = new GestaoFeriasDAO();
    }


    public void carregarDados() {

        try {
            List<GestaoFerias> lista = dao.ListarTodos();
            tela.AtualizarTabela(lista);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                tela, "Erro ao carregar dados de Gestão de Férias: "
                + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE
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