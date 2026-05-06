package com.sicredi.libraryhub.presentation.controller;

import com.sicredi.libraryhub.application.dto.LivroPatchRequestDTO;
import com.sicredi.libraryhub.application.dto.LivroRequestDTO;
import com.sicredi.libraryhub.application.dto.LivroResponseDTO;
import com.sicredi.libraryhub.application.service.LivroService;
import com.sicredi.libraryhub.domain.GeneroPojo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "Gerenciamento do acervo da biblioteca")
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    @Operation(summary = "Criar um novo livro", description = "Adiciona um novo livro ao acervo da biblioteca.")
    @ApiResponse(responseCode = "201", description = "Livro criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    public ResponseEntity<LivroResponseDTO> criar(@RequestBody @Valid LivroRequestDTO request) {
        LivroResponseDTO response = livroService.criar(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um livro por ID", description = "Retorna os detalhes de um livro específico utilizando cache Redis.")
    @ApiResponse(responseCode = "200", description = "Livro retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public ResponseEntity<LivroResponseDTO> buscarPorId(@PathVariable String id) {
        LivroResponseDTO response = livroService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar livros", description = "Retorna uma lista paginada de livros. Permite filtrar por gênero literário.")
    @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso")
    public ResponseEntity<Page<LivroResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            @RequestParam(required = false) GeneroPojo genero) {
        Page<LivroResponseDTO> response = livroService.listar(pagina, tamanho, genero);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um livro", description = "Atualiza os dados de um livro existente pelo ID.")
    @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public ResponseEntity<LivroResponseDTO> atualizar(
            @PathVariable String id,
            @RequestBody @Valid LivroRequestDTO request) {
        LivroResponseDTO response = livroService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualização parcial de um livro", description = "Atualiza parcialmente os dados de um livro existente pelo ID.")
    @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public ResponseEntity<LivroResponseDTO> patch(
            @PathVariable String id,
            @RequestBody @Valid LivroPatchRequestDTO request) {
        LivroResponseDTO response = livroService.patch(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um livro", description = "Remove um livro do acervo e invalida o cache.")
    @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        livroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
