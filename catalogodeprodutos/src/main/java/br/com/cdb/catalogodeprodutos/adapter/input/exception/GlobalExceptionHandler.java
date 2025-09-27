package br.com.cdb.catalogodeprodutos.adapter.input.exception;

import br.com.cdb.catalogodeprodutos.core.domain.exception.ProdutoNaoEncontradoException;


import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handlerNotFound(ProdutoNaoEncontradoException ex){
        log.warn("Produto não encontrado: {}", ex.getMessage());
        Map<String,String> error= new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>  handlerValidation(MethodArgumentNotValidException ex){
        Map<String, String> errors= new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage()));
        log.warn("Erro de validação: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handlerJsonParseError(HttpMessageNotReadableException ex){
        log.warn("Erro ao processar JSON: {}", ex.getMessage());
        Map <String, String> error= new HashMap<>();
        error.put("error", "erro ao processar JSON");
        error.put("mensagem", "Verifique os tipos dos campos enviados");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handlerIllegalArgument(IllegalArgumentException ex) {
        log.warn("Erro de validação: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erro de validação");
        error.put("mensagem", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade de dados: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Já existe um produto com este SKU. Escolha outro.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);

    }
    }
