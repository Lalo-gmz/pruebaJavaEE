package com.prueba.consulta.service;

import com.prueba.consulta.domain.Paciente;
import com.prueba.consulta.repository.PacienteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prueba.consulta.domain.Paciente}.
 */
@Service
@Transactional
public class PacienteService {

    private final Logger log = LoggerFactory.getLogger(PacienteService.class);

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Save a paciente.
     *
     * @param paciente the entity to save.
     * @return the persisted entity.
     */
    public Paciente save(Paciente paciente) {
        log.debug("Request to save Paciente : {}", paciente);
        return pacienteRepository.save(paciente);
    }

    /**
     * Update a paciente.
     *
     * @param paciente the entity to save.
     * @return the persisted entity.
     */
    public Paciente update(Paciente paciente) {
        log.debug("Request to update Paciente : {}", paciente);
        return pacienteRepository.save(paciente);
    }

    /**
     * Partially update a paciente.
     *
     * @param paciente the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Paciente> partialUpdate(Paciente paciente) {
        log.debug("Request to partially update Paciente : {}", paciente);

        return pacienteRepository
            .findById(paciente.getId())
            .map(existingPaciente -> {
                if (paciente.getNombre() != null) {
                    existingPaciente.setNombre(paciente.getNombre());
                }

                return existingPaciente;
            })
            .map(pacienteRepository::save);
    }

    /**
     * Get all the pacientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Paciente> findAll(Pageable pageable) {
        log.debug("Request to get all Pacientes");
        return pacienteRepository.findAll(pageable);
    }

    /**
     * Get one paciente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Paciente> findOne(Long id) {
        log.debug("Request to get Paciente : {}", id);
        return pacienteRepository.findById(id);
    }

    /**
     * Delete the paciente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Paciente : {}", id);
        pacienteRepository.deleteById(id);
    }
}
