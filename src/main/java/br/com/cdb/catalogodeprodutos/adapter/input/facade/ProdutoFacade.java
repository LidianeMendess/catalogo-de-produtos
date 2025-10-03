package br.com.cdb.catalogodeprodutos.adapter.input.facade;

import br.com.cdb.catalogodeprodutos.adapter.input.mapper.ProdutoMapper;
import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.input.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.core.domain.usecase.ProdutoFiltroContexto;
import br.com.cdb.catalogodeprodutos.port.input.ProdutoInputPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdutoFacade {
    private final ProdutoInputPort produtoInputPort;
    private final ProdutoMapper produtoMapper;
    private final ProdutoFiltroContexto produtoFiltroContexto;

    public ProdutoFacade(ProdutoInputPort produtoInputPort, ProdutoMapper produtoMapper, ProdutoFiltroContexto produtoFiltroContexto){
        this.produtoInputPort = produtoInputPort;
        this.produtoMapper = produtoMapper;
        this.produtoFiltroContexto = produtoFiltroContexto;
    }

    public ProdutoResponse inserirProduto(ProdutoRequest request) {
        Produto produto = produtoMapper.toDomain(request);
        Produto salvo = produtoInputPort.createProduto(produto);
        return produtoMapper.toResponse(salvo);
    }

    public ProdutoResponse buscarPorId(int id){
        Produto produto = produtoInputPort.findById(id);
        return produtoMapper.toResponse(produto);
    }

    public List<ProdutoResponse> buscarPorFiltros(String tipoFiltro, Boolean ativo, String nome,
                                                 Double precoMin, Double precoMax,
                                                 int limite, int offset, Categoria categoria) {
        List<Produto> produtos;

        if (tipoFiltro != null) {
            produtos = produtoFiltroContexto.buscarComFiltros(
                    tipoFiltro, ativo, nome, precoMin, precoMax, limite, offset, categoria);
        } else {
            produtos = produtoInputPort.buscarComFiltros(
                    ativo, nome, precoMin, precoMax, limite, offset, categoria);
        }

        return produtoMapper.toResponseList(produtos);
    }

    public ProdutoResponse atualizarProduto(int id, ProdutoRequest request) {
        Produto produto = produtoMapper.toDomain(request);
        Produto atualizado = produtoInputPort.atualizarProduto(id, produto);
        return produtoMapper.toResponse(atualizado);
    }

    public void excluirProduto(int id) {
        produtoInputPort.excluirProduto(id);
    }

    public ProdutoResponse buscarPorSku(String sku) {
        Produto produto = produtoInputPort.buscarPorSku(sku);
        return produtoMapper.toResponse(produto);
    }

    public List<ProdutoResponse> buscarPorCategoria(Categoria categoria, int limite, int offset) {
        List<Produto> produtos = produtoInputPort.buscarComFiltros(
                true, null, null, null, limite, offset, categoria);
        return produtoMapper.toResponseList(produtos);
    }

    public ProdutoResponse decrementarEstoque(int id, int quantidade) {
        Produto produto = produtoInputPort.decrementarEstoque(id, quantidade);
        return produtoMapper.toResponse(produto);
    }

    public List<ProdutoResponse> buscarEstoqueBaixo(int limite) {
        List<Produto> produtos = produtoInputPort.buscarEstoqueBaixo(limite);
        return produtoMapper.toResponseList(produtos);
    }

    public Categoria categoriaMaisEstoque() {
        return produtoInputPort.categoriaMaisEstoque();
    }

    public List<ProdutoResponse> buscarTodos(int limite, int offset) {
        List<Produto> produtos = produtoInputPort.buscarTodos(limite, offset);
        return produtoMapper.toResponseList(produtos);
    }
}