package br.com.cdb.catalogodeprodutos.adapter.input.controller;

import br.com.cdb.catalogodeprodutos.adapter.input.mapper.ProdutoMapper;
import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.input.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.factory.ProdutoFactoryBot;
import br.com.cdb.catalogodeprodutos.factory.ProdutoRequestFactory;
import br.com.cdb.catalogodeprodutos.factory.ProdutoResponseFactory;
import br.com.cdb.catalogodeprodutos.port.input.ProdutoInputPort;
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
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProdutoControllerTest {

    @Mock
    ProdutoInputPort produtoInputPort;

    @Mock
    ProdutoMapper produtoMapper;

    @InjectMocks
    ProdutoController produtoController;

    @Test
    void inserirProdutoRetornaProdutoCriado(){
        ProdutoRequest request = ProdutoRequestFactory.criarProdutoRequest();
        Produto produto = new ProdutoFactoryBot().comId(1).build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(produto.getId());

        when(produtoMapper.toDomain(request)).thenReturn(produto);
        when(produtoInputPort.createProduto(produto)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(response);

        ResponseEntity<ProdutoResponse> resultado = produtoController.inserirProduto(request);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void buscarPorIdRetornaProduto(){
        int id = 1;
        Produto produto = new ProdutoFactoryBot().comId(id).build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(id);

        when(produtoInputPort.findById(id)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(response);

        ResponseEntity<ProdutoResponse> resultado = produtoController.buscarPorId(id);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void buscarPorIdProdutoNaoEncontrado(){
        int id = 1;
        when(produtoInputPort.findById(id)).thenThrow(new RuntimeException("Produto não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class,
        () -> produtoController.buscarPorId(id));

        assertEquals("Produto não encontrado", exception.getMessage());
    }

/*    @Test
    void buscarPorFiltrosRetornaListaDeProdutos() {
        Boolean ativo = true;
        String nome = "Produto Teste";
        Double precoMin = 5.0;
        Double precoMax = 20.0;
        int limite = 5;
        int offset = 0;
        Categoria categoria = Categoria.CAO;

        Produto produto1 = new ProdutoFactoryBot().comId(1).build();
        Produto produto2 = new ProdutoFactoryBot().comId(2).build();

        List<Produto> produtosDomain = List.of(produto1, produto2);

        ProdutoResponse response1 = ProdutoResponseFactory.criarProdutoResponseComId(produto1.getId());
        ProdutoResponse response2 = ProdutoResponseFactory.criarProdutoResponseComId(produto2.getId());
        List<ProdutoResponse> produtosResponse = List.of(response1, response2);

        when(produtoInputPort.buscarComFiltros(ativo, nome, precoMin, precoMax, limite, offset, categoria))
                .thenReturn(produtosDomain);
        when(produtoMapper.toResponseList(produtosDomain)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarPorFiltros(
                ativo, nome, precoMin, precoMax, limite, offset, categoria
        );

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }*/

/*    @Test
    void buscarPorFiltrosSemFiltrosRetornaLista() {
        List<Produto> produtosDomain = List.of(
                new ProdutoFactoryBot().comId(1).build()
        );
        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(1)
        );

        when(produtoInputPort.buscarComFiltros(null, null, null, null, 10, 0, null))
                .thenReturn(produtosDomain);
        when(produtoMapper.toResponseList(produtosDomain)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarPorFiltros(
                null, null, null, null, 10, 0, null);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }*/

/*
    @Test
    void buscarPorFiltrosComFiltrosParciais() {
        Boolean ativo = true;
        Categoria categoria = Categoria.CAO;

        Produto produto = new ProdutoFactoryBot().comId(1).build();
        List<Produto> produtosDomain = List.of(produto);
        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(produto.getId()));

        when(produtoInputPort.buscarComFiltros(ativo, null, null, null, 10, 0, categoria))
                .thenReturn(produtosDomain);
        when(produtoMapper.toResponseList(produtosDomain)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarPorFiltros(
                ativo, null, null, null, 10, 0, categoria
        );

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }
*/

/*
    @Test
    void atualizarProdutoRetornaProdutoAtualizado(){
        int id = 1;

        ProdutoRequest request = ProdutoRequestFactory.criarProdutoRequest();
        Produto produto = new ProdutoFactoryBot().comId(id).build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(id);

        when(produtoMapper.toDomain(request)).thenReturn(produto);
        when(produtoInputPort.atualizarProduto(id,produto)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(response);

        ResponseEntity<ProdutoResponse> resultado = produtoController.atualizarCampos(id, request);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
    }
*/

    @Test
    void deletarProdutoOk(){
        int id = 1;

        ResponseEntity<Void> resultado = produtoController.excluirProduto(id);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
    }

/*
    @Test
    void buscarPorSkuRetornaProduto(){
        String Sku = "SKU123";
        Produto produto = new ProdutoFactoryBot().comSku(Sku).build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponse();

        when(produtoInputPort.buscarPorSku(Sku)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(response);

        ResponseEntity<ProdutoResponse> resultado = produtoController.buscarPorSKU(Sku);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
    }
*/

   /* @Test
    void buscarPorSkuProdutoNaoEncontrado(){
        String sku = "SKU123";
        when(produtoInputPort.buscarPorSku(sku)).thenThrow(new RuntimeException("Produto não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoController.buscarPorSKU(sku));

        assertEquals("Produto não encontrado", exception.getMessage());
    }
*/
    @Test
    void buscarPorCategoriaRetornaListaDeProdutos() {
        Categoria categoria = Categoria.CAO;
        int limite = 10;
        int offset = 0;

        Produto produto1 = new ProdutoFactoryBot().comId(1).build();
        Produto produto2 = new ProdutoFactoryBot().comId(2).build();
        List<Produto> produtosDomain = List.of(produto1, produto2);

        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(produto1.getId()),
                ProdutoResponseFactory.criarProdutoResponseComId(produto2.getId())
        );

        when(produtoInputPort.buscarComFiltros(true, null, null, null, limite, offset, categoria))
                .thenReturn(produtosDomain);
        when(produtoMapper.toResponseList(produtosDomain)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarPorCategoria(categoria, limite, offset);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }

    @Test
    void buscarPorCategoriaRetornaListaVazia() {
        Categoria categoria = Categoria.CAO;
        int limite = 10;
        int offset = 0;

        List<Produto> produtosDomain = List.of();

        List<ProdutoResponse> produtosResponse = List.of();

        when(produtoInputPort.buscarComFiltros(true, null, null, null, limite, offset, categoria))
                .thenReturn(produtosDomain);
        when(produtoMapper.toResponseList(produtosDomain)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarPorCategoria(categoria, limite, offset);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(0, resultado.getBody().size());
        assertEquals(produtosResponse, resultado.getBody());
    }

    @Test
    void decrementarEstoqueRetornaProdutoAtualizado() {
        int id = 1;
        int quantidade = 2;

        Produto produto = new ProdutoFactoryBot().comId(id).build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(id);

        when(produtoInputPort.decrementarEstoque(id, quantidade)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(response);

        ResponseEntity<ProdutoResponse> resultado = produtoController.decrementarEstoque(id, quantidade);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void decrementarEstoqueComQuantidadeMaiorQueEstoqueLancaExcecao() {
        int id = 1;
        int quantidade = 100;


        when(produtoInputPort.decrementarEstoque(id, quantidade))
                .thenThrow(new RuntimeException("Estoque insuficiente"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoController.decrementarEstoque(id, quantidade));

        assertEquals("Estoque insuficiente", exception.getMessage());
    }

/*
    @Test
    void buscarEstoqueBaixoRetornaProdutos() {
        int limite = 5;

        Produto produto1 = new ProdutoFactoryBot().comId(1).build();
        Produto produto2 = new ProdutoFactoryBot().comId(2).build();

        List<Produto> produtosDomain = List.of(produto1, produto2);
        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(produto1.getId()),
                ProdutoResponseFactory.criarProdutoResponseComId(produto2.getId())
        );

        when(produtoInputPort.buscarEstoqueBaixo(limite)).thenReturn(produtosDomain);
        when(produtoMapper.toResponseList(produtosDomain)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarestoqueBaixo(limite);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }

    @Test
    void buscarEstoqueBaixoRetornaListaVazia() {
        int limite = 5;

        when(produtoInputPort.buscarEstoqueBaixo(limite)).thenReturn(List.of());
        when(produtoMapper.toResponseList(List.of())).thenReturn(List.of());

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarestoqueBaixo(limite);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(0, resultado.getBody().size());
    }
*/

    @Test
    void categoriaMaisEstoqueRetornaCategoria() {
        Categoria categoria = Categoria.CAO;

        when(produtoInputPort.categoriaMaisEstoque()).thenReturn(categoria);

        ResponseEntity<Categoria> resultado = produtoController.categoriaMaisEstoque();

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(categoria, resultado.getBody());
    }

    @Test
    void buscarTodosRetornaListaDeProdutos() {
        int limite = 10;
        int offset = 0;

        Produto produto1 = new ProdutoFactoryBot().comId(1).build();
        Produto produto2 = new ProdutoFactoryBot().comId(2).build();

        List<Produto> produtosDomain = List.of(produto1, produto2);
        List<ProdutoResponse> produtosResponse = List.of(
                ProdutoResponseFactory.criarProdutoResponseComId(produto1.getId()),
                ProdutoResponseFactory.criarProdutoResponseComId(produto2.getId())
        );

        when(produtoInputPort.buscarTodos(limite, offset)).thenReturn(produtosDomain);
        when(produtoMapper.toResponseList(produtosDomain)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarTodos(limite, offset);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(produtosResponse, resultado.getBody());
    }

    @Test
    void buscarTodosRetornaListaVazia() {
        int limite = 10;
        int offset = 0;

        List<Produto> produtosDomain = List.of();
        List<ProdutoResponse> produtosResponse = List.of();

        when(produtoInputPort.buscarTodos(limite, offset)).thenReturn(produtosDomain);
        when(produtoMapper.toResponseList(produtosDomain)).thenReturn(produtosResponse);

        ResponseEntity<List<ProdutoResponse>> resultado = produtoController.buscarTodos(limite, offset);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(0, resultado.getBody().size());
    }
}



