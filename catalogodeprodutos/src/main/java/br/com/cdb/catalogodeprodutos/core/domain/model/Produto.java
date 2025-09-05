package br.com.cdb.catalogodeprodutos.core.domain.model;


import java.math.BigDecimal;

public class Produto {

        private Integer id;
        private String sku;
        private String nome;
        private String descricao;
        private BigDecimal preco;
        private Integer quantidade;
        private Boolean ativo;


        public Produto(){}

    public Produto(Integer id, String sku, String nome, String descricao, BigDecimal preco, Integer quantidade, Boolean ativo) {
        this.id = id;
        this.sku = sku;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.ativo = ativo;
    }


        public Integer getId() { return id; }
        public void setId(Integer id){this.id=id;}

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

    }


