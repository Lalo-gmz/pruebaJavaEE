package com.prueba.consulta.web.rest;

import com.prueba.consulta.domain.Cita;
import com.prueba.consulta.repository.CitaRepository;
import com.prueba.consulta.service.CitaService;
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
 * REST controller for managing {@link com.prueba.consulta.domain.Cita}.
 */
@RestController
@RequestMapping("/api/citas")
public class CitaResource {

    private final Logger log = LoggerFactory.getLogger(CitaResource.class);

    private static final String ENTITY_NAME = "cita";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CitaService citaService;

    private final CitaRepository citaRepository;

    public CitaResource(CitaService citaService, CitaRepository citaRepository) {
        this.citaService = citaService;
        this.citaRepository = citaRepository;
    }

    /**
     * {@code POST  /citas} : Create a new cita.
     *
     * @param cita the cita to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cita, or with status {@code 400 (Bad Request)} if the cita has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Cita> createCita(@RequestBody Cita cita) throws URISyntaxException {
        log.debug("REST request to save Cita : {}", cita);
        if (cita.getId() != null) {
            throw new BadRequestAlertException("A new cita cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cita result = citaService.save(cita);
        return ResponseEntity
            .created(new URI("/api/citas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /citas/:id} : Updates an existing cita.
     *
     * @param id the id of the cita to save.
     * @param cita the cita to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cita,
     * or with status {@code 400 (Bad Request)} if the cita is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cita couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cita> updateCita(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cita cita)
        throws URISyntaxException {
        log.debug("REST request to update Cita : {}, {}", id, cita);
        if (cita.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cita.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cita result = citaService.update(cita);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cita.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /citas/:id} : Partial updates given fields of an existing cita, field will ignore if it is null
     *
     * @param id the id of the cita to save.
     * @param cita the cita to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cita,
     * or with status {@code 400 (Bad Request)} if the cita is not valid,
     * or with status {@code 404 (Not Found)} if the cita is not found,
     * or with status {@code 500 (Internal Server Error)} if the cita couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cita> partialUpdateCita(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cita cita)
        throws URISyntaxException {
        log.debug("REST request to partial update Cita partially : {}, {}", id, cita);
        if (cita.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cita.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cita> result = citaService.partialUpdate(cita);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cita.getId().toString())
        );
    }

    /**
     * {@code GET  /citas} : get all the citas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of citas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Cita>> getAllCitas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Citas");
        Page<Cita> page = citaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /citas/:id} : get the "id" cita.
     *
     * @param id the id of the cita to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cita, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cita> getCita(@PathVariable("id") Long id) {
        log.debug("REST request to get Cita : {}", id);
        Optional<Cita> cita = citaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cita);
    }

    /**
     * {@code DELETE  /citas/:id} : delete the "id" cita.
     *
     * @param id the id of the cita to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable("id") Long id) {
        log.debug("REST request to delete Cita : {}", id);
        citaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
