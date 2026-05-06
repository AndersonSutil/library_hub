package com.sicredi.libraryhub.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.testcontainers.RedisContainer;
import com.sicredi.libraryhub.application.dto.LivroRequestDTO;
import com.sicredi.libraryhub.domain.GeneroPojo;
import com.sicredi.libraryhub.domain.Livro;
import com.sicredi.libraryhub.infrastructure.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class LivroControllerIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.4");

    @Container
    static RedisContainer redisContainer = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag("7.0.8"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LivroRepository livroRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379).toString());
    }

    @BeforeEach
    void setUp() {
        livroRepository.deleteAll();
    }

    @Test
    void deveCriarLivroComSucesso() throws Exception {
        LivroRequestDTO request = new LivroRequestDTO("Titulo Teste", "Autor Teste", "9999", 2023, GeneroPojo.TECNOLOGIA, true);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Titulo Teste"))
                .andExpect(jsonPath("$.isbn").value("9999"));
    }

    @Test
    void deveRetornarErro400QuandoDadosInvalidos() throws Exception {
        LivroRequestDTO request = new LivroRequestDTO("", "", "", 500, null, null);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("DADOS_INVALIDOS"));
    }

    @Test
    void deveBuscarLivroPorId() throws Exception {
        Livro livro = Livro.builder().titulo("Test").autor("Auth").isbn("123").anoPublicacao(2020).genero(GeneroPojo.HISTORIA).disponivel(true).build();
        livro = livroRepository.save(livro);

        mockMvc.perform(get("/livros/{id}", livro.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Test"));
    }

    @Test
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        mockMvc.perform(get("/livros/{id}", "id_inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("LIVRO_NAO_ENCONTRADO"));
    }

    @Test
    void deveDeletarLivroComSucesso() throws Exception {
        Livro livro = Livro.builder().titulo("Test").autor("Auth").isbn("123").anoPublicacao(2020).genero(GeneroPojo.HISTORIA).disponivel(true).build();
        livro = livroRepository.save(livro);

        mockMvc.perform(delete("/livros/{id}", livro.getId()))
                .andExpect(status().isNoContent());
    }
}
