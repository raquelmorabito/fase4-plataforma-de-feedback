package br.com.fiap.feedback.infrastructure.repository;

import br.com.fiap.feedback.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
