package com.sicredi.libraryhub.application.dto;

import com.sicredi.libraryhub.domain.GeneroPojo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroRequestDTO(
        @NotBlank(message = "Título é obrigatório")
        String titulo,

        @NotBlank(message = "Autor é obrigatório")
        String autor,

        @NotBlank(message = "ISBN é obrigatório")
        String isbn,

        @NotNull(message = "Ano de publicação é obrigatório")
        @Min(value = 1001, message = "Ano de publicação deve ser maior que 1000")
        @Max(value = 2026, message = "Ano de publicação não pode ser superior ao ano atual") // Fixed: dynamic year would be better but let's use a reasonable max or a custom validator
        Integer anoPublicacao,

        @NotNull(message = "Gênero literário é obrigatório")
        GeneroPojo genero,

        @NotNull(message = "Disponibilidade é obrigatória")
        Boolean disponivel
) {
}
