package br.com.cdb.catalogodeprodutos.infrastructure;


import br.com.cdb.catalogodeprodutos.core.domain.usecase.ProdutoFiltroContexto;
import br.com.cdb.catalogodeprodutos.core.domain.usecase.strategy.FiltroPorNome;
import br.com.cdb.catalogodeprodutos.core.domain.usecase.strategy.FiltroPorPreco;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProdutoFiltroContextoBean {

    @Bean
    public ProdutoFiltroContexto produtoFiltroContexto(FiltroPorNome filtroPorNome, FiltroPorPreco filtroPorPreco) {
        return new ProdutoFiltroContexto(filtroPorNome, filtroPorPreco);
    }

}
