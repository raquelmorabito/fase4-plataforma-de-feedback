package br.com.fiap.feedback.api.dto;

import java.time.LocalDate;
import java.util.Map;

public class ResumoSemanalResponseDTO {

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Double mediaNotas;
    private Long totalAvaliacoes;
    private Map<String, Long> quantidadePorUrgencia;
    private Map<LocalDate, Long> quantidadePorDia;

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Double getMediaNotas() {
        return mediaNotas;
    }

    public void setMediaNotas(Double mediaNotas) {
        this.mediaNotas = mediaNotas;
    }

    public Long getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(Long totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }

    public Map<String, Long> getQuantidadePorUrgencia() {
        return quantidadePorUrgencia;
    }

    public void setQuantidadePorUrgencia(Map<String, Long> quantidadePorUrgencia) {
        this.quantidadePorUrgencia = quantidadePorUrgencia;
    }

    public Map<LocalDate, Long> getQuantidadePorDia() {
        return quantidadePorDia;
    }

    public void setQuantidadePorDia(Map<LocalDate, Long> quantidadePorDia) {
        this.quantidadePorDia = quantidadePorDia;
    }
}
