package br.com.cdb.catalogodeprodutos.factory;

import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;

import java.math.BigDecimal;

public class ProdutoRequestFactory {
    public static ProdutoRequest criarProdutoRequest() {
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Produto Teste");
        request.setDescricao("Descrição teste");
        request.setPreco(BigDecimal.TEN);
        request.setQuantidade(10);
        request.setAtivo(true);
        request.setCategoria(Categoria.CAO);
        request.setSku("SKU123");
        return request;
    }

    public static ProdutoRequest criarProdutoRequestComNome(String nome) {
        ProdutoRequest request = criarProdutoRequest();
        request.setNome(nome);
        return request;
    }
}
