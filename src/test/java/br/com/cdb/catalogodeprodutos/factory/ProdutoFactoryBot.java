package br.com.cdb.catalogodeprodutos.factory;

import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProdutoFactoryBot {

    private Integer id = 1;
    private String sku = "SKU123";
    private String nome = "Produto teste";
    private String descricao = "Descrição padão do produto";
    private BigDecimal preco = BigDecimal.TEN;
    private Integer quantidade = 10;
    private Boolean ativo = true;
    private Categoria categoria = null;
    private Timestamp criadoEm = new Timestamp(System.currentTimeMillis());
    private Timestamp atualizadoEm = new Timestamp(System.currentTimeMillis());

    public ProdutoFactoryBot comId(Integer id){
        this.id = id;
        return this;
    }

    public ProdutoFactoryBot comSku(String sku){
        this.sku = sku;
        return this;
    }

    public ProdutoFactoryBot comNome(String nome){
        this.nome = nome;
        return this;
    }

    public ProdutoFactoryBot comDescricao(String descricao){
        this.descricao = descricao;
        return this;
    }

    public ProdutoFactoryBot comPreco(BigDecimal preco){
        this.preco = preco;
        return this;
    }

    public ProdutoFactoryBot comQuantidade(Integer quantidade){
        this.quantidade = quantidade;
        return this;
    }

    public ProdutoFactoryBot comAtivo(Boolean ativo){
        this.ativo = ativo;
        return this;
    }

    public ProdutoFactoryBot comCategoria(Categoria categoria){
        this.categoria = categoria;
        return this;
    }

    public ProdutoFactoryBot comCriadoEm(Timestamp criadoEm){
        this.criadoEm = criadoEm;
        return this;
    }

    public ProdutoFactoryBot comAtualizadoEm(Timestamp atualizadoEm){
        this.atualizadoEm = atualizadoEm;
        return this;
    }

    public Produto build() {
        return new Produto(id, sku, nome, descricao, preco, quantidade, ativo, categoria, criadoEm, atualizadoEm);
    }
}
