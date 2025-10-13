package br.com.cdb.catalogodeprodutos.core.domain.usecase.strategy;

import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.core.domain.usecase.FiltroProdutoStrategy;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("nome")
public class FiltroPorNome implements FiltroProdutoStrategy {

    private final ProdutoOutputPort produtoOutputPort;

    public FiltroPorNome(ProdutoOutputPort produtoOutputPort){
        this.produtoOutputPort = produtoOutputPort;
    }

    @Override
    public List<Produto> filtrar(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset, Categoria categoria) {
        if (nome == null || nome.isBlank()) {
            return Collections.emptyList();
        }
        if (ativo== null) ativo=true;
        return produtoOutputPort.buscarComFiltros(ativo, nome, null, null, limite, offset, null);
    }
}
