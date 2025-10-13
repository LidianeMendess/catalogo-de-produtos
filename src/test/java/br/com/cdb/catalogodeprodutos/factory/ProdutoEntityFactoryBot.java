package br.com.cdb.catalogodeprodutos.factory;

import br.com.cdb.catalogodeprodutos.adapter.output.entity.ProdutoEntity;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProdutoEntityFactoryBot {

    private Integer id = 1;
    private String sku = "SKU123";
    private String nome = "ProdutoEntity teste";
    private String descricao = "Descrição padrão entity";
    private BigDecimal preco = BigDecimal.TEN;
    private Integer quantidade = 10;
    private Boolean ativo = true;
    private Categoria categoria = Categoria.CAO;
    private Timestamp criadoEm = new Timestamp(System.currentTimeMillis());
    private Timestamp atualizadoEm = new Timestamp(System.currentTimeMillis());

    public ProdutoEntityFactoryBot comId(Integer id){
        this.id = id;
        return this;
    }

    public ProdutoEntityFactoryBot comSku(String sku){
        this.sku = sku;
        return this;
    }

    public ProdutoEntityFactoryBot comNome(String nome){
        this.nome = nome;
        return this;
    }

    public ProdutoEntityFactoryBot comDescricao(String descricao){
        this.descricao = descricao;
        return this;
    }

    public ProdutoEntityFactoryBot comPreco(BigDecimal preco){
        this.preco = preco;
        return this;
    }

    public ProdutoEntityFactoryBot comQuantidade(Integer quantidade){
        this.quantidade = quantidade;
        return this;
    }

    public ProdutoEntityFactoryBot comAtivo(Boolean ativo){
        this.ativo = ativo;
        return this;
    }

    public ProdutoEntityFactoryBot comCategoria(Categoria categoria){
        this.categoria = categoria;
        return this;
    }

    public ProdutoEntityFactoryBot comCriadoEm(Timestamp criadoEm){
        this.criadoEm = criadoEm;
        return this;
    }

    public ProdutoEntityFactoryBot comAtualizadoEm(Timestamp atualizadoEm){
        this.atualizadoEm = atualizadoEm;
        return this;
    }

    public ProdutoEntity build() {
        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(id);
        entity.setSku(sku);
        entity.setNome(nome);
        entity.setDescricao(descricao);
        entity.setPreco(preco);
        entity.setQuantidade(quantidade);
        entity.setAtivo(ativo);
        entity.setCategoria(categoria);
        entity.setCriado_em(criadoEm);
        entity.setAtualizado_em(atualizadoEm);
        return entity;
    }
}
