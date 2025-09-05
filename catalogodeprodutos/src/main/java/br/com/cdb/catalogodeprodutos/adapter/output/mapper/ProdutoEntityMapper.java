package br.com.cdb.catalogodeprodutos.adapter.output.mapper;

import br.com.cdb.catalogodeprodutos.adapter.output.entity.ProdutoEntity;
import br.com.cdb.catalogodeprodutos.core.domain.model.Produto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoEntityMapper {

    Produto toDomain(ProdutoEntity entity);

    List<Produto> toDomainList(List<ProdutoEntity> entities);

    ProdutoEntity toEntity(Produto domain);

    List<ProdutoEntity> toEntityList(List<Produto> domains);

}
