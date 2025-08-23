package br.com.cdb.catalogodeprodutos.port.input;


import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProdutoInputPort {
    Produto createProduto(Produto produto);
    Optional<Produto> findById(int id);

    Page<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, Pageable pageable);
    Produto atualizarProduto(int id, Produto produto);
    void excluirProduto(int id);
}
