package br.com.cdb.catalogodeprodutos.adapter.output.impl;

import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
            p.setNome(rs.getString("nome"));
            p.setDescricao(rs.getString("descricao"));
            p.setPreco(rs.getBigDecimal("preco"));
            p.setQuantidade(rs.getInt("quantidade"));
            p.setAtivo(rs.getBoolean("ativo"));
            return p;
        }
    };

    @Override
    public Produto salvar(Produto produto) {
        if (produto.getId() == null || produto.getId() == 0) {
            // INSERT
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
            // UPDATE
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
    public Page<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, Pageable pageable) {
        String sql = "SELECT * FROM produto WHERE 1=1 ";
        if (ativo != null) sql += "AND ativo = " + ativo + " ";
        if (nome != null && !nome.isEmpty()) sql += "AND LOWER(nome) LIKE LOWER('%" + nome + "%') ";
        if (precoMin != null) sql += "AND preco >= " + precoMin + " ";
        if (precoMax != null) sql += "AND preco <= " + precoMax + " ";

        List<Produto> produtos = jdbcTemplate.query(sql, produtoMapper);

        // Paginação manual simples
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), produtos.size());
        List<Produto> pagedList = produtos.subList(start, end);

        return new PageImpl<>(pagedList, pageable, produtos.size());
    }

    @Override
    public void deletarPorId(int id) {
        jdbcTemplate.update("DELETE FROM produto WHERE id = ?", id);
    }
}
