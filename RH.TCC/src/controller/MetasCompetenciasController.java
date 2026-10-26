package controller;

import dao.MetasCompetenciasDAO;
import java.util.List;
import model.MetasCompetencias;

public class MetasCompetenciasController {

    private final MetasCompetenciasDAO metasDAO;

    public MetasCompetenciasController() {
        this.metasDAO = new MetasCompetenciasDAO();
    }

    public void cadastrarMeta(MetasCompetencias meta, int idFuncionario) {
        if (meta == null) {
            throw new IllegalArgumentException("Os dados da meta não foram informados.");
        }
        if (meta.getMeta() == null || meta.getMeta().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a descrição da meta.");
        }
        if (meta.getPrazo() == null || meta.getPrazo().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o prazo da meta.");
        }
        if (meta.getStatus() == null || meta.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Selecione o status da meta.");
        }
        if (idFuncionario <= 0) {
            throw new IllegalArgumentException("Selecione um funcionário válido.");
        }

        metasDAO.inserir(meta, idFuncionario);
    }

    public void excluirMeta(String idMeta) {
        if (idMeta == null || idMeta.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o ID da meta.");
        }

        try {
            int id = Integer.parseInt(idMeta.trim());
            if (id <= 0) {
                throw new IllegalArgumentException("O ID da meta deve ser maior que zero.");
            }
            metasDAO.excluir(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O ID da meta deve ser numérico.");
        }
    }

    public List<MetasCompetencias> listarMetas() {
        return metasDAO.listarMetas();
    }
}
