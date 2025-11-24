package br.com.fiap.feedback.application.service;

import br.com.fiap.feedback.api.dto.FeedbackRequestDTO;
import br.com.fiap.feedback.api.dto.FeedbackResponseDTO;
import br.com.fiap.feedback.api.dto.ResumoSemanalResponseDTO;
import br.com.fiap.feedback.domain.Feedback;
import br.com.fiap.feedback.infrastructure.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @Test
    void deveDefinirUrgenciaBaixaParaNotaAlta() {
        FeedbackRequestDTO dto = new FeedbackRequestDTO();
        dto.setDescricao("Aula excelente");
        dto.setNota(9);

        Feedback feedbackSalvo = new Feedback();
        feedbackSalvo.setId(1L);
        feedbackSalvo.setDescricao(dto.getDescricao());
        feedbackSalvo.setNota(dto.getNota());
        feedbackSalvo.setUrgencia("BAIXA");
        feedbackSalvo.setDataEnvio(LocalDateTime.now());

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedbackSalvo);

        FeedbackResponseDTO resposta = feedbackService.criarFeedback(dto);

        ArgumentCaptor<Feedback> captor = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackRepository, times(1)).save(captor.capture());
        Feedback enviado = captor.getValue();

        assertThat(enviado.getUrgencia()).isEqualTo("BAIXA");
        assertThat(enviado.getDataEnvio()).isNotNull();

        assertThat(resposta.getId()).isEqualTo(1L);
        assertThat(resposta.getUrgencia()).isEqualTo("BAIXA");
        assertThat(resposta.getDescricao()).isEqualTo("Aula excelente");
        assertThat(resposta.getNota()).isEqualTo(9);
    }

    @Test
    void deveDefinirUrgenciaAltaParaNotaBaixa() {
        FeedbackRequestDTO dto = new FeedbackRequestDTO();
        dto.setDescricao("Aula confusa");
        dto.setNota(2);

        Feedback feedbackSalvo = new Feedback();
        feedbackSalvo.setId(2L);
        feedbackSalvo.setDescricao(dto.getDescricao());
        feedbackSalvo.setNota(dto.getNota());
        feedbackSalvo.setUrgencia("ALTA");
        feedbackSalvo.setDataEnvio(LocalDateTime.now());

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedbackSalvo);

        FeedbackResponseDTO resposta = feedbackService.criarFeedback(dto);

        ArgumentCaptor<Feedback> captor = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackRepository, times(1)).save(captor.capture());
        Feedback enviado = captor.getValue();

        assertThat(enviado.getUrgencia()).isEqualTo("ALTA");
        assertThat(resposta.getUrgencia()).isEqualTo("ALTA");
    }

    @Test
    void deveGerarResumoSemanalComMediaEContagensCorretas() {
        LocalDate hoje = LocalDate.now();

        Feedback f1 = new Feedback();
        f1.setId(1L);
        f1.setDescricao("Aula de Cloud excelente");
        f1.setNota(9);
        f1.setUrgencia("BAIXA");
        f1.setDataEnvio(hoje.atTime(10, 0));

        Feedback f2 = new Feedback();
        f2.setId(2L);
        f2.setDescricao("Aula de Serverless confusa");
        f2.setNota(3);
        f2.setUrgencia("ALTA");
        f2.setDataEnvio(hoje.minusDays(1).atTime(14, 0));

        Feedback f3 = new Feedback();
        f3.setId(3L);
        f3.setDescricao("Aula de Quarkus razoável");
        f3.setNota(6);
        f3.setUrgencia("MEDIA");
        f3.setDataEnvio(hoje.minusDays(2).atTime(9, 30));

        when(feedbackRepository.findByDataEnvioBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(f1, f2, f3));

        ResumoSemanalResponseDTO resumo = feedbackService.gerarResumoSemanal();

        assertThat(resumo.getTotalAvaliacoes()).isEqualTo(3L);
        assertThat(resumo.getMediaNotas()).isEqualTo(6.0);
        assertThat(resumo.getQuantidadePorUrgencia()).containsEntry("BAIXA", 1L);
        assertThat(resumo.getQuantidadePorUrgencia()).containsEntry("ALTA", 1L);
        assertThat(resumo.getQuantidadePorUrgencia()).containsEntry("MEDIA", 1L);

        Map<LocalDate, Long> quantidadePorDia = resumo.getQuantidadePorDia();
        assertThat(quantidadePorDia.values().stream().mapToLong(Long::longValue).sum()).isEqualTo(3L);
    }

    @Test
    void deveRetornarResumoVazioQuandoNaoHaFeedbacksNaSemana() {
        when(feedbackRepository.findByDataEnvioBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        ResumoSemanalResponseDTO resumo = feedbackService.gerarResumoSemanal();

        assertThat(resumo.getTotalAvaliacoes()).isEqualTo(0L);
        assertThat(resumo.getMediaNotas()).isEqualTo(0.0);
        assertThat(resumo.getQuantidadePorUrgencia()).isEmpty();
        assertThat(resumo.getQuantidadePorDia()).isEmpty();
    }
}
