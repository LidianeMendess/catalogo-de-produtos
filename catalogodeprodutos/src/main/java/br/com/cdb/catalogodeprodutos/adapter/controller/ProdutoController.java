package br.com.cdb.catalogodeprodutos.adapter.controller;


import br.com.cdb.catalogodeprodutos.adapter.mapper.ProdutoMapper;
import br.com.cdb.catalogodeprodutos.adapter.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import br.com.cdb.catalogodeprodutos.port.input.ProdutoInputPort;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/produtos")
@Slf4j
public class ProdutoController {

    private final ProdutoInputPort produtoInputPort;
    private final ProdutoMapper produtoMapper;

    @Autowired
    public ProdutoController(ProdutoInputPort produtoInputPort, ProdutoMapper produtoMapper) {
        this.produtoInputPort = produtoInputPort;
        this.produtoMapper = produtoMapper;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> inserirProduto(@Valid @RequestBody ProdutoRequest produtoRequest) {
        log.info("Inserindo produto: {}", produtoRequest.getNome());
        Produto produto = produtoMapper.toDomain(produtoRequest);
        Produto produtoAdicionado = produtoInputPort.createProduto(produto);
        ProdutoResponse response = produtoMapper.toResponse(produtoAdicionado);
        log.info("Produto inserido com sucesso:  id={}", produtoAdicionado.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable int id) {
        log.info("Buscando produto por id={}", id);
        Produto produto = produtoInputPort.findById(id);
        return ResponseEntity.ok(produtoMapper.toResponse(produto));

    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> buscarPorFiltros(
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) Categoria categoria
    ) {

        log.info("Buscando produtos com filtros - ativo: {}, nome: {}, precoMin: {}, precoMax: {}, limite: {}, offset: {}, categoria{}",
                ativo, nome, precoMin, precoMax, limite, offset, categoria);

        List<Produto> produtosDomain = produtoInputPort.buscarComFiltros(ativo, nome, precoMin, precoMax, limite, offset, categoria);
        List<ProdutoResponse> produtosResponse = produtoMapper.toResponseList(produtosDomain);
        return ResponseEntity.ok(produtosResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizarCampos(
            @PathVariable int id,
            @Valid @RequestBody ProdutoRequest produtoRequest) {

        log.info("Atualizando produto id={}", id);
        Produto produto = produtoMapper.toDomain(produtoRequest);
        Produto atualizado = produtoInputPort.atualizarProduto(id, produto);
        ProdutoResponse response = produtoMapper.toResponse(atualizado);
        log.info("Produto atualizado com sucesso: id={}", atualizado.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable int id) {
        log.info("Excluindo produto id={}", id);
        produtoInputPort.excluirProduto(id);
        log.info("produto excluido com sucesso id={}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("sku/{sku}")
    public ResponseEntity<ProdutoResponse> buscarPorSKU(@PathVariable String sku) {
        log.info("Buscando produto por sku={}", sku);
        Produto produto = produtoInputPort.buscarPorSku(sku);
        return ResponseEntity.ok(produtoMapper.toResponse(produto));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProdutoResponse>> buscarPorCategoria(
            @PathVariable Categoria categoria,
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(defaultValue = "0") int offset
    ) {
        log.info("Buscando produtos por categoria: {}, limite{}, offset{}", categoria, limite, offset);

        List<Produto> produtosDomain = produtoInputPort.buscarComFiltros(
                true,
                null,
                null,
                null,
                limite,
                offset,
                categoria
        );
        List<ProdutoResponse> produtoResponse = produtoMapper.toResponseList(produtosDomain);
        return ResponseEntity.ok(produtoResponse);
    }

    @PutMapping("/{id}/decrementar/{quantidade}")
    public ResponseEntity<ProdutoResponse> decrementarEstoque(
            @PathVariable int id,
            @PathVariable int quantidade
    ) {
        Produto produto = produtoInputPort.decrementarEstoque(id, quantidade);
        return ResponseEntity.ok(produtoMapper.toResponse(produto));
    }

    @GetMapping("estoqueAbaixo/{limite}")
    public ResponseEntity<List<ProdutoResponse>> buscarestoqueBaixo( @PathVariable int limite) {
    List<Produto> produtos = produtoInputPort.buscarEstoqueBaixo(limite);
    List<ProdutoResponse> produtoResponse = produtoMapper.toResponseList(produtos);;
    return ResponseEntity.ok(produtoResponse);
    }

    @GetMapping("/categoria/mais-estoque")
    public ResponseEntity<Categoria> categoriaMaisEstoque() {
        Categoria categoria = produtoInputPort.categoriaMaisEstoque();
        return ResponseEntity.ok(categoria);
    }



}
