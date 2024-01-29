package com.prueba.consulta.repository;

import com.prueba.consulta.domain.Cita;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cita entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {}
