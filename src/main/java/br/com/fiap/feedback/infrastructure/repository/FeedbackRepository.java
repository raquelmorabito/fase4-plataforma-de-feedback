package br.com.fiap.feedback.infrastructure.repository;

import br.com.fiap.feedback.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByUrgencia(String urgencia);

    List<Feedback> findByDataEnvioBetween(LocalDateTime inicio, LocalDateTime fim);
}
