package br.com.cdb.catalogodeprodutos.core.domain.model;


import java.math.BigDecimal;
import java.sql.Timestamp;

public class Produto {

        private Integer id;
        private String sku;
        private String nome;
        private String descricao;
        private BigDecimal preco;
        private Integer quantidade;
        private Boolean ativo;
        private Categoria categoria;
        private Timestamp criadoEm;
        private Timestamp atualizadoEm;

        public Produto(){}

    public Produto(Integer id, String sku, String nome, String descricao, BigDecimal preco, Integer quantidade, Boolean ativo, Categoria categoria, Timestamp criadoEm, Timestamp atualizadoEm) {
        this.id = id;
        this.sku = sku;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.ativo = ativo;
        this.categoria=categoria;
        this.criadoEm=criadoEm;
        this.atualizadoEm=atualizadoEm;
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

        public Categoria getCategoria(){return categoria;}
        public void setCategoria(Categoria categoria){ this.categoria = categoria;}

        public  Timestamp getCriadoEm(){return criadoEm;}

        public Timestamp getAtualizadoEm(){return atualizadoEm;}
        public void setAtualizadoEm(Timestamp atualizadoEm){this.atualizadoEm=atualizadoEm;}



    }


