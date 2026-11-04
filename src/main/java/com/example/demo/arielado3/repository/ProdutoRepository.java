package com.example.demo.arielado3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.arielado3.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
