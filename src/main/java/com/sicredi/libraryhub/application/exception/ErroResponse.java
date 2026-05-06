package com.sicredi.libraryhub.application.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErroResponse(
        String codigo,
        String mensagem,
        LocalDateTime timestamp
) {
}
