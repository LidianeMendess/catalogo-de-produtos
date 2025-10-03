package br.com.cdb.catalogodeprodutos.adapter.input.exception;

import br.com.cdb.catalogodeprodutos.adapter.input.response.ErrorResponse;
import br.com.cdb.catalogodeprodutos.core.domain.exception.ProdutoNaoEncontradoException;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlerNotFound(ProdutoNaoEncontradoException ex, HttpServletRequest request){
        log.warn("Produto não encontrado: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerValidation(MethodArgumentNotValidException ex, HttpServletRequest request){
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        log.warn("Erro de validação: {}", message);
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handlerJsonParseError(HttpMessageNotReadableException ex, HttpServletRequest request){
        log.warn("Erro ao processar JSON: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Erro ao processar JSON. Verifique os tipos dos campos enviados",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handlerIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Erro de validação: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Violação de integridade de dados: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Já existe um produto com este SKU. Escolha outro.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        log.error("Erro interno no servidor: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}