package com.prueba.consulta.web.rest;

import com.prueba.consulta.domain.Consultorio;
import com.prueba.consulta.repository.ConsultorioRepository;
import com.prueba.consulta.service.ConsultorioService;
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
 * REST controller for managing {@link com.prueba.consulta.domain.Consultorio}.
 */
@RestController
@RequestMapping("/api/consultorios")
public class ConsultorioResource {

    private final Logger log = LoggerFactory.getLogger(ConsultorioResource.class);

    private static final String ENTITY_NAME = "consultorio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultorioService consultorioService;

    private final ConsultorioRepository consultorioRepository;

    public ConsultorioResource(ConsultorioService consultorioService, ConsultorioRepository consultorioRepository) {
        this.consultorioService = consultorioService;
        this.consultorioRepository = consultorioRepository;
    }

    /**
     * {@code POST  /consultorios} : Create a new consultorio.
     *
     * @param consultorio the consultorio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultorio, or with status {@code 400 (Bad Request)} if the consultorio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Consultorio> createConsultorio(@RequestBody Consultorio consultorio) throws URISyntaxException {
        log.debug("REST request to save Consultorio : {}", consultorio);
        if (consultorio.getId() != null) {
            throw new BadRequestAlertException("A new consultorio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Consultorio result = consultorioService.save(consultorio);
        return ResponseEntity
            .created(new URI("/api/consultorios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consultorios/:id} : Updates an existing consultorio.
     *
     * @param id the id of the consultorio to save.
     * @param consultorio the consultorio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultorio,
     * or with status {@code 400 (Bad Request)} if the consultorio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consultorio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Consultorio> updateConsultorio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Consultorio consultorio
    ) throws URISyntaxException {
        log.debug("REST request to update Consultorio : {}, {}", id, consultorio);
        if (consultorio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultorio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Consultorio result = consultorioService.update(consultorio);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, consultorio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /consultorios/:id} : Partial updates given fields of an existing consultorio, field will ignore if it is null
     *
     * @param id the id of the consultorio to save.
     * @param consultorio the consultorio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultorio,
     * or with status {@code 400 (Bad Request)} if the consultorio is not valid,
     * or with status {@code 404 (Not Found)} if the consultorio is not found,
     * or with status {@code 500 (Internal Server Error)} if the consultorio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Consultorio> partialUpdateConsultorio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Consultorio consultorio
    ) throws URISyntaxException {
        log.debug("REST request to partial update Consultorio partially : {}, {}", id, consultorio);
        if (consultorio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultorio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Consultorio> result = consultorioService.partialUpdate(consultorio);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, consultorio.getId().toString())
        );
    }

    /**
     * {@code GET  /consultorios} : get all the consultorios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consultorios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Consultorio>> getAllConsultorios(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Consultorios");
        Page<Consultorio> page = consultorioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consultorios/:id} : get the "id" consultorio.
     *
     * @param id the id of the consultorio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consultorio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Consultorio> getConsultorio(@PathVariable("id") Long id) {
        log.debug("REST request to get Consultorio : {}", id);
        Optional<Consultorio> consultorio = consultorioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consultorio);
    }

    /**
     * {@code DELETE  /consultorios/:id} : delete the "id" consultorio.
     *
     * @param id the id of the consultorio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultorio(@PathVariable("id") Long id) {
        log.debug("REST request to delete Consultorio : {}", id);
        consultorioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
