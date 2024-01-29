package com.prueba.consulta.repository;

import com.prueba.consulta.domain.Consultorio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Consultorio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {}
