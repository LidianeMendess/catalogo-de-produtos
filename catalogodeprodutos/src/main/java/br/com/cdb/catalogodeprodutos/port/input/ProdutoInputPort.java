package br.com.cdb.catalogodeprodutos.port.input;


import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;


import java.util.List;
import java.util.Optional;

public interface ProdutoInputPort {
    Produto createProduto(Produto produto);
    Optional<Produto> findById(int id);

    List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset);
    Produto atualizarProduto(int id, Produto produto);
    void excluirProduto(int id);
}
