package com.sicredi.libraryhub.application.dto;

import com.sicredi.libraryhub.domain.GeneroPojo;

import java.time.LocalDateTime;

public record LivroResponseDTO(
        String id,
        String titulo,
        String autor,
        String isbn,
        Integer anoPublicacao,
        GeneroPojo genero,
        Boolean disponivel,
        LocalDateTime dataInclusao,
        LocalDateTime dataAtualizacao
) {
}
