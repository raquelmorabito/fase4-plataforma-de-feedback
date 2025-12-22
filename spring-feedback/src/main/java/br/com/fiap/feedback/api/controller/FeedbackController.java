package br.com.fiap.feedback.api.controller;

import br.com.fiap.feedback.api.dto.FeedbackRequestDTO;
import br.com.fiap.feedback.api.dto.FeedbackResponseDTO;
import br.com.fiap.feedback.application.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.fiap.feedback.api.dto.ResumoSemanalResponseDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/avaliacao")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<FeedbackResponseDTO> criar(@Valid @RequestBody FeedbackRequestDTO dto) {
        FeedbackResponseDTO resposta = feedbackService.criarFeedback(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponseDTO>> listarTodos() {
        return ResponseEntity.ok(feedbackService.listarTodos());
    }

    @GetMapping("/por-urgencia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FeedbackResponseDTO>> listarPorUrgencia(@RequestParam("tipo") String tipo) {
        return ResponseEntity.ok(feedbackService.listarPorUrgencia(tipo.toUpperCase()));
    }


    @GetMapping("/por-periodo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FeedbackResponseDTO>> listarPorPeriodo(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return ResponseEntity.ok(feedbackService.listarPorPeriodo(dataInicio, dataFim));
    }
    
    @GetMapping("/resumo-semanal")
//    @PreAuthorize("hasRole('CONSULTA')")
    public ResponseEntity<ResumoSemanalResponseDTO> resumoSemanal() {
        ResumoSemanalResponseDTO resumo = feedbackService.gerarResumoSemanal();
        return ResponseEntity.ok(resumo);
    }

}
