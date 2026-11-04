package com.example.demo.arielado3.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.arielado3.dto.ProdutoDTO;
import com.example.demo.arielado3.model.Categoria;
import com.example.demo.arielado3.model.Produto;
import com.example.demo.arielado3.repository.CategoriaRepository;
import com.example.demo.arielado3.repository.ProdutoRepository;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listar() {
        return produtoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscar(Long id) {
        return toDTO(encontrar(id));
    }

    public ProdutoDTO criar(ProdutoDTO dto) {
        Categoria categoria = encontrarCategoria(dto.categoriaId());
        Produto produto = new Produto(dto.nome(), dto.preco(), categoria);
        return toDTO(produtoRepository.save(produto));
    }

    public ProdutoDTO atualizar(Long id, ProdutoDTO dto) {
        Produto produto = encontrar(id);
        produto.setNome(dto.nome());
        produto.setPreco(dto.preco());
        produto.setCategoria(encontrarCategoria(dto.categoriaId()));
        return toDTO(produtoRepository.save(produto));
    }

    public void excluir(Long id) {
        produtoRepository.delete(encontrar(id));
    }

    private Produto encontrar(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    private Categoria encontrarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    private ProdutoDTO toDTO(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getCategoria().getId(),
                produto.getCategoria().getNome());
    }
}
