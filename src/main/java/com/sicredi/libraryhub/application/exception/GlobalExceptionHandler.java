package com.sicredi.libraryhub.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErroResponse> handleNegocioException(NegocioException ex) {
        ErroResponse response = ErroResponse.builder()
                .codigo(ex.getCodigo())
                .mensagem(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        HttpStatus status = ex.getCodigo().contains("NAO_ENCONTRADO") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String mensagemErro = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação nos dados fornecidos");

        ErroResponse response = ErroResponse.builder()
                .codigo(ErroConstants.DADOS_INVALIDOS)
                .mensagem(mensagemErro)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenericException(Exception ex) {
        ErroResponse response = ErroResponse.builder()
                .codigo(ErroConstants.ERRO_INTERNO)
                .mensagem("Ocorreu um erro inesperado no servidor: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
