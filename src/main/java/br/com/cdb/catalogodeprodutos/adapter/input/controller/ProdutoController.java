package br.com.cdb.catalogodeprodutos.adapter.input.controller;

import br.com.cdb.catalogodeprodutos.adapter.input.controller.api.SwaggerProdutoController;
import br.com.cdb.catalogodeprodutos.adapter.input.facade.ProdutoFacade;
import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.input.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class ProdutoController implements SwaggerProdutoController {

    private final ProdutoFacade produtoFacade;

    public ProdutoController(ProdutoFacade produtoFacade) {
        this.produtoFacade = produtoFacade;
    }

    @Override
    public ResponseEntity<ProdutoResponse> inserirProduto(@Valid @RequestBody ProdutoRequest produtoRequest) {
        log.info("[CONTROLLER] Inserindo produto: {}", produtoRequest.getNome());
        ProdutoResponse response = produtoFacade.inserirProduto(produtoRequest);
        log.info("[CONTROLLER] Produto inserido com sucesso: id={}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable int id) {
        log.info("[CONTROLLER] Buscando produto por id={}", id);
        return ResponseEntity.ok(produtoFacade.buscarPorId(id));
    }

    @Override
    public ResponseEntity<List<ProdutoResponse>> buscarPorFiltros(
            @RequestParam(required = false) String tipoFiltro,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) Categoria categoria
    ) {
        log.info("[CONTROLLER] Buscando produtos com filtros...");
        return ResponseEntity.ok(produtoFacade.buscarPorFiltros(tipoFiltro, ativo, nome, precoMin, precoMax, limite, offset, categoria));
    }

    @Override
    public ResponseEntity<ProdutoResponse> atualizarProduto(@PathVariable int id, @Valid @RequestBody ProdutoRequest request) {
        log.info("[CONTROLLER] Atualizando produto id={}", id);
        ProdutoResponse response = produtoFacade.atualizarProduto(id, request);
        log.info("[CONTROLLER] Produto atualizado com sucesso: id={}", response.getId());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> excluirProduto(@PathVariable int id) {
        log.info("[CONTROLLER] Excluindo produto id={}", id);
        produtoFacade.excluirProduto(id);
        log.info("[CONTROLLER] Produto excluído com sucesso id={}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProdutoResponse> buscarPorSku(@PathVariable String sku) {
        log.info("[CONTROLLER] Buscando produto por sku={}", sku);
        return ResponseEntity.ok(produtoFacade.buscarPorSku(sku));
    }

    @Override
    public ResponseEntity<List<ProdutoResponse>> buscarPorCategoria(
            @PathVariable Categoria categoria,
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(defaultValue = "0") int offset
    ) {
        log.info("[CONTROLLER] Buscando produtos por categoria={}, limite={}, offset={}", categoria, limite, offset);
        return ResponseEntity.ok(produtoFacade.buscarPorCategoria(categoria, limite, offset));
    }

    @Override
    public ResponseEntity<ProdutoResponse> decrementarEstoque(@PathVariable int id, @PathVariable int quantidade) {
        log.info("[CONTROLLER] Decrementando estoque do produto id={} em quantidade={}", id, quantidade);
        return ResponseEntity.ok(produtoFacade.decrementarEstoque(id, quantidade));
    }

    @Override
    public ResponseEntity<List<ProdutoResponse>> buscarEstoqueBaixo(@PathVariable int limite) {
        log.info("[CONTROLLER] Buscando produtos com estoque abaixo de {}", limite);
        return ResponseEntity.ok(produtoFacade.buscarEstoqueBaixo(limite));
    }

    @Override
    public ResponseEntity<Categoria> categoriaMaisEstoque() {
        log.info("[CONTROLLER] Buscando categoria com mais estoque");
        return ResponseEntity.ok(produtoFacade.categoriaMaisEstoque());
    }

    @Override
    public ResponseEntity<List<ProdutoResponse>> buscarTodos(
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(defaultValue = "0") int offset
    ) {
        log.info("[CONTROLLER] Buscando todos os produtos - limite={}, offset={}", limite, offset);
        List<ProdutoResponse> response = produtoFacade.buscarTodos(limite, offset);
        log.info("[CONTROLLER] Retornados {} produtos", response.size());
        return ResponseEntity.ok(response);
    }
}
