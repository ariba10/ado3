package com.example.demo.arielado3.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
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
class ProdutoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveExecutarCicloDeVidaDoProdutoComCategoria() throws Exception {
        String categoria = mockMvc.perform(post("/api/categorias")
                        .with(httpBasic("test", "test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Eletrônicos"}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long categoriaId = objectMapper.readTree(categoria).get("id").asLong();

        String produto = mockMvc.perform(post("/api/produtos")
                        .with(httpBasic("test", "test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Fone","preco":199.90,"categoriaId":%d}
                                """.formatted(categoriaId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Fone"))
                .andExpect(jsonPath("$.categoriaNome").value("Eletrônicos"))
                .andReturn().getResponse().getContentAsString();
        long produtoId = objectMapper.readTree(produto).get("id").asLong();

        mockMvc.perform(get("/api/produtos/{id}", produtoId).with(httpBasic("test", "test")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preco").value(199.90));

        mockMvc.perform(put("/api/produtos/{id}", produtoId)
                        .with(httpBasic("test", "test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Fone sem fio","preco":249.90,"categoriaId":%d}
                                """.formatted(categoriaId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fone sem fio"));

        mockMvc.perform(get("/api/produtos").with(httpBasic("test", "test")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoriaId").value(categoriaId));

        mockMvc.perform(delete("/api/produtos/{id}", produtoId).with(httpBasic("test", "test")))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRejeitarPrecoInvalidoECategoriaInexistente() throws Exception {
        mockMvc.perform(post("/api/produtos")
                        .with(httpBasic("test", "test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Fone","preco":0,"categoriaId":1}
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/produtos")
                        .with(httpBasic("test", "test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Fone","preco":10,"categoriaId":999999}
                                """))
                .andExpect(status().isNotFound());
    }
}
