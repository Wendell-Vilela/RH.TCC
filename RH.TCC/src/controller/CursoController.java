package controller;

import dao.CursoDAO;

public class CursoController {

    private final CursoDAO cursoDAO;

    public CursoController() {
        this.cursoDAO = new CursoDAO();
    }

    public void cadastrarCurso(String nomeCurso, String cargaHoraria, String fornecedor, String tipoCurso) {
        if (nomeCurso == null || nomeCurso.trim().isEmpty()
                || cargaHoraria == null || cargaHoraria.trim().isEmpty()
                || fornecedor == null || fornecedor.trim().isEmpty()
                || tipoCurso == null || tipoCurso.trim().isEmpty()) {
            throw new IllegalArgumentException("Preencha todos os campos do curso.");
        }

        int carga;
        try {
            carga = Integer.parseInt(cargaHoraria.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("A carga horária deve ser um número inteiro.");
        }

        if (carga <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser maior que zero.");
        }

        cursoDAO.inserir(nomeCurso.trim(), carga, fornecedor.trim(), tipoCurso.trim());
    }

    public void excluirCurso(String idCurso) {
        if (idCurso == null || idCurso.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o ID do curso.");
        }

        try {
            int id = Integer.parseInt(idCurso.trim());
            if (id <= 0) {
                throw new IllegalArgumentException("O ID do curso deve ser maior que zero.");
            }
            cursoDAO.excluir(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O ID do curso deve ser numérico.");
        }
    }
}
