package br.com.cdb.catalogodeprodutos.adapter.output.mapper;

import br.com.cdb.catalogodeprodutos.adapter.output.entity.ProdutoEntity;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.factory.ProdutoEntityFactoryBot;
import br.com.cdb.catalogodeprodutos.factory.ProdutoFactoryBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProdutoEntityMapperTest {

    @Autowired
    private ProdutoEntityMapper mapper;

    @Test
    void deveMapearEntityParaDomain(){
        ProdutoEntity entity = new ProdutoEntityFactoryBot()
                .comId(1)
                .comNome("Brinquedo")
                .comDescricao("Bolinha")
                .comPreco(new BigDecimal(20.00))
                .build();

        Produto domain = mapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1);
        assertThat(domain.getNome()).isEqualTo("Brinquedo");
        assertThat(domain.getDescricao()).isEqualTo("Bolinha");
        assertThat(domain.getPreco()).isEqualByComparingTo(BigDecimal.valueOf(20.00));
    }

    @Test
    void deveMapearDomainParaEntity(){
        Produto produto = new ProdutoFactoryBot()
                .comId(1)
                .comNome("Brinquedo")
                .comDescricao("Bolinha")
                .comPreco(new BigDecimal(20.00))
                .build();

        ProdutoEntity entity = mapper.toEntity(produto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1);
        assertThat(entity.getNome()).isEqualTo("Brinquedo");
        assertThat(entity.getDescricao()).isEqualTo("Bolinha");
        assertThat(entity.getPreco()).isEqualByComparingTo(BigDecimal.valueOf(20.00));
    }

    @Test
    void deveMapearListaEntityParaListaDomain() {
        ProdutoEntity entity1 = new ProdutoEntityFactoryBot()
                .comId(1)
                .comNome("Brinquedo")
                .comDescricao("Bolinha")
                .comPreco(new BigDecimal(20.00))
                .build();

        ProdutoEntity entity2 = new ProdutoEntityFactoryBot()
                .comId(1)
                .comNome("Remédio")
                .comDescricao("Ômega 3")
                .comPreco(new BigDecimal(20.00))
                .build();

        List<Produto> domains = mapper.toDomainList(List.of(entity1, entity2));

        assertThat(domains).isNotNull();
        assertThat(domains.get(0).getNome()).isEqualTo("Brinquedo");
        assertThat(domains.get(1).getNome()).isEqualTo("Remédio");
    }

    @Test
    void deveMapearListaDomainParaListaEntity() {
        Produto produto1 = new ProdutoFactoryBot()
                .comId(1)
                .comNome("Brinquedo")
                .comDescricao("Bolinha")
                .comPreco(new BigDecimal(20.00))
                .build();

        Produto produto2 = new ProdutoFactoryBot()
                .comId(1)
                .comNome("Remédio")
                .comDescricao("ômega 3")
                .comPreco(new BigDecimal(20.00))
                .build();

        List<ProdutoEntity> entities = mapper.toEntityList(List.of(produto1, produto2));

        assertThat(entities).isNotNull();
        assertThat(entities.get(0).getNome()).isEqualTo("Brinquedo");
        assertThat(entities.get(1).getNome()).isEqualTo("Remédio");
    }
}
