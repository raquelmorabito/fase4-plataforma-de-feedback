package br.com.fiap.feedback.application.service;

import br.com.fiap.feedback.api.dto.FeedbackRequestDTO;
import br.com.fiap.feedback.api.dto.FeedbackResponseDTO;
import br.com.fiap.feedback.domain.Feedback;
import br.com.fiap.feedback.infrastructure.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public FeedbackResponseDTO criarFeedback(FeedbackRequestDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setDescricao(dto.getDescricao());
        feedback.setNota(dto.getNota());
        feedback.setUrgencia(definirUrgencia(dto.getNota()));
        feedback.setDataEnvio(LocalDateTime.now());

        Feedback salvo = feedbackRepository.save(feedback);

        FeedbackResponseDTO resposta = new FeedbackResponseDTO();
        resposta.setId(salvo.getId());
        resposta.setDescricao(salvo.getDescricao());
        resposta.setNota(salvo.getNota());
        resposta.setUrgencia(salvo.getUrgencia());
        resposta.setDataEnvio(salvo.getDataEnvio());

        return resposta;
    }

    private String definirUrgencia(Integer nota) {
        if (nota >= 0 && nota <= 3) {
            return "ALTA";
        }
        if (nota >= 4 && nota <= 7) {
            return "MEDIA";
        }
        return "BAIXA";
    }
}
