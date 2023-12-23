package com.ufpa.diploma.emissao.repository;

import com.ufpa.diploma.emissao.domain.EntregaDiploma;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EntregaDiploma entity.
 */
@Repository
public interface EntregaDiplomaRepository extends JpaRepository<EntregaDiploma, Long> {
    default Optional<EntregaDiploma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EntregaDiploma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EntregaDiploma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct entregaDiploma from EntregaDiploma entregaDiploma left join fetch entregaDiploma.processo",
        countQuery = "select count(distinct entregaDiploma) from EntregaDiploma entregaDiploma"
    )
    Page<EntregaDiploma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct entregaDiploma from EntregaDiploma entregaDiploma left join fetch entregaDiploma.processo")
    List<EntregaDiploma> findAllWithToOneRelationships();

    @Query("select entregaDiploma from EntregaDiploma entregaDiploma left join fetch entregaDiploma.processo where entregaDiploma.id =:id")
    Optional<EntregaDiploma> findOneWithToOneRelationships(@Param("id") Long id);
}
