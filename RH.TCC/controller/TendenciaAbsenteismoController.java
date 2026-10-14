package controller;

import java.util.*;

import javax.swing.*;

import model.TendenciaAbsenteismo;
import model.FuncionarioAbsenteismo;

import view.TelaTendenciaAbsenteismo;

import dao.TendenciaAbsenteismoDAO;
import dao.FuncionarioAbsenteismoDAO;

public class TendenciaAbsenteismoController {
    private TelaTendenciaAbsenteismo tela;
    private TendenciaAbsenteismoDAO tendenciaDAO;
    private FuncionarioAbsenteismoDAO funcionarioDAO;

    public TendenciaAbsenteismoController(TelaTendenciaAbsenteismo tela) {

        this.tela = tela;
        this.tendenciaDAO = new TendenciaAbsenteismoDAO();
        this.funcionarioDAO = new FuncionarioAbsenteismoDAO();


    }


    public void carregarGrafico(){
        try{
            List<TendenciaAbsenteismo> dados =
            tendenciaDAO.listarTodos();

            tela.atualizarGrafico(dados);


        } catch (Exception e){
            mostrarErro("Erro ao carregar gráfico de absenteísmo.", e);

        }
    };


    public void carregarTabela(){
        try{
            List<FuncionarioAbsenteismo> funcionarios =
            funcionarioDAO.listarTodos();

            tela.atualizarTabela(funcionarios);


        } catch (Exception e){
            mostrarErro("Erro ao carregar tabela de funcionarios.", e);

        }
    };

    public void carregarDados() {
        carregarGrafico();
        carregarTabela();


    }

    public void atualizarDados() {
        if(SwingUtilities.isEventDispatchThread()) {
            carregarDados();
        } else {
            SwingUtilities.invokeLater(this::carregarDados);
        }
    }

    private void mostrarErro(String mensagem, Exception e) {
        JOptionPane.showMessageDialog(tela, mensagem + "\n" + 
        e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }



}
