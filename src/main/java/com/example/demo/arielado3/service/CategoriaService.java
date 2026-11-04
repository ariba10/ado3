package com.example.demo.arielado3.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.arielado3.dto.CategoriaDTO;
import com.example.demo.arielado3.model.Categoria;
import com.example.demo.arielado3.repository.CategoriaRepository;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDTO buscar(Long id) {
        return toDTO(encontrar(id));
    }

    public CategoriaDTO criar(CategoriaDTO dto) {
        return toDTO(repository.save(new Categoria(dto.nome())));
    }

    public CategoriaDTO atualizar(Long id, CategoriaDTO dto) {
        Categoria categoria = encontrar(id);
        categoria.setNome(dto.nome());
        return toDTO(repository.save(categoria));
    }

    public void excluir(Long id) {
        repository.delete(encontrar(id));
    }

    private Categoria encontrar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    private CategoriaDTO toDTO(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNome());
    }
}
