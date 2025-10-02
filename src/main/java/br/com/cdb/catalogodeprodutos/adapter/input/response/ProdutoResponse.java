package br.com.cdb.catalogodeprodutos.adapter.input.response;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponse {
    private Integer id;
    private String sku;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidade;
    private Boolean ativo;
    private Categoria categoria;
}



