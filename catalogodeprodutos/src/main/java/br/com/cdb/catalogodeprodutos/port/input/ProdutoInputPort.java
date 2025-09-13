package br.com.cdb.catalogodeprodutos.port.input;


import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;


import java.util.List;


public interface ProdutoInputPort {
    Produto createProduto(Produto produto);
    Produto findById(int id);
    Produto buscarPorSku(String sku);
    Produto decrementarEstoque(int id, int quantidade);
    List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset, Categoria categoria);
    Produto atualizarProduto(int id, Produto produto);
    void excluirProduto(int id);
    List<Produto> buscarEstoqueBaixo( int limite);
    Categoria categoriaMaisEstoque();

}
