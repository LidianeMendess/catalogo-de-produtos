package br.com.cdb.catalogodeprodutos.adapter.output.impl;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProdutoRepository {

    Produto salvar(Produto produto);

    Optional<Produto> buscarPorId(int id);

    Page<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, Pageable pageable);

    void deletarPorId(int id);
}
