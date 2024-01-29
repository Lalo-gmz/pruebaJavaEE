package com.prueba.consulta.web.rest;

import static com.prueba.consulta.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prueba.consulta.IntegrationTest;
import com.prueba.consulta.domain.Cita;
import com.prueba.consulta.repository.CitaRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CitaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CitaResourceIT {

    private static final ZonedDateTime DEFAULT_HORARIO_CONSULTA_INICIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HORARIO_CONSULTA_INICIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_HORARIO_CONSULTA_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HORARIO_CONSULTA_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/citas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCitaMockMvc;

    private Cita cita;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createEntity(EntityManager em) {
        Cita cita = new Cita().horarioConsultaInicio(DEFAULT_HORARIO_CONSULTA_INICIO).horarioConsultaFin(DEFAULT_HORARIO_CONSULTA_FIN);
        return cita;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createUpdatedEntity(EntityManager em) {
        Cita cita = new Cita().horarioConsultaInicio(UPDATED_HORARIO_CONSULTA_INICIO).horarioConsultaFin(UPDATED_HORARIO_CONSULTA_FIN);
        return cita;
    }

    @BeforeEach
    public void initTest() {
        cita = createEntity(em);
    }

    @Test
    @Transactional
    void createCita() throws Exception {
        int databaseSizeBeforeCreate = citaRepository.findAll().size();
        // Create the Cita
        restCitaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cita)))
            .andExpect(status().isCreated());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeCreate + 1);
        Cita testCita = citaList.get(citaList.size() - 1);
        assertThat(testCita.getHorarioConsultaInicio()).isEqualTo(DEFAULT_HORARIO_CONSULTA_INICIO);
        assertThat(testCita.getHorarioConsultaFin()).isEqualTo(DEFAULT_HORARIO_CONSULTA_FIN);
    }

    @Test
    @Transactional
    void createCitaWithExistingId() throws Exception {
        // Create the Cita with an existing ID
        cita.setId(1L);

        int databaseSizeBeforeCreate = citaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cita)))
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCitas() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        // Get all the citaList
        restCitaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cita.getId().intValue())))
            .andExpect(jsonPath("$.[*].horarioConsultaInicio").value(hasItem(sameInstant(DEFAULT_HORARIO_CONSULTA_INICIO))))
            .andExpect(jsonPath("$.[*].horarioConsultaFin").value(hasItem(sameInstant(DEFAULT_HORARIO_CONSULTA_FIN))));
    }

    @Test
    @Transactional
    void getCita() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        // Get the cita
        restCitaMockMvc
            .perform(get(ENTITY_API_URL_ID, cita.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cita.getId().intValue()))
            .andExpect(jsonPath("$.horarioConsultaInicio").value(sameInstant(DEFAULT_HORARIO_CONSULTA_INICIO)))
            .andExpect(jsonPath("$.horarioConsultaFin").value(sameInstant(DEFAULT_HORARIO_CONSULTA_FIN)));
    }

    @Test
    @Transactional
    void getNonExistingCita() throws Exception {
        // Get the cita
        restCitaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCita() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        int databaseSizeBeforeUpdate = citaRepository.findAll().size();

        // Update the cita
        Cita updatedCita = citaRepository.findById(cita.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCita are not directly saved in db
        em.detach(updatedCita);
        updatedCita.horarioConsultaInicio(UPDATED_HORARIO_CONSULTA_INICIO).horarioConsultaFin(UPDATED_HORARIO_CONSULTA_FIN);

        restCitaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCita.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCita))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
        Cita testCita = citaList.get(citaList.size() - 1);
        assertThat(testCita.getHorarioConsultaInicio()).isEqualTo(UPDATED_HORARIO_CONSULTA_INICIO);
        assertThat(testCita.getHorarioConsultaFin()).isEqualTo(UPDATED_HORARIO_CONSULTA_FIN);
    }

    @Test
    @Transactional
    void putNonExistingCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cita.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cita))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cita))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cita)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCitaWithPatch() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        int databaseSizeBeforeUpdate = citaRepository.findAll().size();

        // Update the cita using partial update
        Cita partialUpdatedCita = new Cita();
        partialUpdatedCita.setId(cita.getId());

        partialUpdatedCita.horarioConsultaInicio(UPDATED_HORARIO_CONSULTA_INICIO).horarioConsultaFin(UPDATED_HORARIO_CONSULTA_FIN);

        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCita.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCita))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
        Cita testCita = citaList.get(citaList.size() - 1);
        assertThat(testCita.getHorarioConsultaInicio()).isEqualTo(UPDATED_HORARIO_CONSULTA_INICIO);
        assertThat(testCita.getHorarioConsultaFin()).isEqualTo(UPDATED_HORARIO_CONSULTA_FIN);
    }

    @Test
    @Transactional
    void fullUpdateCitaWithPatch() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        int databaseSizeBeforeUpdate = citaRepository.findAll().size();

        // Update the cita using partial update
        Cita partialUpdatedCita = new Cita();
        partialUpdatedCita.setId(cita.getId());

        partialUpdatedCita.horarioConsultaInicio(UPDATED_HORARIO_CONSULTA_INICIO).horarioConsultaFin(UPDATED_HORARIO_CONSULTA_FIN);

        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCita.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCita))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
        Cita testCita = citaList.get(citaList.size() - 1);
        assertThat(testCita.getHorarioConsultaInicio()).isEqualTo(UPDATED_HORARIO_CONSULTA_INICIO);
        assertThat(testCita.getHorarioConsultaFin()).isEqualTo(UPDATED_HORARIO_CONSULTA_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cita.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cita))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cita))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cita)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCita() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        int databaseSizeBeforeDelete = citaRepository.findAll().size();

        // Delete the cita
        restCitaMockMvc
            .perform(delete(ENTITY_API_URL_ID, cita.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
