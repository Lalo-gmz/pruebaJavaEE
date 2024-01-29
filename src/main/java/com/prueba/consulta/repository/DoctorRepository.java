package com.prueba.consulta.repository;

import com.prueba.consulta.domain.Doctor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Doctor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {}
