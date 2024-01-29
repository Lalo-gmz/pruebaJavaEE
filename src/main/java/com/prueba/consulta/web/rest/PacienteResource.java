package com.prueba.consulta.web.rest;

import com.prueba.consulta.domain.Paciente;
import com.prueba.consulta.repository.PacienteRepository;
import com.prueba.consulta.service.PacienteService;
import com.prueba.consulta.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.prueba.consulta.domain.Paciente}.
 */
@RestController
@RequestMapping("/api/pacientes")
public class PacienteResource {

    private final Logger log = LoggerFactory.getLogger(PacienteResource.class);

    private static final String ENTITY_NAME = "paciente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PacienteService pacienteService;

    private final PacienteRepository pacienteRepository;

    public PacienteResource(PacienteService pacienteService, PacienteRepository pacienteRepository) {
        this.pacienteService = pacienteService;
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * {@code POST  /pacientes} : Create a new paciente.
     *
     * @param paciente the paciente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paciente, or with status {@code 400 (Bad Request)} if the paciente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Paciente> createPaciente(@RequestBody Paciente paciente) throws URISyntaxException {
        log.debug("REST request to save Paciente : {}", paciente);
        if (paciente.getId() != null) {
            throw new BadRequestAlertException("A new paciente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Paciente result = pacienteService.save(paciente);
        return ResponseEntity
            .created(new URI("/api/pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pacientes/:id} : Updates an existing paciente.
     *
     * @param id the id of the paciente to save.
     * @param paciente the paciente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paciente,
     * or with status {@code 400 (Bad Request)} if the paciente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paciente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> updatePaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Paciente paciente
    ) throws URISyntaxException {
        log.debug("REST request to update Paciente : {}, {}", id, paciente);
        if (paciente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paciente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Paciente result = pacienteService.update(paciente);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paciente.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pacientes/:id} : Partial updates given fields of an existing paciente, field will ignore if it is null
     *
     * @param id the id of the paciente to save.
     * @param paciente the paciente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paciente,
     * or with status {@code 400 (Bad Request)} if the paciente is not valid,
     * or with status {@code 404 (Not Found)} if the paciente is not found,
     * or with status {@code 500 (Internal Server Error)} if the paciente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Paciente> partialUpdatePaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Paciente paciente
    ) throws URISyntaxException {
        log.debug("REST request to partial update Paciente partially : {}, {}", id, paciente);
        if (paciente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paciente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Paciente> result = pacienteService.partialUpdate(paciente);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paciente.getId().toString())
        );
    }

    /**
     * {@code GET  /pacientes} : get all the pacientes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pacientes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Paciente>> getAllPacientes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pacientes");
        Page<Paciente> page = pacienteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pacientes/:id} : get the "id" paciente.
     *
     * @param id the id of the paciente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paciente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getPaciente(@PathVariable("id") Long id) {
        log.debug("REST request to get Paciente : {}", id);
        Optional<Paciente> paciente = pacienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paciente);
    }

    /**
     * {@code DELETE  /pacientes/:id} : delete the "id" paciente.
     *
     * @param id the id of the paciente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable("id") Long id) {
        log.debug("REST request to delete Paciente : {}", id);
        pacienteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
