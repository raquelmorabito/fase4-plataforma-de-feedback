package br.com.fiap.feedback.application.service;

import br.com.fiap.feedback.api.dto.FeedbackRequestDTO;
import br.com.fiap.feedback.api.dto.FeedbackResponseDTO;
import br.com.fiap.feedback.domain.Feedback;
import br.com.fiap.feedback.infrastructure.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        return converterParaDTO(salvo);
    }

    public List<FeedbackResponseDTO> listarTodos() {
        return feedbackRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<FeedbackResponseDTO> listarPorUrgencia(String urgencia) {
        return feedbackRepository.findByUrgencia(urgencia)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<FeedbackResponseDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);
        return feedbackRepository.findByDataEnvioBetween(inicio, fim)
                .stream()
                .map(this::converterParaDTO)
                .toList();
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

    private FeedbackResponseDTO converterParaDTO(Feedback feedback) {
        FeedbackResponseDTO resposta = new FeedbackResponseDTO();
        resposta.setId(feedback.getId());
        resposta.setDescricao(feedback.getDescricao());
        resposta.setNota(feedback.getNota());
        resposta.setUrgencia(feedback.getUrgencia());
        resposta.setDataEnvio(feedback.getDataEnvio());
        return resposta;
    }
}

