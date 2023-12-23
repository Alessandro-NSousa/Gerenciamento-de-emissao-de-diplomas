package com.ufpa.diploma.emissao.repository;

import com.ufpa.diploma.emissao.domain.Turma;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Turma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {}
