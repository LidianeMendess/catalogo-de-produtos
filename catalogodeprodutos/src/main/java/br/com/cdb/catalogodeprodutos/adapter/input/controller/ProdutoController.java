package br.com.cdb.catalogodeprodutos.adapter.input.controller;


import br.com.cdb.catalogodeprodutos.adapter.input.mapper.ProdutoMapper;
import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.input.response.ProdutoResponse;
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
        Produto produto=produtoMapper.toDomain(produtoRequest);
        Produto produtoAdicionado = produtoInputPort.createProduto(produto);
        ProdutoResponse response= produtoMapper.toResponse(produtoAdicionado);
        log.info("Produto inserido com sucesso:  id={}", produtoAdicionado.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable int id) {
        return produtoInputPort.findById(id)
                .map(produtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> buscarPorFiltros(
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(defaultValue = "0") int offset
            ) {

        log.info("Buscando produtos com filtros - ativo: {}, nome: {}, precoMin: {}, precoMax: {}, limite: {}, offset: {}",
                ativo, nome, precoMin, precoMax, limite, offset);

        List<Produto> produtosDomain = produtoInputPort.buscarComFiltros(ativo, nome, precoMin, precoMax, limite, offset);
        List<ProdutoResponse> produtosResponse = produtoMapper.toResponseList(produtosDomain);
        return ResponseEntity.ok(produtosResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizarCampos(
            @PathVariable int id,
            @Valid @RequestBody ProdutoRequest produtoRequest) {

        log.info("Atualizando produto id={}", id);
        Produto produto=produtoMapper.toDomain(produtoRequest);
        Produto atualizado = produtoInputPort.atualizarProduto(id, produto);
        ProdutoResponse response= produtoMapper.toResponse(atualizado);
        log.info("Produto atualizado com sucesso: id={}", atualizado.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable int id) {
        log.info("Excluindo produto id={}",id);
        produtoInputPort.excluirProduto(id);
        log.info("produto excluido com sucesso id={}", id);
        return ResponseEntity.noContent().build();
    }
}
