package br.com.fiap.feedback.api.doc;
import br.com.fiap.feedback.api.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Feedbacks", description = "Operações relacionadas ao recebimento e gestão das avaliações")
public interface FeedbackDocController {

    @Operation(description = "Cria um novo feedback para o usuário que está postando.")
    public ResponseEntity<FeedbackResponseDTO> criar(FeedbackRequestDTO dto);

    @Operation(description = "Lista todos os feedbacks criados, sem filtro.")
    public ResponseEntity<List<FeedbackResponseDTO>> listarTodos();

    @Operation(description = "Lista todos os feedbacks criados, conforme a urgência.")
    public ResponseEntity<List<FeedbackResponseDTO>> listarPorUrgencia(String tipo);

    @Operation(description = "Lista todos os feedbacks durante o período informado.")
    public ResponseEntity<List<FeedbackResponseDTO>> listarPorPeriodo( LocalDate dataInicio, LocalDate dataFim);

    @Operation(description = "Lista um resumo dos últimos 7 dias de comentários.")
    public ResponseEntity<ResumoSemanalResponseDTO> resumoSemanal();

    @Operation(description = "Deleta um feedback conforme  o ID passado.")
    public ResponseEntity<String> deletar(Long feedbackId);

    @Operation(description = "Buscar um feedback conforme  o ID passado.")
    public ResponseEntity<FeedbackResponseDTO> buscarAvalicaoPorId(Long feedbackId);

}
