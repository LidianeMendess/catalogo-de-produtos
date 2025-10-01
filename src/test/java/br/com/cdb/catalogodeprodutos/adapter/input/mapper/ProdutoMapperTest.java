package br.com.cdb.catalogodeprodutos.adapter.input.mapper;

import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.input.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.factory.ProdutoFactoryBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
class ProdutoMapperTest {

    private final ProdutoMapper mapper = ProdutoMapper.INSTANCE;

    @Test
    void deveMapearRequestParaDomain(){
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Ração");
        request.setDescricao("Ração para gato");
        request.setPreco(BigDecimal.valueOf(180.00));

        Produto produto = mapper.toDomain(request);

       assertThat(produto).isNotNull();
       assertThat(produto.getNome()).isEqualTo("Ração");
       assertThat(produto.getDescricao()).isEqualTo("Ração para gato");
       assertThat(produto.getPreco()).isEqualByComparingTo(BigDecimal.valueOf(180.00));
   }

  @Test
  void deveMapearDomainParaResponse(){
   Produto produto = new Produto();
   produto.setId(1);
   produto.setNome("Brinquedo");
   produto.setDescricao("Bolinha");
   produto.setPreco(BigDecimal.valueOf(20.00));

   ProdutoResponse response = mapper.toResponse(produto);

   assertThat(response).isNotNull();
   assertThat(response.getId()).isEqualTo(1);
   assertThat(response.getNome()).isEqualTo("Brinquedo");
   assertThat(response.getDescricao()).isEqualTo("Bolinha");
   assertThat(response.getPreco()).isEqualByComparingTo(BigDecimal.valueOf(20.00));

  }

  @Test
    void deveMapearListaDomainParaListaResponse(){
        Produto produto1 = new ProdutoFactoryBot().comNome("Brinquedo").build();
        Produto produto2 = new ProdutoFactoryBot().comNome("Remédio").build();

        List<ProdutoResponse> responses = mapper.toResponseList(List.of(produto1, produto2));

        assertThat(responses).isNotNull();
        assertThat(responses.get(0).getNome()).isEqualTo("Brinquedo");
      assertThat(responses.get(1).getNome()).isEqualTo("Remédio");
    }
}
