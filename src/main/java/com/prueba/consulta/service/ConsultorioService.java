package com.prueba.consulta.service;

import com.prueba.consulta.domain.Consultorio;
import com.prueba.consulta.repository.ConsultorioRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prueba.consulta.domain.Consultorio}.
 */
@Service
@Transactional
public class ConsultorioService {

    private final Logger log = LoggerFactory.getLogger(ConsultorioService.class);

    private final ConsultorioRepository consultorioRepository;

    public ConsultorioService(ConsultorioRepository consultorioRepository) {
        this.consultorioRepository = consultorioRepository;
    }

    /**
     * Save a consultorio.
     *
     * @param consultorio the entity to save.
     * @return the persisted entity.
     */
    public Consultorio save(Consultorio consultorio) {
        log.debug("Request to save Consultorio : {}", consultorio);
        return consultorioRepository.save(consultorio);
    }

    /**
     * Update a consultorio.
     *
     * @param consultorio the entity to save.
     * @return the persisted entity.
     */
    public Consultorio update(Consultorio consultorio) {
        log.debug("Request to update Consultorio : {}", consultorio);
        return consultorioRepository.save(consultorio);
    }

    /**
     * Partially update a consultorio.
     *
     * @param consultorio the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Consultorio> partialUpdate(Consultorio consultorio) {
        log.debug("Request to partially update Consultorio : {}", consultorio);

        return consultorioRepository
            .findById(consultorio.getId())
            .map(existingConsultorio -> {
                if (consultorio.getNumero() != null) {
                    existingConsultorio.setNumero(consultorio.getNumero());
                }
                if (consultorio.getPiso() != null) {
                    existingConsultorio.setPiso(consultorio.getPiso());
                }

                return existingConsultorio;
            })
            .map(consultorioRepository::save);
    }

    /**
     * Get all the consultorios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Consultorio> findAll(Pageable pageable) {
        log.debug("Request to get all Consultorios");
        return consultorioRepository.findAll(pageable);
    }

    /**
     * Get one consultorio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Consultorio> findOne(Long id) {
        log.debug("Request to get Consultorio : {}", id);
        return consultorioRepository.findById(id);
    }

    /**
     * Delete the consultorio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Consultorio : {}", id);
        consultorioRepository.deleteById(id);
    }
}
