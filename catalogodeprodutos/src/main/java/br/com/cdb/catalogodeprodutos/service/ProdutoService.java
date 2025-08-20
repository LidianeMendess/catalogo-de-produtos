package br.com.cdb.catalogodeprodutos.service;

import br.com.cdb.catalogodeprodutos.dto.ProdutoDTO;
import br.com.cdb.catalogodeprodutos.entity.Produto;
import br.com.cdb.catalogodeprodutos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto inserirProduto(ProdutoDTO dto){
        Produto produto= new Produto();
        return produtoRepository.save(produto);
    }
}
