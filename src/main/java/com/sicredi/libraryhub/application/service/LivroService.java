package com.sicredi.libraryhub.application.service;

import com.sicredi.libraryhub.application.dto.LivroPatchRequestDTO;
import com.sicredi.libraryhub.application.dto.LivroRequestDTO;
import com.sicredi.libraryhub.application.dto.LivroResponseDTO;
import com.sicredi.libraryhub.application.exception.ErroConstants;
import com.sicredi.libraryhub.application.exception.NegocioException;
import com.sicredi.libraryhub.domain.GeneroPojo;
import com.sicredi.libraryhub.domain.Livro;
import com.sicredi.libraryhub.infrastructure.config.CacheConfig;
import com.sicredi.libraryhub.infrastructure.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroResponseDTO criar(LivroRequestDTO request) {
        log.info("Criando novo livro com ISBN: {}", request.isbn());

        livroRepository.findByIsbn(request.isbn()).ifPresent(livro -> {
            throw new NegocioException(ErroConstants.ISBN_DUPLICADO, "Já existe um livro cadastrado com o ISBN " + request.isbn());
        });

        Livro livro = mapToEntity(request);
        Livro livroSalvo = livroRepository.save(livro);
        return mapToResponse(livroSalvo);
    }

    @Cacheable(value = CacheConfig.CACHE_LIVRO, key = "#id")
    public LivroResponseDTO buscarPorId(String id) {
        log.info("Buscando livro por ID: {}", id);
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new NegocioException(ErroConstants.LIVRO_NAO_ENCONTRADO, "Livro com id '" + id + "' não encontrado."));
        return mapToResponse(livro);
    }

    public Page<LivroResponseDTO> listar(int pagina, int tamanho, GeneroPojo genero) {
        log.info("Listando livros. Página: {}, Tamanho: {}, Gênero: {}", pagina, tamanho, genero);
        Pageable pageable = PageRequest.of(pagina, tamanho);

        Page<Livro> livrosPage;
        if (genero != null) {
            livrosPage = livroRepository.findByGenero(genero, pageable);
        } else {
            livrosPage = livroRepository.findAll(pageable);
        }

        return livrosPage.map(this::mapToResponse);
    }

    @CacheEvict(value = CacheConfig.CACHE_LIVRO, key = "#id")
    public LivroResponseDTO atualizar(String id, LivroRequestDTO request) {
        log.info("Atualizando livro com ID: {}", id);

        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new NegocioException(ErroConstants.LIVRO_NAO_ENCONTRADO, "Livro com id '" + id + "' não encontrado."));

        livroRepository.findByIsbn(request.isbn()).ifPresent(livroComMesmoIsbn -> {
            if (!livroComMesmoIsbn.getId().equals(id)) {
                throw new NegocioException(ErroConstants.ISBN_DUPLICADO, "Já existe um livro cadastrado com o ISBN " + request.isbn());
            }
        });

        updateEntityFromRequest(request, livroExistente);

        Livro livroAtualizado = livroRepository.save(livroExistente);
        return mapToResponse(livroAtualizado);
    }

    @CacheEvict(value = CacheConfig.CACHE_LIVRO, key = "#id")
    public LivroResponseDTO patch(String id, LivroPatchRequestDTO request) {
        log.info("Aplicando patch no livro com ID: {}", id);

        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new NegocioException(ErroConstants.LIVRO_NAO_ENCONTRADO, "Livro com id '" + id + "' não encontrado."));

        if (request.isbn() != null) {
            livroRepository.findByIsbn(request.isbn()).ifPresent(livroComMesmoIsbn -> {
                if (!livroComMesmoIsbn.getId().equals(id)) {
                    throw new NegocioException(ErroConstants.ISBN_DUPLICADO, "Já existe um livro cadastrado com o ISBN " + request.isbn());
                }
            });
            livroExistente.setIsbn(request.isbn());
        }

        if (request.titulo() != null) livroExistente.setTitulo(request.titulo());
        if (request.autor() != null) livroExistente.setAutor(request.autor());
        if (request.anoPublicacao() != null) livroExistente.setAnoPublicacao(request.anoPublicacao());
        if (request.genero() != null) livroExistente.setGenero(request.genero());
        if (request.disponivel() != null) livroExistente.setDisponivel(request.disponivel());

        Livro livroAtualizado = livroRepository.save(livroExistente);
        return mapToResponse(livroAtualizado);
    }

    @CacheEvict(value = CacheConfig.CACHE_LIVRO, key = "#id")
    public void excluir(String id) {
        log.info("Excluindo livro com ID: {}", id);
        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new NegocioException(ErroConstants.LIVRO_NAO_ENCONTRADO, "Livro com id '" + id + "' não encontrado."));
        livroRepository.delete(livroExistente);
    }

    private Livro mapToEntity(LivroRequestDTO request) {
        Livro livro = new Livro();
        updateEntityFromRequest(request, livro);
        return livro;
    }

    private void updateEntityFromRequest(LivroRequestDTO request, Livro entity) {
        entity.setTitulo(request.titulo());
        entity.setAutor(request.autor());
        entity.setIsbn(request.isbn());
        entity.setAnoPublicacao(request.anoPublicacao());
        entity.setGenero(request.genero());
        entity.setDisponivel(request.disponivel());
    }

    private LivroResponseDTO mapToResponse(Livro entity) {
        return new LivroResponseDTO(
                entity.getId(),
                entity.getTitulo(),
                entity.getAutor(),
                entity.getIsbn(),
                entity.getAnoPublicacao(),
                entity.getGenero(),
                entity.getDisponivel(),
                entity.getDataInclusao(),
                entity.getDataAtualizacao()
        );
    }
}
