package com.sicredi.libraryhub.application.dto;

import com.sicredi.libraryhub.domain.GeneroPojo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record LivroPatchRequestDTO(
        String titulo,
        String autor,
        String isbn,

        @Min(value = 1001, message = "Ano de publicação deve ser maior que 1000")
        @Max(value = 2026, message = "Ano de publicação não pode ser superior ao ano atual")
        Integer anoPublicacao,

        GeneroPojo genero,
        Boolean disponivel
) {
}
