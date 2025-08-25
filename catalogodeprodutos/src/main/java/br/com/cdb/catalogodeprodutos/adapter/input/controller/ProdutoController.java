package br.com.cdb.catalogodeprodutos.adapter.input.controller;


import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.port.input.ProdutoInputPort;
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

    private final ProdutoInputPort produtoInputPort;

    @Autowired
    public ProdutoController(ProdutoInputPort produtoInputPort) {
        this.produtoInputPort = produtoInputPort;
    }

    @PostMapping
    public ResponseEntity<Produto> inserirProduto(@Valid @RequestBody ProdutoRequest produtoRequest) {
        Produto produtoAdicionado = produtoInputPort.createProduto(produtoRequest.toDomain());
        return new ResponseEntity<>(produtoAdicionado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable int id) {
        return produtoInputPort.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Produto>> buscarPorFiltros(
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            Pageable pageable) {

        Page<Produto> produtos = produtoInputPort.buscarComFiltros(ativo, nome, precoMin, precoMax, pageable);
        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarCampos(
            @PathVariable int id,
            @Valid @RequestBody Produto produto) {

        Produto atualizado = produtoInputPort.atualizarProduto(id, produto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable int id) {
        produtoInputPort.excluirProduto(id);
        return ResponseEntity.noContent().build();
    }
}
