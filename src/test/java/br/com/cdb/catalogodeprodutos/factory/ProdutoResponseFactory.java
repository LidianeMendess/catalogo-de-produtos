package br.com.cdb.catalogodeprodutos.factory;

import br.com.cdb.catalogodeprodutos.adapter.input.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProdutoResponseFactory {
    public static ProdutoResponse criarProdutoResponse() {
    ProdutoResponse response = new ProdutoResponse();
    response.setId(1);
    response.setNome("Produto Teste");
    response.setDescricao("Descrição teste");
    response.setPreco(BigDecimal.TEN);
    response.setQuantidade(10);
    response.setAtivo(true);
    response.setCategoria(Categoria.CAO);
    response.setSku("SKU123");
    return response;
}

    public static ProdutoResponse criarProdutoResponseComId(int id) {
        ProdutoResponse response = criarProdutoResponse();
        response.setId(id);
        return response;
    }

    public static List<ProdutoResponse> criarLista(int quantidade) {
        return IntStream.range(0, quantidade)
                .mapToObj(i -> criarProdutoResponseComId(i + 1))
                .collect(Collectors.toList());
    }
}
