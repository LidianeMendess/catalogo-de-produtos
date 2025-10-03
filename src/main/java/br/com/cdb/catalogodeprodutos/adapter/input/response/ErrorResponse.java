package br.com.cdb.catalogodeprodutos.adapter.input.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalhes do erro da requisição")
public class ErrorResponse {

    @Schema(description = "Timestamp do erro", example = "2025-10-03T11:00:00")
    private String timestamp;

    @Schema(description = "Código HTTP do erro", example = "400")
    private int status;

    @Schema(description = "Tipo do erro", example = "Bad Request")
    private String error;

    @Schema(description = "Mensagem detalhada do erro", example = "O campo 'nome' é obrigatório")
    private String message;

    @Schema(description = "Caminho da requisição que causou o erro", example = "/produtos")
    private String path;
}
