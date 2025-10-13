package br.com.cdb.catalogodeprodutos.core.domain.usecase;

import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;

import java.util.List;

public interface FiltroProdutoStrategy {
    List<Produto> filtrar(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset, Categoria categoria);
}