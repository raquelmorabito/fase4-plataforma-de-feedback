package br.com.fiap.report;

import br.com.fiap.gh.jpa.entities.FeedbackEntity;
import jakarta.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FeedbackRepository {

    @Inject
    DataSource dataSource;

    public List<ReportDTO> buscarFeedbacks() {

        List<ReportDTO> listaAvaliacoes = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     """
                             select f.disciplina, avg(nota) as media_avaliacoes, u.email, u.nome from feedback f
                             inner join usuario u on u.id = f.usuario_professor
                             group by disciplina, usuario_professor
                             
                             """);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                var dto = new ReportDTO(
                    rs.getString("disciplina"),
                    rs.getFloat("media_avaliacoes"),
                    rs.getString("email"),
                    rs.getString("nome"));

                listaAvaliacoes.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("ENCONTRADOS : "+listaAvaliacoes.size() +" FEEDBACKS");
        return listaAvaliacoes;
    }
}
