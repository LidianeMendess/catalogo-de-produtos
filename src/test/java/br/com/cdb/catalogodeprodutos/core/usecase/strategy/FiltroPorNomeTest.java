package br.com.cdb.catalogodeprodutos.core.usecase.strategy;


import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.core.domain.usecase.strategy.FiltroPorNome;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;
import br.com.cdb.catalogodeprodutos.factory.ProdutoFactoryBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FiltroPorNomeTest {

    @Mock
    ProdutoOutputPort produtoOutputPort;

    @InjectMocks
    FiltroPorNome filtroPorNome;

    @Test
    void deveRetornarListaDeProdutosQuandoNomeValido() {
        Boolean ativo = true;
        String nome = "Ração";
        Double precoMin = null;
        Double precoMax = null;
        int limite = 10;
        int offset = 0;
        Categoria categoria = null;

        Produto produto = new ProdutoFactoryBot().comNome(nome).build();
        List<Produto> produtosEsperados = List.of(produto);

        when(produtoOutputPort.buscarComFiltros(ativo, nome, null, null, limite, offset, null))
                .thenReturn(produtosEsperados);

        List<Produto> resultado = filtroPorNome.filtrar(ativo, nome, precoMin, precoMax, limite, offset, categoria);

        assertEquals(produtosEsperados, resultado);
    }

    @Test
    void deveRetornarListaVaziaQuandoNomeForNulo() {
        List<Produto> resultado = filtroPorNome.filtrar(true, null, null, null, 10, 0, null);
        assertEquals(Collections.emptyList(), resultado);
    }

    @Test
    void deveRetornarListaVaziaQuandoNomeForVazio() {
        List<Produto> resultado = filtroPorNome.filtrar(true, "   ", null, null, 10, 0, null);
        assertEquals(Collections.emptyList(), resultado);
    }

    @Test
    void deveUsarAtivoTrueQuandoForNulo() {
        String nome = "Brinquedo";
        List<Produto> produtos = List.of(new ProdutoFactoryBot().comNome(nome).build());

        when(produtoOutputPort.buscarComFiltros(true, nome, null, null, 10, 0, null))
                .thenReturn(produtos);

        List<Produto> resultado = filtroPorNome.filtrar(null, nome, null, null, 10, 0, null);

        assertEquals(produtos, resultado);
    }
}
