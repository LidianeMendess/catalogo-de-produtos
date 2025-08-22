package br.com.cdb.catalogodeprodutos.service;

import br.com.cdb.catalogodeprodutos.dto.ProdutoDTO;
import br.com.cdb.catalogodeprodutos.entity.Produto;
import br.com.cdb.catalogodeprodutos.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto inserirProduto(ProdutoDTO dto){
        Produto produto= new Produto();
        BeanUtils.copyProperties(dto, produto);
        produto.setAtivo(true);
        return produtoRepository.save(produto);
    }

    public Produto buscarPorId(int id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));
    }

    public Page<Produto> buscarComFiltros(Boolean ativo, String nome, Double precoMin, Double precoMax, Pageable pageable){
        return produtoRepository.buscarComFiltros(ativo, nome, precoMin, precoMax, pageable);
    }

    public Produto atualizarProduto(int id, ProdutoDTO dto){
        Produto produto= buscarPorId(id);
        if(dto.getNome() != null) produto.setNome(dto.getNome());
        if(dto.getDescricao() != null) produto.setDescricao(dto.getDescricao());
        if(dto.getPreco() != null) produto.setPreco(dto.getPreco());
        if(dto.getQuantidade() != null) produto.setQuantidade(dto.getQuantidade());
        if(dto.getAtivo() != null) produto.setAtivo(dto.getAtivo());
        return produtoRepository.save(produto);
    }

    public void excluirProduto( int id){
        Produto produto=buscarPorId(id);
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    public void decrementarEstoque(int id, int quantidade){
        Produto produto=buscarPorId(id);
        if(produto.getQuantidade() - quantidade<0){
            throw new IllegalArgumentException("Não é possível reduzir estoque abaixo de 0");
        }
        produto.setQuantidade(produto.getQuantidade()-quantidade);
        produtoRepository.save(produto);
    }
}
