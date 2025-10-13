package br.com.cdb.catalogodeprodutos.core.usecase.strategy;

import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.core.domain.usecase.strategy.FiltroPorPreco;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;
import br.com.cdb.catalogodeprodutos.factory.ProdutoFactoryBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FiltroPorPrecoTest {

    @Mock
    ProdutoOutputPort produtoOutputPort;

    @InjectMocks
    FiltroPorPreco filtroPorPreco;

    @Test
    void deveRetornarListaDeProdutosQuandoPrecoMinOuMaxInformado() {
        Boolean ativo = true;
        Double precoMin = 10.0;
        Double precoMax = 50.0;
        int limite = 10;
        int offset = 0;

        Produto produto = new ProdutoFactoryBot()
                .comPreco(new BigDecimal("30.00"))
                .build();
        List<Produto> produtosEsperados = List.of(produto);

        when(produtoOutputPort.buscarComFiltros(ativo, null, precoMin, precoMax, limite, offset, null))
                .thenReturn(produtosEsperados);

        List<Produto> resultado = filtroPorPreco.filtrar(ativo, null, precoMin, precoMax, limite, offset, null);

        assertEquals(produtosEsperados, resultado);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoInformarPrecoMinENemMax() {
        List<Produto> resultado = filtroPorPreco.filtrar(true, null, null, null, 10, 0, null);
        assertEquals(Collections.emptyList(), resultado);
    }

    @Test
    void deveUsarAtivoTrueQuandoForNulo() {
        Double precoMin = 5.0;
        Double precoMax = 15.0;
        List<Produto> produtos = List.of(new ProdutoFactoryBot()
                .comPreco(new BigDecimal("10.00"))
                .build());

        when(produtoOutputPort.buscarComFiltros(true, null, precoMin, precoMax, 10, 0, null))
                .thenReturn(produtos);

        List<Produto> resultado = filtroPorPreco.filtrar(null, null, precoMin, precoMax, 10, 0, null);

        assertEquals(produtos, resultado);
    }

    @Test
    void deveRetornarListaDeProdutosQuandoSomentePrecoMinInformado() {
        Double precoMin = 20.0;
        Double precoMax = null;
        List<Produto> produtos = List.of(new ProdutoFactoryBot()
                .comPreco(new BigDecimal("25.00"))
                .build());

        when(produtoOutputPort.buscarComFiltros(true, null, precoMin, precoMax, 10, 0, null))
                .thenReturn(produtos);

        List<Produto> resultado = filtroPorPreco.filtrar(true, null, precoMin, precoMax, 10, 0, null);

        assertEquals(produtos, resultado);
    }

    @Test
    void deveRetornarListaDeProdutosQuandoSomentePrecoMaxInformado() {
        Double precoMin = null;
        Double precoMax = 100.0;
        List<Produto> produtos = List.of(new ProdutoFactoryBot()
                .comPreco(new BigDecimal("80.00"))
                .build());

        when(produtoOutputPort.buscarComFiltros(true, null, precoMin, precoMax, 10, 0, null))
                .thenReturn(produtos);

        List<Produto> resultado = filtroPorPreco.filtrar(true, null, precoMin, precoMax, 10, 0, null);

        assertEquals(produtos, resultado);
    }
}
