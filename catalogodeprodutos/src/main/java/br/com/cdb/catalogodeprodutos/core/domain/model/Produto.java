package br.com.cdb.catalogodeprodutos.core.domain.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Produto {

        private Integer id;
        private String sku;
        private String nome;
        private String descricao;
        private BigDecimal preco;
        private Integer quantidade;
        private Boolean ativo;
        private LocalDateTime criadoEm;
        private LocalDateTime atualizadoEm;

        public Produto(){}

    public Produto(Integer id, String sku, String nome, String descricao, BigDecimal preco, Integer quantidade, Boolean ativo, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.sku = sku;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }


    public Integer getId() { return id; }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public BigDecimal getPreco() { return preco; }
        public void setPreco(BigDecimal preco) { this.preco = preco; }

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

        public Boolean getAtivo() { return ativo; }
        public void setAtivo(boolean ativo) { this.ativo = ativo; }

        public LocalDateTime getCriadoEm() { return criadoEm; }
        public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

        public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
        public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }


    }


