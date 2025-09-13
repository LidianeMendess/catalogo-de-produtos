package br.com.cdb.catalogodeprodutos.core.domain.usecase;

import br.com.cdb.catalogodeprodutos.core.domain.exception.ProdutoNaoEncontradoException;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.port.input.ProdutoInputPort;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class ProdutoUseCase implements ProdutoInputPort {

    private static final Logger logger= LoggerFactory.getLogger(ProdutoUseCase.class);

    private final ProdutoOutputPort produtoOutputPort;
    private final JdbcTemplate jdbcTemplate;

    public ProdutoUseCase(ProdutoOutputPort produtoOutputPort, JdbcTemplate jdbcTemplate) {
        this.produtoOutputPort = produtoOutputPort;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Produto createProduto(Produto produto) {

        logger.info("Criando produto id={}", produto.getId());

        if (produto.getId() == null) {
            logger.error("ID é obrigatório.");
            throw new IllegalArgumentException("ID não pode ser nulo!");
        }

        if (produtoOutputPort.buscarPorId(produto.getId()).isPresent()) {
            logger.error("Já existe um produto com ID={}", produto.getId());
            throw new IllegalArgumentException("ID já existe!");
        }
        if (produto.getSku() == null || produto.getSku().isBlank()) {
            logger.error("SKU é obrigatório.");
            throw new IllegalArgumentException("SKU é obrigatório!");
        }

        Optional<Produto> existenteSku = produtoOutputPort.buscarPorSku(produto.getSku());
        if (existenteSku.isPresent()) {
            logger.error("SKU já existe: {}", produto.getSku());
            throw new IllegalArgumentException("SKU já existe!");
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
    public Produto findById(int id) {
        logger.info("Buscando produto com id={}", id);

        return produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto com ID " + id+ " não encontrado"));
    }

    @Override
    public List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset, Categoria categoria) {
        if (ativo == null) ativo = true;

        logger.info("Buscando produtos com filtros: ativo={}, nome='{}', precoMin={}, precoMax={}, limite={}, offset={}, categoria={}",
                ativo, nome, precoMin, precoMax, limite, offset, categoria);

        List<Produto> produtos = produtoOutputPort.buscarComFiltros(ativo, nome, precoMin, precoMax, limite, offset, categoria);

        logger.debug("Quantidade de produtos encontrados: {}", produtos.size());

        return produtos;
    }

    @Override
    public Produto buscarPorSku(String sku) {
        logger.info("Buscando produto pelo SKU={}", sku);
        return produtoOutputPort.buscarPorSku(sku)
           .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto com SKU " + sku + " não encontrado"));
    }

    @Override
    public Produto atualizarProduto(int id, Produto produtoAtualizado) {
        logger.info("Atualizando produto id={}", id);

        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.error("Produto não encontrado com id={}", id);
                   return new ProdutoNaoEncontradoException("Produto não encontrado com id: " + id);
                });

        if (produtoAtualizado.getSku() != null && !produtoAtualizado.getSku().equals(existente.getSku())) {
            logger.error("Tentativa de alterar SKU do produto id={}", id);
            throw new IllegalArgumentException("SKU é imutável e não pode ser alterado!");
        }

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

        if (produtoAtualizado.getPreco() != null) {
            if (produtoAtualizado.getPreco().compareTo(BigDecimal.ZERO) < 0) {
                logger.error("Preço negativo para produto id={}: {}", id, produtoAtualizado.getPreco());
                throw new IllegalArgumentException("Preço não pode ser negativo");
            }
            existente.setPreco(produtoAtualizado.getPreco());
            logger.debug("Preço atualizado para produto id={}: {}", id, produtoAtualizado.getPreco());
        }

        if (produtoAtualizado.getCategoria() !=null){
            logger.info("Atualizando categoria");
            existente.setCategoria(produtoAtualizado.getCategoria());
        }
        Produto atualizado = produtoOutputPort.atualizarProduto(id, existente);
        logger.info("Produto atualizado com sucesso: id={}", atualizado.getId());
        return atualizado;

    }

    @Override
    public void excluirProduto(int id) {
        logger.info("Excluindo (soft delete) produto id={}", id);

        produtoOutputPort.deletarPorId(id);

        logger.info("Produto marcado como inativo com sucesso: id={}", id);
    }
    @Override
    public Produto decrementarEstoque(int id, int quantidade) {
        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado com id: " + id));

        int novoEstoque = existente.getQuantidade() - quantidade;
        if (novoEstoque < 0) {
            throw new IllegalArgumentException("Não é possível reduzir estoque abaixo de 0");
        }

        String sql = "CALL prr_atualizar_produto(?, NULL, NULL, NULL, ?, NULL, ?)";
        Timestamp agora = Timestamp.valueOf(LocalDateTime.now());
        jdbcTemplate.update(sql, id, novoEstoque, agora);

        existente.setQuantidade(novoEstoque);
        existente.setAtualizadoEm(agora);

        return existente;
    }

    @Override
    public List<Produto> buscarEstoqueBaixo(int limite) {
        logger.info("Buscando produtos com estoque abaixo de: {}", limite);
        List<Produto> produtos = produtoOutputPort.buscarEstoqueBaixo(limite);

        if (produtos.isEmpty()) {
            throw new ProdutoNaoEncontradoException("Não há produtos com estoque abaixo de: " + limite);
        }
        return produtos;
    }

    @Override
    public Categoria categoriaMaisEstoque() {
        List<Produto> produtosAtivos = produtoOutputPort.buscarComFiltros(
                true, null, null, null, Integer.MAX_VALUE, 0, null);

        if (produtosAtivos == null || produtosAtivos.isEmpty()) {
            logger.warn("Nenhum produto ativo encontrado");
            throw new ProdutoNaoEncontradoException("Nenhum produto ativo encontrado!");
        }

        Map<Categoria, Integer> estoquePorCategoria = produtosAtivos.stream()
                .filter(p -> p.getCategoria() != null)
                .collect(Collectors.groupingBy(
                        Produto::getCategoria,
                        Collectors.summingInt(p -> p.getQuantidade() != null ? p.getQuantidade() : 0)
                ));

        if (estoquePorCategoria.isEmpty()) {
            logger.warn("Nenhum produto com categoria válida encontrado");
            throw new ProdutoNaoEncontradoException("Nenhum produto com categoria válida encontrado!");
        }

        Categoria categoriaMaisEstoque = estoquePorCategoria.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        logger.info("Categoria com maior estoque: {}", categoriaMaisEstoque);
        return categoriaMaisEstoque;
    }

}
