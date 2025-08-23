package br.com.cdb.catalogodeprodutos.exception;

import jakarta.persistence.EntityNotFoundException;

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
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlerNotFound(EntityNotFoundException msg){
        Map<String,String> error= new HashMap<>();
        error.put("error", msg.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>  handlerValidation(MethodArgumentNotValidException msg){
        Map<String, String> errors= new HashMap<>();
        msg.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handlerJsonParseError(HttpMessageNotReadableException msg){
        Map <String, String> error= new HashMap<>();
        error.put("erro", "erro ao processar JSON");
        error.put("mensagem", "Verifique os tipos dos camposenviados");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Já existe um produto com este SKU. Escolha outro.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    }
