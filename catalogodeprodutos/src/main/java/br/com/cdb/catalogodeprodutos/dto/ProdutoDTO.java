package br.com.cdb.catalogodeprodutos.dto;

import br.com.cdb.catalogodeprodutos.entity.Produto;
import com.fasterxml.jackson.databind.util.BeanUtil;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProdutoDTO {

    private Integer id;

    private String sku;

    @NotBlank
    @Size(min=3, max=120)
    private String nome;

    @Size(max=255)
    private String descricao;

    @DecimalMin("0.0")
    private BigDecimal preco;

    @Min(0)
    private Integer quantidade;
    private Boolean ativo;

    public ProdutoDTO(){}

    public ProdutoDTO(Produto produto) {
        this.id = produto.getId();
        this.sku = produto.getSku();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.quantidade = produto.getQuantidade();
        this.ativo = produto.getAtivo();
    }

    public Integer getId() { return id; }

    public String getSku() { return sku; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }


}
