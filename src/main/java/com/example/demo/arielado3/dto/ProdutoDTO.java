package com.example.demo.arielado3.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProdutoDTO(
        Long id,
        @NotBlank @Size(max = 150) String nome,
        @NotNull @Positive BigDecimal preco,
        @NotNull @Positive Long categoriaId,
        String categoriaNome) {
}
