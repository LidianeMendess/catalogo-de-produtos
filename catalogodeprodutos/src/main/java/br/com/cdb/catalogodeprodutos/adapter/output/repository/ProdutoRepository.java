package br.com.cdb.catalogodeprodutos.adapter.output.repository;

import br.com.cdb.catalogodeprodutos.adapter.output.entity.ProdutoEntity;
import br.com.cdb.catalogodeprodutos.adapter.output.mapper.ProdutoEntityMapper;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ProdutoRepository implements ProdutoOutputPort {

    private final JdbcTemplate jdbcTemplate;
    private final ProdutoEntityMapper produtoEntityMapper;

    private final RowMapper<ProdutoEntity> rowMapper = new BeanPropertyRowMapper<>(ProdutoEntity.class);

    @Autowired
    public ProdutoRepository(JdbcTemplate jdbcTemplate, ProdutoEntityMapper produtoEntityMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.produtoEntityMapper = produtoEntityMapper;
    }

    @Override
    public Produto salvar(Produto produto) {
        if (produto.getId() == null || produto.getId() == 0) {
            log.info("Inserindo produto no banco: {}", produto.getNome());

            if (produto.getSku() == null || produto.getSku().isBlank()) {
                log.error("SKU é obrigatório para o produto: {}", produto.getNome());
                throw new IllegalArgumentException("SKU é obrigatório!");
            }

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO produto (nome, descricao, preco, quantidade, ativo, sku, criado_em, atualizado_em) VALUES (?, ?, ?, ?, ?, ?, now(), now())",
                        new String[]{"id"}
                );
                ps.setString(1, produto.getNome());
                ps.setString(2, produto.getDescricao());
                ps.setBigDecimal(3, produto.getPreco());
                ps.setInt(4, produto.getQuantidade() != null ? produto.getQuantidade() : 0);
                ps.setBoolean(5, produto.getAtivo() != null ? produto.getAtivo() : true);
                ps.setString(6, produto.getSku());
                return ps;
            }, keyHolder);

            produto.setId(keyHolder.getKey().intValue());
            log.info("Produto inserido com sucesso: {} (id={})", produto.getNome(), produto.getId());

        } else {
            log.info("Atualizando produto id={}",produto.getId());
            jdbcTemplate.update(
                    "UPDATE produto SET nome = ?, descricao = ?, preco = ?, quantidade = ?, ativo = ? WHERE id = ?",
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getQuantidade(),
                    produto.getAtivo(),
                    produto.getId()
            );
            log.info("Produto atualizado com sucesso id={}", produto.getId());
        }
        return produto;
    }

    @Override
    public Optional<Produto> buscarPorId(int id) {
        String sql= "SELECT * FROM buscar_produto_por_id(?)";
        List<ProdutoEntity> entities = jdbcTemplate.query(sql, rowMapper, id);

        return entities.isEmpty()
                ? Optional.empty()
                : Optional.of(produtoEntityMapper.toDomain(entities.get(0)));
    }

    @Override
    public List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset) {
        StringBuilder sql = new StringBuilder("SELECT * FROM produto WHERE 1=1 ");
        log.info("Buscando produtos com filtros: ativo={}, nome={}, precoMin={}, precoMax={}, limite={}, offset={}",
                ativo, nome, precoMin, precoMax, limite, offset);

        List<Object> params = new ArrayList<>();

        if (ativo != null) {
            sql.append("AND ativo = ? ");
            params.add(ativo);
        }
        if (nome != null && !nome.isEmpty()) {
            sql.append("AND LOWER(nome) LIKE ? ");
            params.add("%" + nome.toLowerCase() + "%");
        }
        if (precoMin != null) {
            sql.append("AND preco >= ? ");
            params.add(precoMin);
        }
        if (precoMax != null) {
            sql.append("AND preco <= ? ");
            params.add(precoMax);
        }

        sql.append("LIMIT ? OFFSET ?");
        params.add(limite);
        params.add(offset);

        List<ProdutoEntity> entities = jdbcTemplate.query(sql.toString(), rowMapper, params.toArray());
        return produtoEntityMapper.toDomainList(entities);
    }

    @Override
    public void deletarPorId(int id) {
        log.info("Deletando produto id={}", id);
        jdbcTemplate.update("DELETE FROM produto WHERE id = ?", id);
        log.info("Produto deletado com sucesso id={}", id);
    }

    @Override
    public List<Produto> buscarTodos(int limite, int offset) {
        String sql = "SELECT * FROM produto LIMIT ? OFFSET ?";
        List<ProdutoEntity> entities = jdbcTemplate.query(sql, rowMapper, limite, offset);
        return produtoEntityMapper.toDomainList(entities);
    }
}
