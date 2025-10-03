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
import java.util.stream.Collectors;


public class ProdutoUseCase implements ProdutoInputPort {

    private static final Logger logger= LoggerFactory.getLogger(ProdutoUseCase.class);

    private final ProdutoOutputPort produtoOutputPort;

    public ProdutoUseCase(ProdutoOutputPort produtoOutputPort) {
        this.produtoOutputPort = produtoOutputPort;
     }

    @Override
    public Produto createProduto(Produto produto) {
        logger.info("Criando produto id={}", produto.getId());

        validarId(produto.getId());
        validarSku(produto.getSku());
        validarPreco(produto.getPreco());
        validarNome(produto.getNome());

        produto.setAtivo(true);

        logger.warn("Produto salvo com sucesso: {}", produto);
        return produtoOutputPort.salvar(produto);
    }

    @Override
    public Produto atualizarProduto(int id, Produto produtoAtualizado) {
        logger.info("Atualizando produto id={}", id);

        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.error("Produto não encontrado com id={}", id);
                    return new ProdutoNaoEncontradoException("Produto não encontrado com id: " + id);
                });

        validarSkuAtualizacao(produtoAtualizado.getSku(), existente.getSku());
        atualizarCamposExistentes(existente, produtoAtualizado);

        Produto atualizado = produtoOutputPort.atualizarProduto(id, existente);
        logger.info("Produto atualizado com sucesso: id={}", atualizado.getId());
        return atualizado;
    }

    private void validarId(Integer id) {
        if (id == null) {
            logger.error("ID é obrigatório.");
            throw new IllegalArgumentException("ID não pode ser nulo!");
        }
        if (produtoOutputPort.buscarPorId(id).isPresent()) {
            logger.error("Já existe um produto com ID={}", id);
            throw new IllegalArgumentException("ID já existe!");
        }
    }

    private void validarSku(String sku) {
        if (sku == null || sku.isBlank()) {
            logger.error("SKU é obrigatório.");
            throw new IllegalArgumentException("SKU é obrigatório!");
        }
        if (produtoOutputPort.buscarPorSku(sku).isPresent()) {
            logger.error("SKU já existe: {}", sku);
            throw new IllegalArgumentException("SKU já existe!");
        }
    }

    private void validarSkuAtualizacao(String skuNovo, String skuExistente) {
        if (skuNovo != null && !skuNovo.equals(skuExistente)) {
            logger.error("Tentativa de alterar SKU do produto");
            throw new IllegalArgumentException("SKU é imutável e não pode ser alterado!");
        }
    }

    private void validarNome(String nome) {
        if (nome != null && (nome.length() < 3 || nome.length() > 120)) {
            logger.error("Nome deve ter entre 3 e 120 caracteres: {}", nome);
            throw new IllegalArgumentException("Nome deve ter entre 3 e 120 caracteres");
        }
    }

    private void validarPreco(BigDecimal preco) {
        if (preco == null) {
            logger.error("Preço é obrigatório!");
            throw new IllegalArgumentException("Preço é obrigatório!");
        }
        if (preco.compareTo(BigDecimal.ZERO) < 0) {
            logger.error("Preço não pode ser negativo preço={}", preco);
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
    }


    private void atualizarCamposExistentes(Produto existente, Produto atualizado) {
        if (atualizado.getNome() != null) existente.setNome(atualizado.getNome());
        if (atualizado.getDescricao() != null) existente.setDescricao(atualizado.getDescricao());
        if (atualizado.getQuantidade() != null) {
            if (atualizado.getQuantidade() < 0) {
                logger.error("Quantidade não pode ser negativa: {}", atualizado.getQuantidade());
                throw new IllegalArgumentException("Quantidade não pode ser negativa");
            }
            existente.setQuantidade(atualizado.getQuantidade());
        }
        if (atualizado.getPreco() != null) validarPreco(atualizado.getPreco());
        if (atualizado.getPreco() != null) existente.setPreco(atualizado.getPreco());
        if (atualizado.getAtivo() != null) existente.setAtivo(atualizado.getAtivo());
        if (atualizado.getCategoria() != null) existente.setCategoria(atualizado.getCategoria());
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
           .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado com SKU: " + sku));
    }

    @Override
    public void excluirProduto(int id) {
        logger.info("Excluindo (soft delete) produto id={}", id);

        produtoOutputPort.buscarPorId(id).ifPresentOrElse(produto -> {
            produtoOutputPort.deletarPorId(id);
            logger.info("Produto marcado como inativo com sucesso: id={}", id);
        }, () -> {
            logger.info("Produto com id={} não encontrado para exclusão (soft delete ignorado)", id);
        });
    }

    @Override
    public Produto decrementarEstoque(int id, int quantidade) {
        Produto existente = produtoOutputPort.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado com id: " + id));

        int novoEstoque = existente.getQuantidade() - quantidade;
        if (novoEstoque < 0) {
            throw new IllegalArgumentException("Não é possível reduzir estoque abaixo de 0");
        }

        existente.setQuantidade(novoEstoque);
        existente.setAtualizadoEm(Timestamp.valueOf(LocalDateTime.now()));

        return produtoOutputPort.atualizarProduto(id, existente);
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

    @Override
    public List<Produto> buscarTodos(int limite, int offset){
        logger.info("Buscando todos os produtos com limite={} e offset={}", limite, offset);

        if (limite <= 0) {
            logger.error("Limite inválido: {}", limite);
            throw new IllegalArgumentException("O limite deve ser maior que 0");
        }
        if (offset < 0) {
            logger.error("Offset inválido: {}", offset);
            throw new IllegalArgumentException("O offset não pode ser negativo");
        }

        List<Produto> produtos = produtoOutputPort.buscarTodos(limite, offset);

        logger.debug("Quantidade de produtos encontrados: {}", produtos.size());

        return produtos;
    }

}
