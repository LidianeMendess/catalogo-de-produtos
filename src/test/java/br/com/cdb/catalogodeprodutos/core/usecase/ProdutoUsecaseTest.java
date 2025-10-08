package br.com.cdb.catalogodeprodutos.core.usecase;

import br.com.cdb.catalogodeprodutos.core.domain.exception.ProdutoNaoEncontradoException;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.core.domain.usecase.ProdutoUseCase;
import br.com.cdb.catalogodeprodutos.factory.ProdutoFactoryBot;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class ProdutoUsecaseTest {

    @Mock
    ProdutoOutputPort produtoOutputPort;

    @InjectMocks
    ProdutoUseCase produtoUsecase;

    @ParameterizedTest(name = "ID inválido: {0}")
    @ValueSource(ints = {0, -1})
    void idNaoExisteErro(int id){

        assertThatThrownBy(() -> produtoUsecase.findById(id))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                .hasMessage("Produto com ID " + id+ " não encontrado");

        verify(produtoOutputPort, never()).salvar(any());
    }

    @ParameterizedTest(name =  "SKU inválido: \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void criarProdutoSkuObrigatorio(String sku){
        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .comSku(sku)
                .build();

        assertThatThrownBy(() -> produtoUsecase.createProduto(produto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("SKU é obrigatório!");

        verify(produtoOutputPort, never()).salvar(any());
    }

    @Test
    void criarProdutoSkuDuplicadoErro(){

        Produto produto = new ProdutoFactoryBot()
                .comSku("ABC123")
                .build();
        when(produtoOutputPort.buscarPorSku("ABC123")).thenReturn(Optional.of(produto));

        assertThatThrownBy(() -> produtoUsecase.createProduto(produto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("SKU já existe!");

        verify(produtoOutputPort, never()).salvar(any());
    }

    @ParameterizedTest(name = "Nome inválido: {0}")
    @ValueSource(strings = {"A", "AB", "NOMEDEVECONTERNOMAXIMOCENTOEVINTECARACTERESNOMEDEVECONTERNOMAXIMOCENTOEVINTECARACTERESNOMEDEVECONTERNOMAXIMOCENTOEVINTECA"})
    void lancarExcecaoParaNomeInvalido(String nome){
        Produto produto = new ProdutoFactoryBot().comNome(nome).build();

        assertThatThrownBy(() -> produtoUsecase.createProduto(produto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome deve ter entre 3 e 120 caracteres");
    }

    @ParameterizedTest(name = "preço inválido (negativo): {0}")
    @ValueSource(strings = {"-0.01", "-10"})
    void lancarExcecaoPrecoNegativo(String precoStr){
        BigDecimal preco = new BigDecimal(precoStr);
        Produto produto = new ProdutoFactoryBot().comPreco(preco).build();

        assertThatThrownBy(() -> produtoUsecase.createProduto(produto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço não pode ser negativo");
    }

    @Test
    void lancarExcecaoPrecoNulo(){
        Produto produto = new ProdutoFactoryBot().comPreco(null).build();

        assertThatThrownBy(() -> produtoUsecase.createProduto(produto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço é obrigatório!");
    }

    @Test
    void aceitarPrecoZero(){
        Produto produto = new ProdutoFactoryBot()
                .comPreco(BigDecimal.ZERO)
                .comId(1)
                .comSku("SKUZERO")
                .comNome("Produto Zero")
                .build();

        when(produtoOutputPort.buscarPorId(1)).thenReturn(Optional.empty());
        when(produtoOutputPort.buscarPorSku("SKUZERO")).thenReturn(Optional.empty());
        when(produtoOutputPort.salvar(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

        Produto salvo = produtoUsecase.createProduto(produto);

        assertThat(salvo.getPreco()).isEqualByComparingTo(BigDecimal.ZERO);
        verify(produtoOutputPort).salvar(produto);
    }


    @Test
    void criarProdutoOK(){
        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .comSku("ABC123")
                .comNome("ProdtoTeste")
                .comPreco(new BigDecimal("100.00"))
                .build();

        when(produtoOutputPort.buscarPorId(1)).thenReturn(Optional.empty());
        when(produtoOutputPort.buscarPorSku("ABC123")).thenReturn(Optional.empty());

        when(produtoOutputPort.salvar(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Produto produtoSalvo = produtoUsecase.createProduto(produto);

        assertEquals(produto, produtoSalvo);

        verify(produtoOutputPort).salvar(produto);

    }
    @Test
    void atualizarSkuErro(){

      Produto existente = new ProdutoFactoryBot()
              .comId(1)
              .comSku("ABC123")
              .build();

      Produto atualizado = new ProdutoFactoryBot()
              .comId(1)
              .comSku("XYZ456")
              .build();

       when(produtoOutputPort.buscarPorId(1)).thenReturn(Optional.of(existente));

       assertThatThrownBy(() -> produtoUsecase.atualizarProduto(1, atualizado))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage("SKU é imutável e não pode ser alterado!");

        verify(produtoOutputPort, never()).atualizarProduto(anyInt(), any());
    }

    @Test
    void atualizarProdutoLancaExcecaoQuandoQuantidadeNegativa() {
        int id = 1;
        Produto existente = new ProdutoFactoryBot().comId(id).build();
        Produto atualizado = new ProdutoFactoryBot().comId(id).comQuantidade(-5).build();

        when(produtoOutputPort.buscarPorId(id)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> produtoUsecase.atualizarProduto(id, atualizado))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantidade não pode ser negativa");

        verify(produtoOutputPort, never()).salvar(any(Produto.class));
    }

    @Test
    void atualizarProdutoQuandoNaoEncontradoLancaExcecao() {
        int id = 999;
        Produto atualizado = new ProdutoFactoryBot()
                .comId(id)
                .comSku("SKU999")
                .build();

        when(produtoOutputPort.buscarPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoUsecase.atualizarProduto(id, atualizado))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                .hasMessage("Produto não encontrado com id: " + id);

        verify(produtoOutputPort, never()).atualizarProduto(anyInt(), any());
    }

    @Test
    void atualizarProdutoOk(){
        Produto existente = new ProdutoFactoryBot()
                .comId(1)
                .comSku("ABC123")
                .comNome("Original")
                .comPreco(new BigDecimal("50.00"))
                .comQuantidade(10)
                .build();

        Produto atualizado = new ProdutoFactoryBot()
                .comId(1)
                .comSku("ABC123")
                .comNome("Atualizado")
                .comPreco(new BigDecimal("60.00"))
                .comQuantidade(20)
                .build();

        when(produtoOutputPort.buscarPorId(1)).thenReturn(Optional.of(existente));
        when(produtoOutputPort.atualizarProduto(1, existente)).thenAnswer(invocation -> invocation.getArgument(1));

        Produto resultado = produtoUsecase.atualizarProduto(1, atualizado);

        assertEquals("Atualizado", resultado.getNome());
        assertEquals(20, resultado.getQuantidade());
        assertEquals(new BigDecimal("60.00"), resultado.getPreco());
        verify(produtoOutputPort).atualizarProduto(1, existente);
    }

    @ParameterizedTest(name = "quantidade inválida: {0}")
    @ValueSource(ints = {1, 5, 10})
    void decrementarEstoqueErro(int quantidade) {
        Produto produto = new ProdutoFactoryBot().comQuantidade(0).build();
        int produtoId = produto.getId();

        when(produtoOutputPort.buscarPorId(produto.getId()))
                .thenReturn(Optional.of(produto));

        assertThatThrownBy(() -> produtoUsecase.decrementarEstoque(produtoId, quantidade))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Não é possível reduzir estoque abaixo de 0");
    }

    @Test
    void decrementarEstoqueQuandoNaoEncontradoLancaExcecao() {
        int id = 999;
        when(produtoOutputPort.buscarPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoUsecase.decrementarEstoque(id, 5))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                .hasMessage("Produto não encontrado com id: " + id);

        verify(produtoOutputPort, never()).atualizarProduto(anyInt(), any());
    }

    @Test
    void decrementarEstoqueOk(){
        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .comQuantidade(10)
                .build();

        Produto produtoAtualizado = new ProdutoFactoryBot()
                .comId(1)
                .comQuantidade(7)
                .build();

        int quantidadeAReduzir = 3;
        when(produtoOutputPort.buscarPorId(produto.getId()))
                .thenReturn((Optional.of(produto)));
        when(produtoOutputPort.atualizarProduto(produto.getId(), produto))
                .thenReturn((produtoAtualizado));

        Produto resultado = produtoUsecase.decrementarEstoque(produto.getId(), quantidadeAReduzir);

            assertEquals(7, resultado.getQuantidade());
            verify(produtoOutputPort).atualizarProduto(produto.getId(), produto);

    }

    @ParameterizedTest(name = "limite: {0}")
    @ValueSource(ints = {5, 10, 20})
    void buscarEstoqueBaixoProdutoNaoEncontrado(int limite) {
        when(produtoOutputPort.buscarEstoqueBaixo(limite))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> produtoUsecase.buscarEstoqueBaixo(limite))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                .hasMessage("Não há produtos com estoque abaixo de: " + limite);
    }

    @Test
    void buscarEstoqueBaixoComProdutosok() {
        Produto p1 = new ProdutoFactoryBot().comQuantidade(3).build();
        Produto p2 = new ProdutoFactoryBot().comQuantidade(2).build();

        when(produtoOutputPort.buscarEstoqueBaixo(5))
                .thenReturn(List.of(p1, p2));

        List<Produto> resultado = produtoUsecase.buscarEstoqueBaixo(5);

        assertThat(resultado).hasSize(2).containsExactlyInAnyOrder(p1, p2);
    }

    @Test
    void categoriaMaisEstoqueQuandoNenhumProdutoAtivoEntaoLancaExcecao() {
         when(produtoOutputPort.buscarComFiltros(true, null, null, null, Integer.MAX_VALUE, 0, null))
                 .thenReturn(Collections.emptyList());

         assertThatThrownBy(() -> produtoUsecase.categoriaMaisEstoque())
                                    .isInstanceOf(ProdutoNaoEncontradoException.class)
                                    .hasMessage("Nenhum produto ativo encontrado!");
    }

    @Test
    void categoriaMaisEstoqueQuandoProdutosSemCategoriaEntaoLancaExcecao() {
        Produto produto = new ProdutoFactoryBot()
                .comCategoria(null)
                .comQuantidade(10)
                .build();

            when(produtoOutputPort.buscarComFiltros(true, null, null, null, Integer.MAX_VALUE, 0, null))
                    .thenReturn(List.of(produto));

            assertThatThrownBy(() -> produtoUsecase.categoriaMaisEstoque())
                    .isInstanceOf(ProdutoNaoEncontradoException.class)
                    .hasMessage("Nenhum produto com categoria válida encontrado!");
    }

    @Test
    void categoriaMaisEstoqueQuandoProdutosValidoEntaoRetornaCategoriaComMaisEstoque() {
        Produto p1 = new ProdutoFactoryBot()
                .comCategoria(Categoria.GATO)
                .comQuantidade(10)
                .build();

        Produto p2 = new ProdutoFactoryBot()
                .comCategoria(Categoria.CAO)
                .comQuantidade(20)
                .build();

        when(produtoOutputPort.buscarComFiltros(true, null, null, null, Integer.MAX_VALUE, 0, null))
                    .thenReturn(List.of(p1, p2));

        Categoria categoria = produtoUsecase.categoriaMaisEstoque();

        assertThat(categoria).isEqualTo(Categoria.CAO);
    }

    @Test
    void categoriaMaisEstoqueQuandoQuantidadeNullEntaoConsideraZero() {
        Produto p1 = new ProdutoFactoryBot()
                .comCategoria(Categoria.GATO)
                .comQuantidade(null)
                .build();

        Produto p2 = new ProdutoFactoryBot()
                .comCategoria(Categoria.CAO)
                .comQuantidade(1)
                .build();

        when(produtoOutputPort.buscarComFiltros(true, null, null, null, Integer.MAX_VALUE, 0, null))
                    .thenReturn(List.of(p1, p2));

        Categoria categoria = produtoUsecase.categoriaMaisEstoque();

            assertThat(categoria).isEqualTo(Categoria.CAO);
    }


    @Test
    void buscarComFiltrosComAtivoNuloDeveConsiderarTrue() {
        Produto produto = new ProdutoFactoryBot().comId(1).build();

        when(produtoOutputPort.buscarComFiltros(eq(true), any(), any(), any(), anyInt(), anyInt(), any()))
                .thenReturn(List.of(produto));

        List<Produto> resultado = produtoUsecase.buscarComFiltros(
                null,
                "Produto",
                10.0,
                100.0,
                5,
                0,
                Categoria.CAO
        );

        assertThat(resultado).hasSize(1);
        verify(produtoOutputPort).buscarComFiltros(eq(true), eq("Produto"), eq(10.0), eq(100.0), eq(5), eq(0), eq(Categoria.CAO));
    }



    @Test
    void buscarTodosQuandoLimiteMenorOuIgualZeroEntaoLancaExcecao() {
        assertThatThrownBy(() -> produtoUsecase.buscarTodos(0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O limite deve ser maior que 0");

        assertThatThrownBy(() -> produtoUsecase.buscarTodos(-5, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O limite deve ser maior que 0");
    }

    @Test
    void buscarTodosQuandoOffsetNegativoEntaoLancaExcecao() {
        assertThatThrownBy(() -> produtoUsecase.buscarTodos(10, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O offset não pode ser negativo");
    }

    @Test
    void buscarTodosQuandoParametrosValidosEntaoRetornaListaDeProdutos() {
        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .comSku("ABC123")
                .build();

        when(produtoOutputPort.buscarTodos(10, 0))
                .thenReturn(List.of(produto));

        List<Produto> resultado = produtoUsecase.buscarTodos(10, 0);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getSku()).isEqualTo("ABC123");
    }

    @Test
    void buscarTodosQuandoNaoExistemProdutosEntaoRetornaListaVazia() {
        when(produtoOutputPort.buscarTodos(10, 0))
                .thenReturn(Collections.emptyList());

        List<Produto> resultado = produtoUsecase.buscarTodos(10, 0);

        assertThat(resultado).isEmpty();
    }

    @Test
    void buscarPorSkuRetornaProdutoQuandoEncontrado() {
        String sku = "SKU123";
        Produto produto = new ProdutoFactoryBot().comSku(sku).build();

        when(produtoOutputPort.buscarPorSku(sku)).thenReturn(Optional.of(produto));

        Produto resultado = produtoUsecase.buscarPorSku(sku);

        assertThat(resultado).isEqualTo(produto);
        verify(produtoOutputPort).buscarPorSku(sku);
    }

    @Test
    void buscarPorSkuLancaExcecaoQuandoNaoEncontrado() {
        String sku = "SKU123";

        when(produtoOutputPort.buscarPorSku(sku)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoUsecase.buscarPorSku(sku))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                .hasMessage("Produto não encontrado com SKU: SKU123");

        verify(produtoOutputPort).buscarPorSku(sku);
    }

    @Test
    void excluirProdutoOk(){
        int id=1;

        when(produtoOutputPort.buscarPorId(id))
                .thenReturn((Optional.of(new ProdutoFactoryBot().comId(id).build())));

        produtoUsecase.excluirProduto(id);

        verify(produtoOutputPort).deletarPorId(id);
    }

    @Test
    void excluirProdutoNaoExisteNaoLancaExcecao() {
        int id = 1;
        when(produtoOutputPort.buscarPorId(id))
                .thenReturn(Optional.empty());

        produtoUsecase.excluirProduto(id);

        verify(produtoOutputPort, never()).deletarPorId(id);
    }

}





