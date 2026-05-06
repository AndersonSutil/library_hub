package com.sicredi.libraryhub.application.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErroConstants {
    public static final String ISBN_DUPLICADO = "ISBN_DUPLICADO";
    public static final String LIVRO_NAO_ENCONTRADO = "LIVRO_NAO_ENCONTRADO";
    public static final String DADOS_INVALIDOS = "DADOS_INVALIDOS";
    public static final String ERRO_INTERNO = "ERRO_INTERNO_SERVIDOR";
}
