package br.com.cdb.catalogodeprodutos.adapter.input.mapper;

import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.input.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-05T11:53:30-0300",
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

        return produto;
    }

    @Override
    public ProdutoResponse toResponse(Produto produto) {
        if ( produto == null ) {
            return null;
        }

        ProdutoResponse produtoResponse = new ProdutoResponse();

        produtoResponse.setId( produto.getId() );
        produtoResponse.setSku( produto.getSku() );
        produtoResponse.setNome( produto.getNome() );
        produtoResponse.setDescricao( produto.getDescricao() );
        produtoResponse.setPreco( produto.getPreco() );
        produtoResponse.setQuantidade( produto.getQuantidade() );
        produtoResponse.setAtivo( produto.getAtivo() );

        return produtoResponse;
    }

    @Override
    public List<ProdutoResponse> toResponseList(List<Produto> produtos) {
        if ( produtos == null ) {
            return null;
        }

        List<ProdutoResponse> list = new ArrayList<ProdutoResponse>( produtos.size() );
        for ( Produto produto : produtos ) {
            list.add( toResponse( produto ) );
        }

        return list;
    }
}
