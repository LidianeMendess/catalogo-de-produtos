package br.com.cdb.catalogodeprodutos.core.domain.usecase;

import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.core.domain.usecase.strategy.FiltroPorNome;
import br.com.cdb.catalogodeprodutos.core.domain.usecase.strategy.FiltroPorPreco;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class ProdutoFiltroContexto {
    private final Map<String, FiltroProdutoStrategy> mapStrategy;

    public ProdutoFiltroContexto(FiltroPorNome filtroPorNome, FiltroPorPreco filtroPorPreco){
        this.mapStrategy = Map.of(
                "nome", filtroPorNome,
                "preco", filtroPorPreco
        );
    }

    public List<Produto> buscarComFiltros(String tipoFiltro, Boolean ativo, String nome, Double precoMin, Double precoMax,
                                          int limite, int offset, Categoria categoria) {
        FiltroProdutoStrategy estrategia = mapStrategy.get(tipoFiltro);
        if (estrategia == null) return Collections.emptyList();
        return estrategia.filtrar(ativo, nome, precoMin, precoMax, limite, offset, categoria);
    }
}
