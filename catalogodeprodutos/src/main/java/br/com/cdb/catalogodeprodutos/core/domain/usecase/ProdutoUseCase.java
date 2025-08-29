package br.com.cdb.catalogodeprodutos.core.domain.usecase;

import br.com.cdb.catalogodeprodutos.core.domain.exception.ProdutoNaoEncontradoException;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.port.input.ProdutoInputPort;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoUseCase implements ProdutoInputPort {


    private final ProdutoOutputPort produtoOutputPort;

    public ProdutoUseCase(ProdutoOutputPort produtoOutputPort) {
        this.produtoOutputPort = produtoOutputPort;
    }

    @Override
    public Produto createProduto(Produto produto) {
        if (produto.getSku() == null || produto.getSku().isBlank()) {
            throw new IllegalArgumentException("SKU é obrigatório!");
        }

        if (produto.getPreco() != null && produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }

        if (produto.getNome() != null) {
            if (produto.getNome().length() < 3 || produto.getNome().length() > 120) {
                throw new IllegalArgumentException("Nome deve ter entre 3 e 120 caracteres");
            }
        }

        produto.setAtivo(true);
        return produtoOutputPort.salvar(produto);
    }

    @Override
    public Optional<Produto> findById(int id) {
        return produtoOutputPort.buscarPorId(id);
    }

    @Override
    public List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset) {
        if (ativo == null) ativo = true;
        return produtoOutputPort.buscarComFiltros(ativo, nome, precoMin, precoMax, limite, offset);
    }

    @Override
    public Produto atualizarProduto(int id, Produto produtoAtualizado) {
        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado com id: " + id));

        if (produtoAtualizado.getNome() != null) {
            if (produtoAtualizado.getNome().length() < 3 || produtoAtualizado.getNome().length() > 120) {
                throw new IllegalArgumentException("Nome deve ter entre 3 e 120 caracteres");
            }
            existente.setNome(produtoAtualizado.getNome());
        }

        if (produtoAtualizado.getDescricao() != null) existente.setDescricao(produtoAtualizado.getDescricao());
        if (produtoAtualizado.getQuantidade() != null) {
            if (produtoAtualizado.getQuantidade() < 0) {
                throw new IllegalArgumentException("Quantidade não pode ser negativa");
            }
            existente.setQuantidade(produtoAtualizado.getQuantidade());
        }

        if (produtoAtualizado.getAtivo() != null) existente.setAtivo(produtoAtualizado.getAtivo());
        if (produtoAtualizado.getSku() != null && !produtoAtualizado.getSku().isBlank()) {
            existente.setSku(produtoAtualizado.getSku());
        }
        if (produtoAtualizado.getPreco() != null) {
            if (produtoAtualizado.getPreco().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Preço não pode ser negativo");
            }
            existente.setPreco(produtoAtualizado.getPreco());
        }
        existente.setAtualizadoEm(LocalDateTime.now());
        return produtoOutputPort.salvar(existente);
    }

    @Override
    public void excluirProduto(int id) {
        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado com id: " + id));

        existente.setAtivo(false);
        produtoOutputPort.salvar(existente);
    }

    public void decrementarEstoque(int id, int quantidade) {
        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado com id: " + id));

        if (existente.getQuantidade() - quantidade < 0) {
            throw new IllegalArgumentException("Não é possível reduzir estoque abaixo de 0");
        }

        existente.setQuantidade(existente.getQuantidade() - quantidade);
        produtoOutputPort.salvar(existente);
    }
}