package br.com.fiap.feedback.api.controller;

import br.com.fiap.feedback.api.doc.FeedbackDocController;
import br.com.fiap.feedback.api.dto.FeedbackRequestDTO;
import br.com.fiap.feedback.api.dto.FeedbackResponseDTO;
import br.com.fiap.feedback.application.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import br.com.fiap.feedback.api.dto.ResumoSemanalResponseDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/avaliacao")
@PreAuthorize("hasRole('FEEDBACK')")
public class FeedbackController implements FeedbackDocController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @PreAuthorize("hasRole('VIEW_FEEDBACK')")
    public ResponseEntity<FeedbackResponseDTO> criar(@Valid @RequestBody FeedbackRequestDTO dto) {
        FeedbackResponseDTO resposta = feedbackService.criarFeedback(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<List<FeedbackResponseDTO>> listarTodos() {

        return ResponseEntity.ok(feedbackService.listarTodos());
    }

    @GetMapping("/por-urgencia")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<List<FeedbackResponseDTO>> listarPorUrgencia(@RequestParam("tipo") String tipo) {

        return ResponseEntity.ok(feedbackService.listarPorUrgencia(tipo.toUpperCase()));
    }


    @GetMapping("/por-periodo")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<List<FeedbackResponseDTO>> listarPorPeriodo(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return ResponseEntity.ok(feedbackService.listarPorPeriodo(dataInicio, dataFim));
    }
    
    @GetMapping("/resumo-semanal")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<ResumoSemanalResponseDTO> resumoSemanal() {
        ResumoSemanalResponseDTO resumo = feedbackService.gerarResumoSemanal();
        return ResponseEntity.ok(resumo);
    }

    @DeleteMapping("/{feedbackId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletar(@PathVariable(required = true) Long feedbackId) {

        feedbackService.deletarFeedback(feedbackId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Avaliação deletada com sucesso.");
    }

    @GetMapping("/{feedbackId}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<FeedbackResponseDTO> buscarAvalicaoPorId(@PathVariable(required = true) Long feedbackId) {

        var response = feedbackService.buscarFeedbackPorId(feedbackId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
