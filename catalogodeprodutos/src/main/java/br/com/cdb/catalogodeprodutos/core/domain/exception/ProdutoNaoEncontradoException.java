package br.com.cdb.catalogodeprodutos.core.domain.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(String msg) {
        super(msg);
    }
}
