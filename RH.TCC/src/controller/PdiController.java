package controller;

import dao.PdiDAO;
import java.util.List;
import model.PDI;

public class PdiController {

    private final PdiDAO pdiDAO;

    public PdiController() {
        this.pdiDAO = new PdiDAO();
    }

    public List<PDI> listarPdiPorFuncionario(String nomeFuncionario) {
        if (nomeFuncionario == null || nomeFuncionario.trim().isEmpty()) {
            throw new IllegalArgumentException("Selecione um funcionário.");
        }

        return pdiDAO.listarPdiPorFuncionario(nomeFuncionario.trim());
    }
}
