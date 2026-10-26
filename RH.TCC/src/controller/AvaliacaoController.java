package controller;

import dao.AvaliacaoDAO;
import model.Avaliacao;

public class AvaliacaoController {

    private final AvaliacaoDAO avaliacaoDAO;

    public AvaliacaoController() {
        this.avaliacaoDAO = new AvaliacaoDAO();
    }

    public Avaliacao buscarAvaliacaoPorFuncionario(String nomeFuncionario) {
        if (nomeFuncionario == null || nomeFuncionario.trim().isEmpty()) {
            return null;
        }

        return avaliacaoDAO.buscarAvaliacaoPorFuncionario(nomeFuncionario.trim());
    }
}
