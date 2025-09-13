package br.com.cdb.catalogodeprodutos.adapter.output.repository;

import br.com.cdb.catalogodeprodutos.adapter.output.entity.CategoriaEntity;
import br.com.cdb.catalogodeprodutos.adapter.output.entity.ProdutoEntity;
import br.com.cdb.catalogodeprodutos.adapter.output.mapper.ProdutoEntityMapper;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.Timestamp;
import java.util.*;

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
        log.info("Criando produto: {}", produto);

        jdbcTemplate.update(connection -> {
            CallableStatement cs = connection.prepareCall(
                    "CALL pr_criar_produto(?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            cs.setObject(1, produto.getId(), java.sql.Types.INTEGER); // null se quiser que banco gere
            cs.setString(2, produto.getSku());
            cs.setString(3, produto.getNome());
            cs.setString(4, produto.getDescricao());
            cs.setBigDecimal(5, produto.getPreco());
            cs.setObject(6, produto.getQuantidade(), java.sql.Types.INTEGER);
            cs.setObject(7, produto.getAtivo(), java.sql.Types.BOOLEAN);
            cs.setObject(8, produto.getCriadoEm(), java.sql.Types.TIMESTAMP);
            cs.setObject(9, produto.getAtualizadoEm(), java.sql.Types.TIMESTAMP);
            return cs;
        });

        return produto;
    }


    @Override
    public Produto atualizarProduto(int id, Produto produtoAtualizado) {
        log.info("Atualizando produto id={}", id);

        jdbcTemplate.update(connection -> {
            CallableStatement cs = connection.prepareCall(
                    "CALL pr_atualizar_produtos(?::integer, ?::varchar, ?::varchar, ?::numeric, ?::integer, ?::boolean, ?::timestamp)"
            );
            cs.setInt(1, id);
            cs.setString(2, produtoAtualizado.getNome());
            cs.setString(3, produtoAtualizado.getDescricao());
            cs.setBigDecimal(4, produtoAtualizado.getPreco());
            cs.setObject(5, produtoAtualizado.getQuantidade(), java.sql.Types.INTEGER);
            cs.setObject(6, produtoAtualizado.getAtivo(), java.sql.Types.BOOLEAN);
            if (produtoAtualizado.getAtualizadoEm() != null) {
                cs.setTimestamp(7, new Timestamp(produtoAtualizado.getAtualizadoEm().getTime()));
            } else {
                cs.setNull(7, java.sql.Types.TIMESTAMP);
            }


            return cs;
        });

        produtoAtualizado.setId(id);
        return produtoAtualizado;
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
    public List<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, int limite, int offset, Categoria categoria) {
        log.info("Buscando produtos com filtros: ativo={}, nome={}, precoMin={}, precoMax={}, categoria{}, limite={}, offset={}",
                ativo, nome, precoMin, precoMax, categoria, limite, offset);

       String sql= "SELECT * FROM buscar_com_filtros_produtos(?, ?, ?, ?, ?, ?, ?)";

        CategoriaEntity categoriaEntity = (categoria != null)
                ? CategoriaEntity.valueOf(categoria.name())
                : null;

        List<ProdutoEntity> entities = jdbcTemplate.query(
                sql,
                rowMapper,
                ativo,
                nome,
                precoMin,
                precoMax,
                categoriaEntity != null ? categoriaEntity.name() : null,
                limite,
                offset
        );
        return produtoEntityMapper.toDomainList(entities);
    }

    @Override
    public void deletarPorId(int id) {
        log.info("Deletando (desativando) produto id={}", id);

        jdbcTemplate.update(
                "CALL deletar_por_id(?)", id
        );
        log.info("Produto desativado com sucesso id={}", id);
    }

    @Override
    public List<Produto> buscarTodos(int limite, int offset) {
        String sql = "SELECT * FROM buscar_todos_produtos(?, ?)";
        List<ProdutoEntity> entities = jdbcTemplate.query(sql, rowMapper, limite, offset);
        return produtoEntityMapper.toDomainList(entities);
    }

    @Override
    public Optional<Produto> buscarPorSku(String sku) {
        log.info("Buscando produto de SKU= {}", sku);
        String sql = "SELECT * FROM buscar_produto_por_sku(?)";
        List<ProdutoEntity> entities = jdbcTemplate.query(sql, rowMapper, sku);
        if (entities.isEmpty()) return Optional.empty();
        return Optional.of(produtoEntityMapper.toDomain(entities.get(0)));
    }

    @Override
    public void decrementarEstoque(int id, int quantidade){
        log.info("Decrementando {} unidades do produto de id={}", quantidade, id);
        jdbcTemplate.update("CALL decrementar_estoque(?, ?)", id, quantidade);
        log.info("Estoque decrementado com sucesso!");
    }

    @Override
    public List<Produto> buscarEstoqueBaixo(int limite){
        log.info("Buscando produtos com estoque abaixo de {}", limite);
        String sql = "SELECT*FROM buscar_estoque_baixo(?)";
        List<ProdutoEntity> entities = jdbcTemplate.query(sql, rowMapper, limite);
        return produtoEntityMapper.toDomainList(entities);
    }

    @Override
    public Categoria categoriaMaisEstoque() {
        log.info("Buscando categorias com mais estoque.");
        String sql = "SELECT categoria_mais_estoque()";
        String categoriaStr = jdbcTemplate.queryForObject(sql, String.class);
        return Categoria.valueOf(categoriaStr);
    }

}
