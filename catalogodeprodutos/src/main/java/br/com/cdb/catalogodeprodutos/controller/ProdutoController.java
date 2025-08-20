package br.com.cdb.catalogodeprodutos.controller;


import br.com.cdb.catalogodeprodutos.dto.ProdutoDTO;
import br.com.cdb.catalogodeprodutos.entity.Produto;
import br.com.cdb.catalogodeprodutos.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<String> inserirProduto(@Valid @RequestBody ProdutoDTO dto){
        Produto produtoAdicionado= produtoService.inserirProduto(dto);
        return new ResponseEntity<>("Produto "+dto.getId()+" adicionado!", HttpStatus.CREATED);
    }
}
