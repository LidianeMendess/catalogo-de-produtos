package br.com.cdb.catalogodeprodutos.adapter.output.impl;

import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Produto> produtoMapper = new RowMapper<>() {
        @Override
        public Produto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Produto p = new Produto();
            p.setId(rs.getInt("id"));
            p.setSku(rs.getString("sku"));
            p.setNome(rs.getString("nome"));
            p.setDescricao(rs.getString("descricao"));
            p.setPreco(rs.getBigDecimal("preco"));
            p.setQuantidade(rs.getInt("quantidade"));
            p.setAtivo(rs.getBoolean("ativo"));
            p.setCriadoEm(rs.getTimestamp("criado_em").toLocalDateTime());
            p.setAtualizadoEm(rs.getTimestamp("atualizado_em").toLocalDateTime());
            return p;
        }
    };

    @Override
    public Produto salvar(Produto produto) {
        if (produto.getId() == null || produto.getId() == 0) {
            jdbcTemplate.update(
                    "INSERT INTO produto (nome, descricao, preco, quantidade, ativo, criado_em, atualizado_em) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getQuantidade(),
                    produto.getAtivo(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE produto SET nome = ?, descricao = ?, preco = ?, quantidade = ?, ativo = ?, atualizado_em = ? WHERE id = ?",
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getQuantidade(),
                    produto.getAtivo(),
                    LocalDateTime.now(),
                    produto.getId()
            );
        }
        return produto;
    }

    @Override
    public Optional<Produto> buscarPorId(int id) {
        List<Produto> produtos = jdbcTemplate.query(
                "SELECT * FROM produto WHERE id = ?",
                produtoMapper,
                id
        );
        return produtos.isEmpty() ? Optional.empty() : Optional.of(produtos.get(0));
    }


    @Override
    public List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset) {
        StringBuilder sql = new StringBuilder("SELECT * FROM produto WHERE 1=1 ");
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

        return jdbcTemplate.query(sql.toString(), produtoMapper, params.toArray());
    }

    @Override
    public void deletarPorId(int id) {
        jdbcTemplate.update("DELETE FROM produto WHERE id = ?", id);
    }

    @Override
    public List<Produto> buscarTodos(int limite, int offset) {
        String sql = "SELECT * FROM produto LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, produtoMapper, limite, offset);
    }

}



