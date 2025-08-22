package br.com.cdb.catalogodeprodutos.repository;

import br.com.cdb.catalogodeprodutos.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {


    @Query(value = "SELECT * FROM produto p " +
            "WHERE 1=1 " +
            "AND (:ativo IS NULL OR p.ativo = :ativo) " +
            "AND (:nome IS NULL OR LOWER(p.nome::text) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:precoMin IS NULL OR p.preco >= :precoMin) " +
            "AND (:precoMax IS NULL OR p.preco <= :precoMax)",
            nativeQuery = true)
    Page<Produto> buscarComFiltros(@Param("ativo") Boolean ativo,
                                   @Param("nome") String nome,
                                   @Param("precoMin") Double precoMin,
                                   @Param("precoMax") Double precoMax,
                                   Pageable pageable);


}
