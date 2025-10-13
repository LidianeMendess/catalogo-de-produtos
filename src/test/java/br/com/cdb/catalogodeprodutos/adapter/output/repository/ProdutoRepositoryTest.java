package br.com.cdb.catalogodeprodutos.adapter.output.repository;

import br.com.cdb.catalogodeprodutos.adapter.output.entity.ProdutoEntity;
import br.com.cdb.catalogodeprodutos.adapter.output.mapper.ProdutoEntityMapper;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.factory.ProdutoEntityFactoryBot;
import br.com.cdb.catalogodeprodutos.factory.ProdutoFactoryBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class ProdutoRepositoryTest {

    @Mock
    JdbcTemplate jdbcTemplate;

    @Mock
    ProdutoEntityMapper produtoEntityMapper;

    @InjectMocks
    ProdutoRepository produtoRepository;

    private void mockMapper(ProdutoEntity entity, Produto domain) {
        when(produtoEntityMapper.toDomainList(List.of(entity)))
                .thenReturn(List.of(domain));
        when(produtoEntityMapper.toDomain(entity)).thenReturn(domain);
    }

    @Test
    void salvarProdutoOk() {
        Produto produto = new ProdutoFactoryBot()
                .comSku("SKU123")
                .comNome("Produto Teste")
                .comDescricao("Descrição teste")
                .comPreco(new BigDecimal("100.00"))
                .comQuantidade(10)
                .comAtivo(true)
                .comCategoria(Categoria.CAO)
                .build();

        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setSku(produto.getSku());
        produtoEntity.setNome(produto.getNome());
        produtoEntity.setDescricao(produto.getDescricao());
        produtoEntity.setPreco(produto.getPreco());
        produtoEntity.setQuantidade(produto.getQuantidade());
        produtoEntity.setCategoria(produto.getCategoria());

        when(produtoEntityMapper.toEntity(produto)).thenReturn(produtoEntity);

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(produtoEntity);

        mockMapper(produtoEntity, produto);

        Produto resultado = produtoRepository.salvar(produto);

        assertNotNull(resultado);
    }

    @Test
    void salvarProdutoErro() {
        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .comSku("SKU123")
                .comNome("Produto teste")
                .comCategoria(Categoria.GATO)
                .build();

        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                any(Object[].class)
        )).thenThrow(new RuntimeException("Erro ao salvar"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> produtoRepository.salvar(produto)
        );

        assertEquals("Erro ao salvar", exception.getMessage());

        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), any(Object[].class));
    }


    @Test
    void atualizarProdutoComAtualizadoEmNulo() {
        int id = 1;
        Produto produto = new ProdutoFactoryBot()
                .comId(id)
                .comAtualizadoEm(null)
                .build();

        Produto atualizado = produtoRepository.atualizarProduto(id, produto);

        assertEquals(id, atualizado.getId());
        assertNull(atualizado.getAtualizadoEm());
    }

    @Test
    void atualizarProdutoComCamposNulos() {
        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .comNome(null)
                .comDescricao(null)
                .comPreco(null)
                .comQuantidade(null)
                .comAtivo(null)
                .build();

        when(jdbcTemplate.update(any(PreparedStatementCreator.class))).thenReturn(1);

        Produto atualizado = produtoRepository.atualizarProduto(1, produto);

        assertEquals(1, atualizado.getId());
        verify(jdbcTemplate).update(any(PreparedStatementCreator.class));
    }


    @Test
    void atualizarProdutoOk() {
        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .comSku("SKU123")
                .comNome("Produto teste")
                .comDescricao("Descrição padrão do produto")
                .comPreco(BigDecimal.TEN)
                .comQuantidade(10)
                .comAtivo(true)
                .comCategoria(null)
                .comAtualizadoEm(new java.sql.Timestamp(System.currentTimeMillis()))
                .build();

        when(jdbcTemplate.update(any(PreparedStatementCreator.class))).thenReturn(1);

        Produto atualizado = produtoRepository.atualizarProduto(produto.getId(), produto);

        assertEquals(produto.getId(), atualizado.getId());
        assertEquals(produto.getNome(), atualizado.getNome());
        assertEquals(produto.getDescricao(), atualizado.getDescricao());

        verify(jdbcTemplate).update(any(PreparedStatementCreator.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarPorIdQuandoNaoEncontrado() {
        when(jdbcTemplate.query(anyString(), (RowMapper<ProdutoEntity>) any(), eq(1)))
                .thenReturn(List.of());

        Optional<Produto> resultado = produtoRepository.buscarPorId(1);

        assertTrue(resultado.isEmpty());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(1));
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarPorIdProdutoEncontrado() {
        ProdutoEntity produtoEntity = new ProdutoEntityFactoryBot()
                .comId(1)
                .build();

        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .build();

        when(jdbcTemplate.query(anyString(), (RowMapper<ProdutoEntity>) any(), eq(1)))
                .thenReturn(List.of(produtoEntity));

        mockMapper(produtoEntity, produto);

        Optional<Produto> resultado = produtoRepository.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(1));
        verify(produtoEntityMapper).toDomain(produtoEntity);
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarPorIdLancaExcecao() {
        when(jdbcTemplate.query(anyString(), (RowMapper<ProdutoEntity>) any(), eq(1)))
                .thenThrow(new RuntimeException("Erro no banco"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoRepository.buscarPorId(1));

        assertEquals("Erro no banco", exception.getMessage());
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarComFiltrosRetornaProdutos() {
        ProdutoEntity produtoEntity = new ProdutoEntityFactoryBot()
                .comId(1)
                .build();

        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .build();

        List<ProdutoEntity> entities = List.of(produtoEntity);

        when(jdbcTemplate.query(
                anyString(),
                any(RowMapper.class),
                anyBoolean(), anyString(), anyDouble(), anyDouble(),
                anyString(), anyInt(), anyInt()))
                .thenReturn(entities);

        mockMapper(produtoEntity, produto);

        List<Produto> resultado = produtoRepository.buscarComFiltros(
                true,
                "Produto teste",
                10.0,
                100.0,
                10,
                0,
                Categoria.CAO
        );

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId());

        verify(jdbcTemplate).query(
                anyString(),
                any(RowMapper.class),
                eq(true),
                eq("Produto teste"),
                eq(10.0),
                eq(100.0),
                eq("CAO"),
                eq(10),
                eq(0)
        );
        verify(produtoEntityMapper).toDomainList(entities);
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarComFiltrosCategoriaNula() {
        ProdutoEntity produtoEntity = new ProdutoEntityFactoryBot()
                .comId(1)
                .build();
        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .build();

        when(jdbcTemplate.query(
                anyString(),
                (RowMapper<ProdutoEntity>) any(RowMapper.class),
                eq(true),
                eq(null),
                eq(20.0),
                eq(null),
                eq(null),
                eq(5),
                eq(0)
        )).thenReturn(List.of(produtoEntity));

        mockMapper(produtoEntity, produto);

        List<Produto> resultado = produtoRepository.buscarComFiltros(
                true,
                null,
                20.0,
                null,
                5,
                0,
                null
        );

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId());
    }

    @Test
    void buscarComFiltrosComCategoriaPreenchida() {
        ProdutoEntity entity = new ProdutoEntityFactoryBot().comId(1).build();
        Produto produto = new ProdutoFactoryBot().comId(1).build();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class),
                anyBoolean(), anyString(), anyDouble(), anyDouble(),
                eq("CAO"), anyInt(), anyInt()))
                .thenReturn(List.of(entity));

        when(produtoEntityMapper.toDomainList(List.of(entity))).thenReturn(List.of(produto));

        List<Produto> resultado = produtoRepository.buscarComFiltros(
                true, "nome", 10.0, 20.0, 5, 0, Categoria.CAO
        );

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId());
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarComFiltrosParciais() {
        ProdutoEntity produtoEntity = new ProdutoEntityFactoryBot().comId(2).build();
        Produto produto = new ProdutoFactoryBot().comId(2).build();

        when(jdbcTemplate.query(
                anyString(),
                (RowMapper<ProdutoEntity>) any(RowMapper.class),
                eq(true),
                isNull(),
                eq(20.0),
                isNull(),
                isNull(),
                eq(5),
                eq(0)
        )).thenReturn(List.of(produtoEntity));

        mockMapper(produtoEntity, produto);

        List<Produto> resultado = produtoRepository.buscarComFiltros(
                true,
                null,
                20.0,
                null,
                5,
                0,
                null
        );

        assertEquals(1, resultado.size());
        assertEquals(2, resultado.get(0).getId());
    }

    @Test
    void deletarPorIdDeveChamarJdbcUpdate(){
        int id = 10;

        produtoRepository.deletarPorId(id);

        verify(jdbcTemplate).update("CALL deletar_por_id(?)",id);
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarTodosDeveRetornarListaDeProdutos(){
        int limite = 10;
        int offset = 0;

        ProdutoEntity produtoEntity = new ProdutoEntityFactoryBot().comId(1).build();
        Produto produto = new ProdutoFactoryBot().comId(1).build();

        when(jdbcTemplate.query(anyString(), (RowMapper<ProdutoEntity>) any(RowMapper.class), eq(limite), eq(offset)))
                .thenReturn(List.of(produtoEntity));

        mockMapper(produtoEntity, produto);

                List<Produto> resultado = produtoRepository.buscarTodos(limite, offset);

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId());

        verify(jdbcTemplate).query(eq("SELECT * FROM buscar_todos(?, ?)"), any(RowMapper.class), eq(limite), eq(offset));

        verify(produtoEntityMapper).toDomainList((List.of(produtoEntity)));
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarTodosLancaExcecao() {
        when(jdbcTemplate.query(anyString(), (RowMapper<ProdutoEntity>) any(), eq(10), eq(0)))
                .thenThrow(new RuntimeException("Erro ao buscar todos"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoRepository.buscarTodos(10, 0));

        assertEquals("Erro ao buscar todos", exception.getMessage());
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarPorSkuEmptyQuandoNaoEncontrado() {
        when(jdbcTemplate.query
                (anyString(),
                (RowMapper<ProdutoEntity>) any(RowMapper.class),
                 anyString()
        )).thenReturn(List.of());

        Optional<Produto> resultado = produtoRepository.buscarPorSku("SKU123");

        assertTrue(resultado.isEmpty());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class),anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarPorSkuProdutoEncontrado() {
        ProdutoEntity produtoEntity = new ProdutoEntityFactoryBot()
                .comSku("SKU123")
                .build();

        Produto produto = new ProdutoFactoryBot()
                .comSku("SKU123")
                .build();

        when(jdbcTemplate.query
                (anyString(),
                (RowMapper<ProdutoEntity>) any(RowMapper.class),
                anyString()
                )).thenReturn(List.of(produtoEntity));

        mockMapper(produtoEntity, produto);

        Optional<Produto> resultado = produtoRepository.buscarPorSku("SKU123");

        assertTrue(resultado.isPresent());
        assertEquals("SKU123", resultado.get().getSku());


        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), anyString());
        verify(produtoEntityMapper).toDomain(produtoEntity);
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarPorSkuLancaExcecao() {
        when(jdbcTemplate.query(anyString(), (RowMapper<ProdutoEntity>) any(), anyString()))
                .thenThrow(new RuntimeException("Erro ao buscar por SKU"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoRepository.buscarPorSku("SKU123"));

        assertEquals("Erro ao buscar por SKU", exception.getMessage());
    }


    @Test
    void decrementarEstoqueDeveChamarJdbcUpdate(){
        int id=1;
        int quantidade= 5;

        produtoRepository.decrementarEstoque(id, quantidade);

        verify(jdbcTemplate).update("CALL decrementar_estoque(?, ?)", id, quantidade);
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarEstoqueBaixoLancaExcecao() {
        when(jdbcTemplate.query(anyString(), (RowMapper<ProdutoEntity>) any(), eq(5)))
                .thenThrow(new RuntimeException("Erro ao buscar estoque baixo"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoRepository.buscarEstoqueBaixo(5));

        assertEquals("Erro ao buscar estoque baixo", exception.getMessage());
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarEstoqueBaixoRetornaOk(){
        int limite=5;
        ProdutoEntity produtoEntity = new ProdutoEntityFactoryBot().comQuantidade(limite).build();
        Produto produto = new ProdutoFactoryBot().comQuantidade(limite).build();

        when(jdbcTemplate.query(anyString(), (RowMapper<ProdutoEntity>) any(RowMapper.class), eq(limite)))
                .thenReturn(List.of(produtoEntity));

        mockMapper(produtoEntity, produto);

        List<Produto> resultado = produtoRepository.buscarEstoqueBaixo(limite);

        assertEquals(1, resultado.size());
        assertEquals(limite, resultado.get(0).getQuantidade());

        verify(jdbcTemplate).query(eq("SELECT*FROM buscar_estoque_baixo(?)"), any(RowMapper.class), eq(limite));

        verify(produtoEntityMapper).toDomainList((List.of(produtoEntity)));
    }

    @Test
    void buscarCategoriaMaisEstoqueOk() {
        String categoriaEsperadaStr = "CAO";
        Categoria categoriaEsperada = Categoria.CAO;

        when(jdbcTemplate.queryForObject(anyString(), eq(String.class)))
                .thenReturn(categoriaEsperadaStr);

        Categoria resultado = produtoRepository.categoriaMaisEstoque();

        assertEquals(categoriaEsperada, resultado);
        verify(jdbcTemplate).queryForObject("SELECT categoria_mais_estoque()", String.class);
    }

    @Test
    void categoriaMaisEstoqueLancaExcecao() {
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("Erro ao buscar categoria"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoRepository.categoriaMaisEstoque());

        assertEquals("Erro ao buscar categoria", exception.getMessage());
    }

    @Test
    void categoriaMaisEstoqueRetornaNullDeveLancarExcecao() {
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class))).thenReturn(null);

        assertThrows(NullPointerException.class, () -> produtoRepository.categoriaMaisEstoque());
    }
}






