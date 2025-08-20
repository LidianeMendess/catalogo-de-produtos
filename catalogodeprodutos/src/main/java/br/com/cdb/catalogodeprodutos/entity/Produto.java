package br.com.cdb.catalogodeprodutos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(unique=true, nullable = false)
    private String sku;

    @Column
    @NotBlank
    @Size(min=3, max=120)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(precision=12, scale=2)
    @DecimalMin("0.0")
    private BigDecimal preco;

    @Min(0)
    private int quantidade;
    private boolean ativo;

    @Column(name="criado_em", updatable=false)
    private LocalDateTime criadoEm;

    @Column(name="atualizado_em")
    private LocalDateTime atualizadoEm;

    public Produto(){}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    @PrePersist
    protected void datasCriacao(){
        criadoEm= LocalDateTime.now();
        atualizadoEm=LocalDateTime.now();
    }

    @PreUpdate
    protected void dataModificacao(){
        atualizadoEm=LocalDateTime.now();
    }


}
