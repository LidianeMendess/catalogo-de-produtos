package br.com.cdb.catalogodeprodutos.adapter.input.exception;

import br.com.cdb.catalogodeprodutos.adapter.input.response.ErrorResponse;
import br.com.cdb.catalogodeprodutos.core.domain.exception.ProdutoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;

    @Mock
    HttpServletRequest request;

    @Test
    void deveRetornarNotFoundQuandoProdutoNaoEncontrado() {
        when(request.getRequestURI()).thenReturn("/produtos/1");
        ProdutoNaoEncontradoException ex = new ProdutoNaoEncontradoException("Produto não encontrado");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Produto não encontrado", response.getBody().getMessage());
        assertEquals("/produtos/1", response.getBody().getPath());
    }

    @Test
    void deveRetornarBadRequestQuandoErroDeValidacao() {
        when(request.getRequestURI()).thenReturn("/produtos");
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("produto", "nome", "Nome é obrigatório");
        when(bindingResult.getFieldError()).thenReturn(fieldError);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Nome é obrigatório", response.getBody().getMessage());
    }

    @Test
    void deveRetornarBadRequestQuandoJsonInvalido() {
        when(request.getRequestURI()).thenReturn("/produtos");
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro ao ler JSON");


        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerJsonParseError(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao processar JSON. Verifique os tipos dos campos enviados", response.getBody().getMessage());
    }

    @Test
    void deveRetornarBadRequestQuandoIllegalArgument() {
        when(request.getRequestURI()).thenReturn("/produtos");
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerIllegalArgument(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Argumento inválido", response.getBody().getMessage());
    }

    @Test
    void deveRetornarConflictQuandoViolacaoDeIntegridade() {
        when(request.getRequestURI()).thenReturn("/produtos");
        DataIntegrityViolationException ex = new DataIntegrityViolationException("SKU duplicado");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDataIntegrityViolation(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Já existe um produto com este SKU. Escolha outro.", response.getBody().getMessage());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoErroGenerico() {
        when(request.getRequestURI()).thenReturn("/produtos");
        Exception ex = new Exception("Erro inesperado");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAllExceptions(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Ocorreu um erro inesperado", response.getBody().getMessage());
    }
}
