package br.com.cdb.catalogodeprodutos.adapter.output;

import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;
import br.com.cdb.catalogodeprodutos.adapter.output.impl.ProdutoRepositoryImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProdutoOutputAdapter implements ProdutoOutputPort {

    private final ProdutoRepositoryImpl repository;

    public ProdutoOutputAdapter(ProdutoRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public Produto salvar(Produto produto) {
        return repository.salvar(produto);
    }

    @Override
    public Optional<Produto> buscarPorId(Integer id) {
        return repository.buscarPorId(id);
    }


    @Override
    public List<Produto> buscarTodos() {
        return repository.buscarTodos(100,0);
    }

    @Override
    public void deletar(Integer id) {
        repository.deletarPorId(id);
    }

    @Override
    public Optional<Produto> findById(int id) {
        return buscarPorId(id);
    }

    @Override
    public List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset) {
        return repository.buscarComFiltros(ativo, nome, precoMin, precoMax, limite, offset);
    }
}
