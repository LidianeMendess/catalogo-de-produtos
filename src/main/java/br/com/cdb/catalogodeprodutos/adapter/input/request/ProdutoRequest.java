package br.com.cdb.catalogodeprodutos.adapter.input.request;

import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest {

    @Schema(description = "Identificador único do produto", example = "1")
    private Integer id;

    @Schema(description = "Código SKU do produto", example = "SKU123")
    private String sku;

    @Schema(description= "Nome do produto", example = "Ração Premium")
    private String nome;

    @Schema(description ="Descrição do produto", example = "Ração Premium para gatos castrados.")
    private String descricao;

    @Schema(description = "Preço unitário", example = "59.90")
    private BigDecimal preco;

    @Schema(description = "Quantidade em estoque", example = "50")
    private Integer quantidade;

    @Schema(description = "Indica se está ativo", example = "true")
    private Boolean ativo;

    @Schema(description = "Categoria", example = "GATO", allowableValues = { "CAO", "GATO", "PASSARO", "PEIXE", "OUTROS"})
    private Categoria categoria;
}



