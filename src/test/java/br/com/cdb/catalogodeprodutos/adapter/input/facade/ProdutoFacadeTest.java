package br.com.cdb.catalogodeprodutos.adapter.input.facade;

import br.com.cdb.catalogodeprodutos.adapter.input.mapper.ProdutoMapper;
import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.input.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.core.domain.usecase.ProdutoFiltroContexto;
import br.com.cdb.catalogodeprodutos.factory.ProdutoFactoryBot;
import br.com.cdb.catalogodeprodutos.factory.ProdutoRequestFactory;
import br.com.cdb.catalogodeprodutos.factory.ProdutoResponseFactory;
import br.com.cdb.catalogodeprodutos.port.input.ProdutoInputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProdutoFacadeTest {

    @Mock
    ProdutoInputPort produtoInputPort;

    @Mock
    ProdutoMapper produtoMapper;

    @Mock
    ProdutoFiltroContexto produtoFiltroContexto;

    @InjectMocks
    ProdutoFacade produtoFacade;

    @Test
    void inserirProdutoDeveRetornarProdutoResponse() {
        ProdutoRequest request = ProdutoRequestFactory.criarProdutoRequest();
        Produto produto = new ProdutoFactoryBot().comId(1).build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(produto.getId());

        when(produtoMapper.toDomain(request)).thenReturn(produto);
        when(produtoInputPort.createProduto(produto)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(response);

        ProdutoResponse resultado = produtoFacade.inserirProduto(request);

        assertEquals(response, resultado);
        verify(produtoMapper).toDomain(request);
        verify(produtoInputPort).createProduto(produto);
        verify(produtoMapper).toResponse(produto);
    }

    @Test
    void buscarPorIdDeveRetornarProdutoResponse() {
        int id = 1;
        Produto produto = new ProdutoFactoryBot().comId(id).build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(id);

        when(produtoInputPort.findById(id)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(response);

        ProdutoResponse resultado = produtoFacade.buscarPorId(id);

        assertEquals(response, resultado);
    }

    @Test
    void atualizarProdutoDeveRetornarProdutoAtualizado() {
        int id = 1;
        ProdutoRequest request = ProdutoRequestFactory.criarProdutoRequest();
        Produto produto = new ProdutoFactoryBot().comId(id).build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(id);

        when(produtoMapper.toDomain(request)).thenReturn(produto);
        when(produtoInputPort.atualizarProduto(id, produto)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(response);

        ProdutoResponse resultado = produtoFacade.atualizarProduto(id, request);

        assertEquals(response, resultado);
    }

    @Test
    void excluirProdutoDeveChamarInputPort() {
        int id = 1;
        doNothing().when(produtoInputPort).excluirProduto(id);

        produtoFacade.excluirProduto(id);

        verify(produtoInputPort).excluirProduto(id);
    }

    @Test
    void buscarPorSkuDeveRetornarProdutoResponse() {
        String sku = "SKU123";
        Produto produto = new ProdutoFactoryBot().build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponse();

        when(produtoInputPort.buscarPorSku(sku)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(response);

        ProdutoResponse resultado = produtoFacade.buscarPorSku(sku);

        assertEquals(response, resultado);
    }

    @Test
    void buscarPorFiltrosDeveUsarFiltroContextoQuandoTipoFiltroPresente() {
        String tipoFiltro = "preco";
        Boolean ativo = true;
        String nome = "Produto Teste";
        Double precoMin = 10.0;
        Double precoMax = 50.0;
        int limite = 5;
        int offset = 0;
        Categoria categoria = Categoria.CAO;

        List<Produto> produtos = List.of(new ProdutoFactoryBot().build());
        List<ProdutoResponse> responses = List.of(ProdutoResponseFactory.criarProdutoResponse());

        when(produtoFiltroContexto.buscarComFiltros(tipoFiltro, ativo, nome, precoMin, precoMax, limite, offset, categoria))
                .thenReturn(produtos);
        when(produtoMapper.toResponseList(produtos)).thenReturn(responses);

        List<ProdutoResponse> resultado = produtoFacade.buscarPorFiltros(tipoFiltro, ativo, nome, precoMin, precoMax, limite, offset, categoria);

        assertEquals(responses, resultado);
    }

    @Test
    void buscarPorFiltrosDeveChamarInputPortQuandoTipoFiltroNulo() {
        Boolean ativo = true;
        String nome = "Produto Teste";
        int limite = 5;
        int offset = 0;
        Categoria categoria = Categoria.CAO;

        List<Produto> produtos = List.of(new ProdutoFactoryBot().build());
        List<ProdutoResponse> responses = List.of(ProdutoResponseFactory.criarProdutoResponse());

        when(produtoInputPort.buscarComFiltros(ativo, nome, null, null, limite, offset, categoria))
                .thenReturn(produtos);
        when(produtoMapper.toResponseList(produtos)).thenReturn(responses);

        List<ProdutoResponse> resultado = produtoFacade.buscarPorFiltros(null, ativo, nome, null, null, limite, offset, categoria);

        assertEquals(responses, resultado);
    }

    @Test
    void buscarPorCategoriaDeveRetornarListaDeProdutos() {
        Categoria categoria = Categoria.CAO;
        int limite = 10;
        int offset = 0;

        List<Produto> produtos = List.of(new ProdutoFactoryBot().build());
        List<ProdutoResponse> responses = List.of(ProdutoResponseFactory.criarProdutoResponse());

        when(produtoInputPort.buscarComFiltros(true, null, null, null, limite, offset, categoria)).thenReturn(produtos);
        when(produtoMapper.toResponseList(produtos)).thenReturn(responses);

        List<ProdutoResponse> resultado = produtoFacade.buscarPorCategoria(categoria, limite, offset);

        assertEquals(responses, resultado);
    }

    @Test
    void decrementarEstoqueDeveRetornarProdutoAtualizado() {
        int id = 1;
        int quantidade = 2;
        Produto produto = new ProdutoFactoryBot().comId(id).build();
        ProdutoResponse response = ProdutoResponseFactory.criarProdutoResponseComId(id);

        when(produtoInputPort.decrementarEstoque(id, quantidade)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(response);

        ProdutoResponse resultado = produtoFacade.decrementarEstoque(id, quantidade);

        assertEquals(response, resultado);
    }

    @Test
    void buscarEstoqueBaixoDeveRetornarListaDeProdutos() {
        int limite = 5;
        List<Produto> produtos = List.of(new ProdutoFactoryBot().build());
        List<ProdutoResponse> responses = List.of(ProdutoResponseFactory.criarProdutoResponse());

        when(produtoInputPort.buscarEstoqueBaixo(limite)).thenReturn(produtos);
        when(produtoMapper.toResponseList(produtos)).thenReturn(responses);

        List<ProdutoResponse> resultado = produtoFacade.buscarEstoqueBaixo(limite);

        assertEquals(responses, resultado);
    }

    @Test
    void categoriaMaisEstoqueDeveRetornarCategoria() {
        Categoria categoria = Categoria.CAO;
        when(produtoInputPort.categoriaMaisEstoque()).thenReturn(categoria);

        Categoria resultado = produtoFacade.categoriaMaisEstoque();

        assertEquals(categoria, resultado);
    }

    @Test
    void buscarTodosDeveRetornarListaDeProdutos() {
        int limite = 10;
        int offset = 0;
        List<Produto> produtos = List.of(new ProdutoFactoryBot().build());
        List<ProdutoResponse> responses = List.of(ProdutoResponseFactory.criarProdutoResponse());

        when(produtoInputPort.buscarTodos(limite, offset)).thenReturn(produtos);
        when(produtoMapper.toResponseList(produtos)).thenReturn(responses);

        List<ProdutoResponse> resultado = produtoFacade.buscarTodos(limite, offset);

        assertEquals(responses, resultado);
    }
}
