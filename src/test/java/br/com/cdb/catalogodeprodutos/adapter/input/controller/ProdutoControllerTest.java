package br.com.cdb.catalogodeprodutos.adapter.input.controller;

import br.com.cdb.catalogodeprodutos.adapter.input.facade.ProdutoFacade;
import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.input.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.factory.ProdutoFactoryBot;
import br.com.cdb.catalogodeprodutos.factory.ProdutoRequestFactory;
import br.com.cdb.catalogodeprodutos.factory.ProdutoResponseFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProdutoControllerTest {

    @Mock
    ProdutoFacade produtoFacade;

    @InjectMocks
    ProdutoController produtoController;

    @Test
    void inserirProdutoRetornaProdutoCriado() {
        ProdutoRequest request = ProdutoRequestFactory.criarProdutoRequest();
        Produto produto = new ProdutoFactoryBot().comId(1).build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(produto.getId());

        when(produtoFacade.inserirProduto(request)).thenReturn(response);

        ResponseEntity<ProdutoResponse> resultado = produtoController.inserirProduto(request);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void buscarPorIdRetornaProduto() {
        int id = 1;
        Produto produto = new ProdutoFactoryBot()
                .comId(id).
                build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(id);

        when(produtoFacade.buscarPorId(id)).thenReturn(response);

        ResponseEntity<ProdutoResponse> resultado = produtoController.buscarPorId(id);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void buscarPorIdProdutoNaoEncontrado() {
        int id = 1;
        when(produtoFacade.buscarPorId(id)).thenThrow(new RuntimeException("Produto não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoController.buscarPorId(id));

        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void buscarPorFiltrosRetornaListaDeProdutos() {
        String tipoFiltro = null;
        Boolean ativo = true;
        String nome = "Produto Teste";
        Double precoMin = 5.0;
        Double precoMax = 20.0;
        int limite = 5;
        int offset = 0;
        Categoria categoria = Categoria.CAO;

        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(1),
                ProdutoResponseFactory.criarProdutoResponseComId(2)
        );

        when(produtoFacade.buscarPorFiltros(
                tipoFiltro,
                ativo,
                nome,
                precoMin,
                precoMax,
                limite,
                offset,
                categoria
        )).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarPorFiltros(
                tipoFiltro,
                ativo,
                nome,
                precoMin,
                precoMax,
                limite,
                offset,
                categoria
        );

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }

    @Test
    void buscarPorFiltrosSemFiltrosRetornaLista() {
        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(1)
        );

        when(produtoFacade.buscarPorFiltros(null,null, null, null, null, 10, 0, null))
                .thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarPorFiltros(
                null, null, null, null, null, 10, 0, null);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }

    @Test
    void buscarPorFiltrosComFiltrosParciais() {
        String tipoFiltro = null;
        Boolean ativo = true;
        Categoria categoria = Categoria.CAO;

        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(1)
        );

        when(produtoFacade.buscarPorFiltros(tipoFiltro, ativo, null, null, null, 10, 0, categoria))
                .thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarPorFiltros(
                tipoFiltro, ativo, null, null, null, 10, 0, categoria
        );

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }

    @Test
    void atualizarProdutoRetornaProdutoAtualizado() {
        int id = 1;
        ProdutoRequest request = ProdutoRequestFactory.criarProdutoRequest();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(id);

        when(produtoFacade.atualizarProduto(id, request)).thenReturn(response);

        ResponseEntity<ProdutoResponse> resultado = produtoController.atualizarProduto(id, request);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void deletarProdutoOk() {
        int id = 1;

        doNothing().when(produtoFacade).excluirProduto(id);

        ResponseEntity<Void> resultado = produtoController.excluirProduto(id);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
    }

    @Test
    void buscarPorSkuRetornaProduto() {
        String sku = "SKU123";
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponse();

        when(produtoFacade.buscarPorSku(sku)).thenReturn(response);

        ResponseEntity<ProdutoResponse> resultado = produtoController.buscarPorSku(sku);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void buscarPorSkuProdutoNaoEncontrado() {
        String sku = "SKU123";
        when(produtoFacade.buscarPorSku(sku)).thenThrow(new RuntimeException("Produto não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoController.buscarPorSku(sku));

        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void buscarPorCategoriaRetornaListaDeProdutos() {
        Categoria categoria = Categoria.CAO;
        int limite = 10;
        int offset = 0;

        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(1),
                ProdutoResponseFactory.criarProdutoResponseComId(2)
        );

        when(produtoFacade.buscarPorCategoria(categoria, limite, offset)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarPorCategoria(categoria, limite, offset);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }

    @Test
    void buscarPorCategoriaRetornaListaVazia() {
        Categoria categoria = Categoria.CAO;
        int limite = 10;
        int offset = 0;

        when(produtoFacade.buscarPorCategoria(categoria, limite, offset)).thenReturn(List.of());

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarPorCategoria(categoria, limite, offset);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(0, resultado.getBody().size());
    }

    @Test
    void decrementarEstoqueRetornaProdutoAtualizado() {
        int id = 1;
        int quantidade = 2;
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(id);

        when(produtoFacade.decrementarEstoque(id, quantidade)).thenReturn(response);

        ResponseEntity<ProdutoResponse> resultado = produtoController.decrementarEstoque(id, quantidade);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void decrementarEstoqueComQuantidadeMaiorQueEstoqueLancaExcecao() {
        int id = 1;
        int quantidade = 100;

        when(produtoFacade.decrementarEstoque(id, quantidade))
                .thenThrow(new RuntimeException("Estoque insuficiente"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoController.decrementarEstoque(id, quantidade));

        assertEquals("Estoque insuficiente", exception.getMessage());
    }

    @Test
    void buscarEstoqueBaixoRetornaProdutos() {
        int limite = 5;

        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(1),
                ProdutoResponseFactory.criarProdutoResponseComId(2)
        );

        when(produtoFacade.buscarEstoqueBaixo(limite)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarEstoqueBaixo(limite);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }

    @Test
    void buscarEstoqueBaixoRetornaListaVazia() {
        int limite = 5;

        when(produtoFacade.buscarEstoqueBaixo(limite)).thenReturn(List.of());

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarEstoqueBaixo(limite);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(0, resultado.getBody().size());
    }

    @Test
    void categoriaMaisEstoqueRetornaCategoria() {
        Categoria categoria = Categoria.CAO;

        when(produtoFacade.categoriaMaisEstoque()).thenReturn(categoria);

        ResponseEntity<Categoria> resultado = produtoController.categoriaMaisEstoque();

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(categoria, resultado.getBody());
    }

    @Test
    void buscarTodosRetornaListaDeProdutos() {
        int limite = 10;
        int offset = 0;

        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(1),
                ProdutoResponseFactory.criarProdutoResponseComId(2)
        );

        when(produtoFacade.buscarTodos(limite, offset)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarTodos(limite, offset);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }

    @Test
    void buscarTodosRetornaListaVazia() {
        int limite = 10;
        int offset = 0;

        when(produtoFacade.buscarTodos(limite, offset)).thenReturn(List.of());

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarTodos(limite, offset);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(0, resultado.getBody().size());
    }
}
