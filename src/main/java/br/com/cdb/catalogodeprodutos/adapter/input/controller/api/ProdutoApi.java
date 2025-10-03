package br.com.cdb.catalogodeprodutos.adapter.input.controller.api;

import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.input.response.ErrorResponse;
import br.com.cdb.catalogodeprodutos.adapter.input.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Categoria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Produtos",
        description = "Gerenciamento de produtos: busca, criação, atualização, exclusão e consultas específicas de estoque e categoria."
)
@RequestMapping("/produtos")
public interface ProdutoApi {

    @Operation(summary = "Insere novo produto")
    @ApiResponse(responseCode = "201", description = "Produto inserido com sucesso",
            content = @Content(schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    ResponseEntity<ProdutoResponse> inserirProduto(@Valid @RequestBody ProdutoRequest produtoRequest);

    @Operation(summary = "Busca produto por ID")
    @ApiResponse(responseCode = "200", description = "Produto encontrado",
            content = @Content(schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable int id);

    @Operation(summary = "Busca produtos por filtros")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    ResponseEntity<List<ProdutoResponse>> buscarPorFiltros(
            @RequestParam(required = false) String tipoFiltro,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) Categoria categoria
    );

    @Operation(summary = "Atualiza produto")
    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Erro de validação",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    ResponseEntity<ProdutoResponse> atualizarProduto(@PathVariable int id, @Valid @RequestBody ProdutoRequest request);

    @Operation(summary = "Exclui produto")
    @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    ResponseEntity<Void> excluirProduto(@PathVariable int id);

    @Operation(summary = "Busca produto por SKU")
    @ApiResponse(responseCode = "200", description = "Produto encontrado",
            content = @Content(schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("sku/{sku}")
    ResponseEntity<ProdutoResponse> buscarPorSku(@PathVariable String sku);

    @Operation(summary = "Busca produtos por categoria com paginação")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/categoria/{categoria}")
    ResponseEntity<List<ProdutoResponse>> buscarPorCategoria(
            @PathVariable Categoria categoria,
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(defaultValue = "0") int offset);

    @Operation(summary = "Decrementa estoque de um produto")
    @ApiResponse(responseCode = "200", description = "Estoque decrementado com sucesso",
            content = @Content(schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}/decrementar/{quantidade}")
    ResponseEntity<ProdutoResponse> decrementarEstoque(@PathVariable int id, @PathVariable int quantidade);

    @Operation(summary = "Busca produtos com estoque abaixo de um limite")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("estoqueAbaixo/{limite}")
    ResponseEntity<List<ProdutoResponse>> buscarEstoqueBaixo(@PathVariable int limite);

    @Operation(summary = "Busca a categoria com mais estoque")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada",
            content = @Content(schema = @Schema(implementation = Categoria.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/categoria/mais-estoque")
    ResponseEntity<Categoria> categoriaMaisEstoque();

    @Operation(summary = "Busca todos os produtos com paginação")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(schema = @Schema(implementation = ProdutoResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/todos")
    ResponseEntity<List<ProdutoResponse>> buscarTodos(
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(defaultValue = "0") int offset);
}
