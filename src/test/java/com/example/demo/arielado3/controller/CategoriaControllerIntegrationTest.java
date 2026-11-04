package com.example.demo.arielado3.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.security.user.name=test",
        "spring.security.user.password=test"
})
@Transactional
class CategoriaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveExecutarCicloDeVidaDaCategoria() throws Exception {
        String categoria = mockMvc.perform(post("/api/categorias")
                        .with(httpBasic("test", "test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Livros"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Livros"))
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(categoria).get("id").asLong();

        mockMvc.perform(get("/api/categorias/{id}", id).with(httpBasic("test", "test")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        mockMvc.perform(put("/api/categorias/{id}", id)
                        .with(httpBasic("test", "test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Literatura"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Literatura"));

        mockMvc.perform(get("/api/categorias").with(httpBasic("test", "test")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.nome == 'Literatura')]").isNotEmpty());

        mockMvc.perform(delete("/api/categorias/{id}", id).with(httpBasic("test", "test")))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRejeitarNomeEmBrancoENegarAcessoAnonimo() throws Exception {
        mockMvc.perform(post("/api/categorias")
                        .with(httpBasic("test", "test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":" "}
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isUnauthorized());
    }
}
