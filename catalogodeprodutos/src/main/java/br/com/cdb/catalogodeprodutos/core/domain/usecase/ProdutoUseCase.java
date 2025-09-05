package br.com.cdb.catalogodeprodutos.core.domain.usecase;

import br.com.cdb.catalogodeprodutos.core.domain.exception.ProdutoNaoEncontradoException;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.port.input.ProdutoInputPort;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public class ProdutoUseCase implements ProdutoInputPort {

    private static final Logger logger= LoggerFactory.getLogger(ProdutoUseCase.class);

    private final ProdutoOutputPort produtoOutputPort;

    public ProdutoUseCase(ProdutoOutputPort produtoOutputPort) {
        this.produtoOutputPort = produtoOutputPort;
    }

    @Override
    public Produto createProduto(Produto produto) {

        logger.info("Criando produto id={}", produto.getId());
        if (produto.getSku() == null || produto.getSku().isBlank()) {
            logger.error("SKU é obrigatório.");
            throw new IllegalArgumentException("SKU é obrigatório!");
        }

        if (produto.getPreco() != null && produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            logger.error("Preço não pode ser negativo preço={}", produto.getPreco());
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }

        if (produto.getNome() != null) {
            if (produto.getNome().length() < 3 || produto.getNome().length() > 120) {
                logger.error("Nome deve ter entre 3 e 120 caracteres: {}", produto.getNome());
                throw new IllegalArgumentException("Nome deve ter entre 3 e 120 caracteres");
            }
        }

        produto.setAtivo(true);
        logger.warn("produto salvo com sucesso: {}", produto);
        return produtoOutputPort.salvar(produto);

    }

    @Override
    public Optional<Produto> findById(int id) {
        logger.info("Buscando produto com id={}", id);

        return produtoOutputPort.buscarPorId(id)
                .map(produto -> {
                    logger.debug("Produto encontrado: {}", produto);
                    return produto;
                });
    }

    @Override
    public List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset) {
        if (ativo == null) ativo = true;

        logger.info("Buscando produtos com filtros: ativo={}, nome='{}', precoMin={}, precoMax={}, limite={}, offset={}",
                ativo, nome, precoMin, precoMax, limite, offset);

        List<Produto> produtos = produtoOutputPort.buscarComFiltros(ativo, nome, precoMin, precoMax, limite, offset);

        logger.debug("Quantidade de produtos encontrados: {}", produtos.size());

        return produtos;
    }


    @Override
    public Produto atualizarProduto(int id, Produto produtoAtualizado) {
        logger.info("Atualizando produto id={}", id);

        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.error("Produto não encontrado com id={}", id);
                   return new ProdutoNaoEncontradoException("Produto não encontrado com id: " + id);
                });

        if (produtoAtualizado.getNome() != null) {
            if (produtoAtualizado.getNome().length() < 3 || produtoAtualizado.getNome().length() > 120) {
                logger.error("Nome inválido para produto id={}: {}", id, produtoAtualizado.getNome());
                throw new IllegalArgumentException("Nome deve ter entre 3 e 120 caracteres");
            }
            existente.setNome(produtoAtualizado.getNome());
            logger.debug("Nome atualizado para produto id={}: {}", id, produtoAtualizado.getNome());
        }

        if (produtoAtualizado.getDescricao() != null) existente.setDescricao(produtoAtualizado.getDescricao());
        if (produtoAtualizado.getQuantidade() != null) {
            if (produtoAtualizado.getQuantidade() < 0) {
                logger.error("Quantidade negativa para produto id={}: {}", id, produtoAtualizado.getQuantidade());
                throw new IllegalArgumentException("Quantidade não pode ser negativa");
            }
            existente.setQuantidade(produtoAtualizado.getQuantidade());
            logger.debug("Quantidade atualizada para produto id={}: {}", id, produtoAtualizado.getQuantidade());
        }

        if (produtoAtualizado.getAtivo() != null) existente.setAtivo(produtoAtualizado.getAtivo());
        logger.debug("Status ativo atualizado para produto id={}: {}", id, produtoAtualizado.getAtivo());
        if (produtoAtualizado.getSku() != null && !produtoAtualizado.getSku().isBlank()) {
            existente.setSku(produtoAtualizado.getSku());
            logger.debug("SKU atualizado para produto id={}: {}", id, produtoAtualizado.getSku());

        }
        if (produtoAtualizado.getPreco() != null) {
            if (produtoAtualizado.getPreco().compareTo(BigDecimal.ZERO) < 0) {
                logger.error("Preço negativo para produto id={}: {}", id, produtoAtualizado.getPreco());
                throw new IllegalArgumentException("Preço não pode ser negativo");
            }
            existente.setPreco(produtoAtualizado.getPreco());
            logger.debug("Preço atualizado para produto id={}: {}", id, produtoAtualizado.getPreco());
        }
        Produto atualizado = produtoOutputPort.salvar(existente);
        logger.info("Produto atualizado com sucesso: id={}", atualizado.getId());
        return atualizado;

    }

    @Override
    public void excluirProduto(int id) {
        logger.info("Excluindo (soft delete) produto id={}", id);
        logger.info("Excluindo (soft delete) produto id={}", id);

        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.error("Produto não encontrado com id={}", id);
                    return new ProdutoNaoEncontradoException("Produto não encontrado com id: " + id);
                });

        existente.setAtivo(false);
        produtoOutputPort.salvar(existente);

        logger.info("Produto marcado como inativo com sucesso: id={}", id);
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