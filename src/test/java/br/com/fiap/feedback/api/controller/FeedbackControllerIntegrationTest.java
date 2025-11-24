package br.com.fiap.feedback.api.controller;

import br.com.fiap.feedback.api.dto.FeedbackRequestDTO;
import br.com.fiap.feedback.infrastructure.repository.FeedbackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class FeedbackControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FeedbackRepository feedbackRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void configurarMockMvc() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        feedbackRepository.deleteAll();
    }

    @Test
    void devePermitirCriarFeedbackSemAutenticacao() throws Exception {
        FeedbackRequestDTO dto = new FeedbackRequestDTO();
        dto.setDescricao("Aula de Cloud com teste");
        dto.setNota(9);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/avaliacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.descricao", is("Aula de Cloud com teste")))
                .andExpect(jsonPath("$.nota", is(9)));
    }

    @Test
    void deveRetornar401AoListarSemAutenticacao() throws Exception {
        mockMvc.perform(get("/avaliacao"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar403AoListarComUsuarioAluno() throws Exception {
        FeedbackRequestDTO dto = new FeedbackRequestDTO();
        dto.setDescricao("Aula de Serverless para teste aluno");
        dto.setNota(5);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/avaliacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/avaliacao").with(httpBasic("aluno", "Aluno@123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void devePermitirListarComUsuarioAdmin() throws Exception {
        FeedbackRequestDTO dto = new FeedbackRequestDTO();
        dto.setDescricao("Aula de Quarkus para teste admin");
        dto.setNota(7);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/avaliacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/avaliacao").with(httpBasic("admin", "Admin@123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deveRetornarResumoSemanalParaAdmin() throws Exception {
        FeedbackRequestDTO dto = new FeedbackRequestDTO();
        dto.setDescricao("Aula com foco em resumo semanal");
        dto.setNota(8);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/avaliacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/avaliacao/resumo-semanal").with(httpBasic("admin", "Admin@123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAvaliacoes", is(1)))
                .andExpect(jsonPath("$.mediaNotas", is(8.0)));
    }
}
