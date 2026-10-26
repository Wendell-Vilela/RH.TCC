package controller;

import dao.FeedbackDAO;
import java.util.List;
import model.Feedback;

public class FeedbackController {

    private final FeedbackDAO feedbackDAO;

    public FeedbackController() {
        this.feedbackDAO = new FeedbackDAO();
    }

    public void cadastrarFeedback(Feedback feedback, int idFuncionario, int idAvaliador) {
        if (feedback == null) {
            throw new IllegalArgumentException("Os dados do feedback não foram informados.");
        }
        if (feedback.getCompetencia() == null || feedback.getCompetencia().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a competência do feedback.");
        }
        if (feedback.getFeedback() == null || feedback.getFeedback().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a descrição do feedback.");
        }
        if (idFuncionario <= 0 || idAvaliador <= 0) {
            throw new IllegalArgumentException("Os IDs do funcionário e do avaliador devem ser válidos.");
        }

        feedbackDAO.inserir(feedback, idFuncionario, idAvaliador);
    }

    public void excluirFeedback(String idFeedback) {
        if (idFeedback == null || idFeedback.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o ID do feedback.");
        }

        try {
            int id = Integer.parseInt(idFeedback.trim());
            if (id <= 0) {
                throw new IllegalArgumentException("O ID do feedback deve ser maior que zero.");
            }
            feedbackDAO.excluir(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O ID do feedback deve ser numérico.");
        }
    }

    public List<Feedback> listarFeedbacks() {
        return feedbackDAO.listarFeedbacks();
    }
}
