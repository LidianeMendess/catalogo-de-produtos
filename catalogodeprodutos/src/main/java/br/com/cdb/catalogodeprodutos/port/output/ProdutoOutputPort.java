package br.com.cdb.catalogodeprodutos.port.output;

import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;


import java.util.List;
import java.util.Optional;


public interface ProdutoOutputPort {


        Produto salvar(Produto produto);
        Optional<Produto> buscarPorId(Integer id);
        List<Produto> buscarTodos();
        void deletar(Integer id);
        Optional<Produto> findById(int id);
        List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset);

}
