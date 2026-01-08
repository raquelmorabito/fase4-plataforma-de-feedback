package br.com.fiap.report.dto;

public record ReportDTO(String disciplina,
                        Float media_avaliacoes,
                        String email,
                        String nome_professor,
                        Long professor_id) {
}
