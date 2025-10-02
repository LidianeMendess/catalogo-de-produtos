package br.com.cdb.catalogodeprodutos.infrastructure;

import br.com.cdb.catalogodeprodutos.core.domain.usecase.ProdutoUseCase;
import br.com.cdb.catalogodeprodutos.port.output.ProdutoOutputPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProdutoUsecaseBean extends ProdutoUseCase {

    public ProdutoUsecaseBean(ProdutoOutputPort produtoOutputPort, JdbcTemplate jdbcTemplate) {
        super(produtoOutputPort, jdbcTemplate);
    }
}
