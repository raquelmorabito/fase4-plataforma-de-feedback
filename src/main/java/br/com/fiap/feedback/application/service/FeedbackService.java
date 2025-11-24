package br.com.fiap.feedback.application.service;

import br.com.fiap.feedback.api.dto.FeedbackRequestDTO;
import br.com.fiap.feedback.api.dto.FeedbackResponseDTO;
import br.com.fiap.feedback.api.dto.ResumoSemanalResponseDTO;
import br.com.fiap.feedback.domain.Feedback;
import br.com.fiap.feedback.infrastructure.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResumoSemanalResponseDTO gerarResumoSemanal() {
        LocalDate dataFim = LocalDate.now();
        LocalDate dataInicio = dataFim.minusDays(6);

        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        var feedbacks = feedbackRepository.findByDataEnvioBetween(inicio, fim);

        ResumoSemanalResponseDTO resumo = new ResumoSemanalResponseDTO();
        resumo.setDataInicio(dataInicio);
        resumo.setDataFim(dataFim);

        if (feedbacks.isEmpty()) {
            resumo.setMediaNotas(0.0);
            resumo.setTotalAvaliacoes(0L);
            resumo.setQuantidadePorUrgencia(Map.of());
            resumo.setQuantidadePorDia(Map.of());
            return resumo;
        }

        double media = feedbacks.stream()
                .mapToInt(Feedback::getNota)
                .average()
                .orElse(0.0);

        long total = feedbacks.size();

        Map<String, Long> quantidadePorUrgencia = feedbacks.stream()
                .collect(Collectors.groupingBy(Feedback::getUrgencia, Collectors.counting()));

        Map<LocalDate, Long> quantidadePorDia = feedbacks.stream()
                .collect(Collectors.groupingBy(f -> f.getDataEnvio().toLocalDate(), Collectors.counting()));

        resumo.setMediaNotas(media);
        resumo.setTotalAvaliacoes(total);
        resumo.setQuantidadePorUrgencia(quantidadePorUrgencia);
        resumo.setQuantidadePorDia(quantidadePorDia);

        return resumo;
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

