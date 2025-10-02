package br.com.cdb.catalogodeprodutos.port.output;

import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoOutputPort {

     Produto salvar (Produto produto);

    Produto atualizarProduto(int id, Produto produto);

    Optional<Produto> buscarPorId(int id);

    Optional<Produto> buscarPorSku(String sku);

    List<Produto> buscarTodos(int limite, int offset);

    List<Produto> buscarEstoqueBaixo(int limite);

    void deletarPorId(int id);

    void decrementarEstoque(int id, int quantidade);

    Categoria categoriaMaisEstoque();

    List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset, Categoria categoria);
}
