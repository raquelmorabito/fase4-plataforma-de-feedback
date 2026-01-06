package br.com.fiap.report;

import br.com.fiap.gh.jpa.entities.FeedbackEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FeedbackRepository {

    @Inject
    DataSource dataSource;

    public List<FeedbackEntity> buscarFeedbacks() {

        List<FeedbackEntity> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id " +
                             " FROM feedback");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FeedbackEntity f = new FeedbackEntity();
                 rs.getLong("id");
//                f.comentario = rs.getString("comentario");
//                f.nota = rs.getInt("nota");
                lista.add(f);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lista;
    }
}
