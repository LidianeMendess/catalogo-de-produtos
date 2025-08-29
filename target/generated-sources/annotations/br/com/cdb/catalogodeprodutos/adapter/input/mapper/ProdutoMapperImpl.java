package br.com.cdb.catalogodeprodutos.adapter.input.mapper;

import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-29T16:37:11-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class ProdutoMapperImpl implements ProdutoMapper {

    @Override
    public Produto toDomain(ProdutoRequest request) {
        if ( request == null ) {
            return null;
        }

        Produto produto = new Produto();

        produto.setId( request.getId() );
        produto.setSku( request.getSku() );
        produto.setNome( request.getNome() );
        produto.setDescricao( request.getDescricao() );
        produto.setPreco( request.getPreco() );
        produto.setQuantidade( request.getQuantidade() );
        if ( request.getAtivo() != null ) {
            produto.setAtivo( request.getAtivo() );
        }
        produto.setCriadoEm( request.getCriadoEm() );
        produto.setAtualizadoEm( request.getAtualizadoEm() );

        return produto;
    }
}
