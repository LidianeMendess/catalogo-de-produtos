package br.com.cdb.catalogodeprodutos.core.usecase;

import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.port.input.ProdutoInputPort;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoUseCase implements ProdutoInputPort {

    private final ProdutoOutputPort produtoOutputPort;

    public ProdutoUseCase(ProdutoOutputPort produtoOutputPort) {
        this.produtoOutputPort = produtoOutputPort;
    }

    @Override
    public Produto createProduto(Produto produto) {
        produto.setAtivo(true);
        return produtoOutputPort.salvar(produto);
    }

    @Override
    public Optional<Produto> findById(int id) {
        return produtoOutputPort.buscarPorId(id);
    }

    @Override
    public Page<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, Pageable pageable) {
        if (ativo == null) ativo = true;
        return produtoOutputPort.buscarComFiltros(ativo, nome, precoMin, precoMax, pageable);
    }

    @Override
    public Produto atualizarProduto(int id, Produto produtoAtualizado) {
        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        if (produtoAtualizado.getNome() != null) existente.setNome(produtoAtualizado.getNome());
        if (produtoAtualizado.getDescricao() != null) existente.setDescricao(produtoAtualizado.getDescricao());
        if (produtoAtualizado.getPreco() != null) existente.setPreco(produtoAtualizado.getPreco());
        if (produtoAtualizado.getQuantidade() != null) existente.setQuantidade(produtoAtualizado.getQuantidade());
        if (produtoAtualizado.getAtivo() != null) existente.setAtivo(produtoAtualizado.getAtivo());

        return produtoOutputPort.salvar(existente);
    }

    @Override
    public void excluirProduto(int id) {
        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        existente.setAtivo(false);
        produtoOutputPort.salvar(existente);
    }


    public void decrementarEstoque(int id, int quantidade) {
        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        if (existente.getQuantidade() - quantidade < 0) {
            throw new IllegalArgumentException("Não é possível reduzir estoque abaixo de 0");
        }

        existente.setQuantidade(existente.getQuantidade() - quantidade);
        produtoOutputPort.salvar(existente);
    }
}