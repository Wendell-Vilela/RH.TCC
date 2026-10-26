package controller;

import dao.CursosTrilhasDAO;
import java.util.List;
import model.CursosTrilhas;

public class CursosTrilhasController {

    private final CursosTrilhasDAO cursosTrilhasDAO;

    public CursosTrilhasController() {
        this.cursosTrilhasDAO = new CursosTrilhasDAO();
    }

    public List<CursosTrilhas> listarCursosPorFuncionario(String nomeFuncionario) {
        if (nomeFuncionario == null || nomeFuncionario.trim().isEmpty()) {
            throw new IllegalArgumentException("Selecione um funcionário.");
        }

        return cursosTrilhasDAO.listarCursosPorFuncionario(nomeFuncionario.trim());
    }
}
