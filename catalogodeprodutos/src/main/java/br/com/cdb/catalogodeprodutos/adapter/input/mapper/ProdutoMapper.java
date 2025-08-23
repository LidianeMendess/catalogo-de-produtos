package br.com.cdb.catalogodeprodutos.adapter.input.mapper;

import br.com.cdb.catalogodeprodutos.adapter.input.request.ProdutoRequest;
import br.com.cdb.catalogodeprodutos.adapter.output.entity.ProdutoEntity;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoMapper INSTANCE= Mappers.getMapper(ProdutoMapper.class);



    Produto toDomain(ProdutoRequest request);


    ProdutoEntity toEntity(Produto produto);


    Produto toDomain(ProdutoEntity entity);
}
