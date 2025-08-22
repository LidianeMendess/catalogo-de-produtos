package br.com.cdb.catalogodeprodutos.controller;


import br.com.cdb.catalogodeprodutos.dto.ProdutoDTO;
import br.com.cdb.catalogodeprodutos.entity.Produto;
import br.com.cdb.catalogodeprodutos.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<String> inserirProduto(@Valid @RequestBody ProdutoDTO dto){
        Produto produtoAdicionado= produtoService.inserirProduto(dto);
        return new ResponseEntity<>("Produto "+produtoAdicionado.getId()+" adicionado!", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity <ProdutoDTO>buscarPorId(@PathVariable int id){
        Produto produto=produtoService.buscarPorId(id);
        return ResponseEntity.ok(new ProdutoDTO(produto));
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>>buscarPorFiltros(
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            Pageable pageable){
        Page<ProdutoDTO> produtos= produtoService.buscarComFiltros(ativo, nome, precoMin, precoMax,pageable).map(ProdutoDTO::new);
        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarCampos(@PathVariable int id, @Valid @RequestBody ProdutoDTO dto){
        Produto produto=produtoService.atualizarProduto(id, dto);
        return ResponseEntity.ok(new ProdutoDTO(produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable int id){
        produtoService.excluirProduto(id);
        return ResponseEntity.noContent().build();
    }

}
