package controller;

public class ProximasAcoesController {

    public ProximasAcoesController() {
    }

    public boolean validarAcao(String descricao, String prazo, String status) {
        return descricao != null && !descricao.trim().isEmpty()
                && prazo != null && !prazo.trim().isEmpty()
                && status != null && !status.trim().isEmpty();
    }
}
