package br.com.cdb.catalogodeprodutos.adapter.mapper;

import br.com.cdb.catalogodeprodutos.adapter.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.response.ProdutoResponse;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoMapper INSTANCE= Mappers.getMapper(ProdutoMapper.class);

    Produto toDomain(ProdutoRequest request);

    ProdutoResponse toResponse(Produto produto);

    List <ProdutoResponse> toResponseList(List<Produto> produtos);

}
