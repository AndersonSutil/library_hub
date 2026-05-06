package com.sicredi.libraryhub.application.service;

import com.sicredi.libraryhub.application.dto.LivroRequestDTO;
import com.sicredi.libraryhub.application.dto.LivroResponseDTO;
import com.sicredi.libraryhub.application.exception.NegocioException;
import com.sicredi.libraryhub.domain.GeneroPojo;
import com.sicredi.libraryhub.domain.Livro;
import com.sicredi.libraryhub.infrastructure.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

    private LivroRequestDTO requestDTO;
    private Livro livro;

    @BeforeEach
    void setUp() {
        requestDTO = new LivroRequestDTO("O Hobbit", "J.R.R. Tolkien", "978-0007525508", 1937, GeneroPojo.FANTASIA, true);

        livro = new Livro();
        livro.setId("1");
        livro.setTitulo("O Hobbit");
        livro.setAutor("J.R.R. Tolkien");
        livro.setIsbn("978-0007525508");
        livro.setAnoPublicacao(1937);
        livro.setGenero(GeneroPojo.FANTASIA);
        livro.setDisponivel(true);
        livro.setDataInclusao(LocalDateTime.now());
        livro.setDataAtualizacao(LocalDateTime.now());
    }

    @Test
    void deveCriarLivroComSucesso() {
        when(livroRepository.findByIsbn(requestDTO.isbn())).thenReturn(Optional.empty());
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        LivroResponseDTO result = livroService.criar(requestDTO);

        assertNotNull(result);
        assertEquals(livro.getId(), result.id());
        assertEquals(livro.getTitulo(), result.titulo());
        verify(livroRepository).save(any(Livro.class));
    }

    @Test
    void naoDeveCriarLivroComIsbnDuplicado() {
        when(livroRepository.findByIsbn(requestDTO.isbn())).thenReturn(Optional.of(livro));

        NegocioException exception = assertThrows(NegocioException.class, () -> livroService.criar(requestDTO));

        assertEquals("ISBN_DUPLICADO", exception.getCodigo());
        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void deveBuscarLivroPorIdComSucesso() {
        when(livroRepository.findById("1")).thenReturn(Optional.of(livro));

        LivroResponseDTO result = livroService.buscarPorId("1");

        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals(livro.getTitulo(), result.titulo());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        when(livroRepository.findById("1")).thenReturn(Optional.empty());

        NegocioException exception = assertThrows(NegocioException.class, () -> livroService.buscarPorId("1"));
        assertEquals("LIVRO_NAO_ENCONTRADO", exception.getCodigo());
    }

    @Test
    void deveAtualizarLivroComSucesso() {
        when(livroRepository.findById("1")).thenReturn(Optional.of(livro));
        when(livroRepository.findByIsbn(requestDTO.isbn())).thenReturn(Optional.of(livro));
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        LivroResponseDTO result = livroService.atualizar("1", requestDTO);

        assertNotNull(result);
        assertEquals(livro.getId(), result.id());
        verify(livroRepository).save(any(Livro.class));
    }

    @Test
    void naoDeveAtualizarLivroComIsbnDeOutroLivro() {
        Livro outroLivro = new Livro();
        outroLivro.setId("2");
        outroLivro.setIsbn("978-0007525508");

        when(livroRepository.findById("1")).thenReturn(Optional.of(livro));
        when(livroRepository.findByIsbn(requestDTO.isbn())).thenReturn(Optional.of(outroLivro));

        NegocioException exception = assertThrows(NegocioException.class, () -> livroService.atualizar("1", requestDTO));
        assertEquals("ISBN_DUPLICADO", exception.getCodigo());
        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void deveExcluirLivroComSucesso() {
        when(livroRepository.findById("1")).thenReturn(Optional.of(livro));

        livroService.excluir("1");

        verify(livroRepository).delete(livro);
    }
}
