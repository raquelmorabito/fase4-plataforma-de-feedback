package br.com.fiap.feedback.application.service;

import br.com.fiap.feedback.api.dto.FeedbackRequestDTO;
import br.com.fiap.feedback.api.dto.FeedbackResponseDTO;
import br.com.fiap.feedback.api.dto.ResumoSemanalResponseDTO;
import br.com.fiap.feedback.infrastructure.repository.FeedbackRepository;
import br.com.fiap.gh.jpa.entities.UsuarioEntity;
import br.com.fiap.gh.jpa.enums.EnumPrioridade;
import br.com.fiap.gh.jpa.entities.FeedbackEntity;
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
    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;
    private final UsuarioService usuarioService;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           NotificationService notificationService,
                           AuthenticationService authenticationService, UsuarioService usuarioService) {
        this.feedbackRepository = feedbackRepository;
        this.notificationService = notificationService;
        this.authenticationService = authenticationService;
        this.usuarioService = usuarioService;
    }

    public FeedbackResponseDTO criarFeedback(FeedbackRequestDTO dto) {

        FeedbackEntity feedback = new FeedbackEntity();
        feedback.setDescricao(dto.getDescricao());
        feedback.setNota(dto.getNota());
        feedback.setUrgencia(definirUrgencia(dto.getNota()));
        feedback.setDataEnvio(LocalDateTime.now());
        feedback.setDisciplina(dto.getDisciplina());
        feedback.setProfessor( usuarioService.getUsuarioById( dto.getProfessor()));
        feedback.setAluno( recuperarAlunoLogado() );
        FeedbackEntity feedbackEntity = feedbackRepository.save(feedback);

        sendNotificationToTeacherIfUrgent(feedbackEntity);

        return converterParaDTO(feedbackEntity);
    }

    private UsuarioEntity recuperarAlunoLogado() {

        return authenticationService.getAuthenticatedUser();
    }

    private void sendNotificationToTeacherIfUrgent(FeedbackEntity feedback) {

        if(feedback.getUrgencia().equals(EnumPrioridade.ALTA.name())) {

            notificationService.sendNewNotificationWhenUrgent(feedback);
        }
    }

    public List<FeedbackResponseDTO> listarTodos() {
        return feedbackRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public FeedbackResponseDTO buscarFeedbackPorId( Long idFeedback) {
        var entity = feedbackRepository.findById(idFeedback);

        return entity.map(this::converterParaDTO).orElse(null);
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
                .mapToInt(FeedbackEntity::getNota)
                .average()
                .orElse(0.0);

        long total = feedbacks.size();

        Map<String, Long> quantidadePorUrgencia = feedbacks.stream()
                .collect(Collectors.groupingBy(FeedbackEntity::getUrgencia, Collectors.counting()));

        Map<LocalDate, Long> quantidadePorDia = feedbacks.stream()
                .collect(Collectors.groupingBy(f -> f.getDataEnvio().toLocalDate(), Collectors.counting()));

        resumo.setMediaNotas(media);
        resumo.setTotalAvaliacoes(total);
        resumo.setQuantidadePorUrgencia(quantidadePorUrgencia);
        resumo.setQuantidadePorDia(quantidadePorDia);

        return resumo;
    }

    private String definirUrgencia(Integer nota) {
        if (nota <= 3)
            return EnumPrioridade.ALTA.name();
        if ( nota <= 7)
            return EnumPrioridade.MEDIA.name();

        return EnumPrioridade.BAIXA.name();
    }

    private FeedbackResponseDTO converterParaDTO(FeedbackEntity feedback) {
        FeedbackResponseDTO resposta = new FeedbackResponseDTO();
        resposta.setId(feedback.getId());
        resposta.setDisciplina(feedback.getDisciplina());
        resposta.setDescricao(feedback.getDescricao());
        resposta.setNota(feedback.getNota());
        resposta.setUrgencia(feedback.getUrgencia());
        resposta.setDataEnvio(feedback.getDataEnvio());

        if(feedback.getAluno() != null)
            resposta.setAluno(feedback.getAluno().getNome());

        if(feedback.getProfessor() != null)
            resposta.setProfessor(feedback.getProfessor().getNome());
        return resposta;
    }

    public void deletarFeedback(Long feedbackId) {

        feedbackRepository.deleteById(feedbackId);
    }
}

