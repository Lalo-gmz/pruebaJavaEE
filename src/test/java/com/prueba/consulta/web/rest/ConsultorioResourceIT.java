package com.prueba.consulta.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prueba.consulta.IntegrationTest;
import com.prueba.consulta.domain.Consultorio;
import com.prueba.consulta.repository.ConsultorioRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ConsultorioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsultorioResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_PISO = "AAAAAAAAAA";
    private static final String UPDATED_PISO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consultorios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsultorioRepository consultorioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultorioMockMvc;

    private Consultorio consultorio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultorio createEntity(EntityManager em) {
        Consultorio consultorio = new Consultorio().numero(DEFAULT_NUMERO).piso(DEFAULT_PISO);
        return consultorio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultorio createUpdatedEntity(EntityManager em) {
        Consultorio consultorio = new Consultorio().numero(UPDATED_NUMERO).piso(UPDATED_PISO);
        return consultorio;
    }

    @BeforeEach
    public void initTest() {
        consultorio = createEntity(em);
    }

    @Test
    @Transactional
    void createConsultorio() throws Exception {
        int databaseSizeBeforeCreate = consultorioRepository.findAll().size();
        // Create the Consultorio
        restConsultorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultorio)))
            .andExpect(status().isCreated());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeCreate + 1);
        Consultorio testConsultorio = consultorioList.get(consultorioList.size() - 1);
        assertThat(testConsultorio.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testConsultorio.getPiso()).isEqualTo(DEFAULT_PISO);
    }

    @Test
    @Transactional
    void createConsultorioWithExistingId() throws Exception {
        // Create the Consultorio with an existing ID
        consultorio.setId(1L);

        int databaseSizeBeforeCreate = consultorioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultorio)))
            .andExpect(status().isBadRequest());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConsultorios() throws Exception {
        // Initialize the database
        consultorioRepository.saveAndFlush(consultorio);

        // Get all the consultorioList
        restConsultorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].piso").value(hasItem(DEFAULT_PISO)));
    }

    @Test
    @Transactional
    void getConsultorio() throws Exception {
        // Initialize the database
        consultorioRepository.saveAndFlush(consultorio);

        // Get the consultorio
        restConsultorioMockMvc
            .perform(get(ENTITY_API_URL_ID, consultorio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consultorio.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.piso").value(DEFAULT_PISO));
    }

    @Test
    @Transactional
    void getNonExistingConsultorio() throws Exception {
        // Get the consultorio
        restConsultorioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsultorio() throws Exception {
        // Initialize the database
        consultorioRepository.saveAndFlush(consultorio);

        int databaseSizeBeforeUpdate = consultorioRepository.findAll().size();

        // Update the consultorio
        Consultorio updatedConsultorio = consultorioRepository.findById(consultorio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConsultorio are not directly saved in db
        em.detach(updatedConsultorio);
        updatedConsultorio.numero(UPDATED_NUMERO).piso(UPDATED_PISO);

        restConsultorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConsultorio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConsultorio))
            )
            .andExpect(status().isOk());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeUpdate);
        Consultorio testConsultorio = consultorioList.get(consultorioList.size() - 1);
        assertThat(testConsultorio.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testConsultorio.getPiso()).isEqualTo(UPDATED_PISO);
    }

    @Test
    @Transactional
    void putNonExistingConsultorio() throws Exception {
        int databaseSizeBeforeUpdate = consultorioRepository.findAll().size();
        consultorio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultorio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsultorio() throws Exception {
        int databaseSizeBeforeUpdate = consultorioRepository.findAll().size();
        consultorio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsultorio() throws Exception {
        int databaseSizeBeforeUpdate = consultorioRepository.findAll().size();
        consultorio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultorioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultorio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultorioWithPatch() throws Exception {
        // Initialize the database
        consultorioRepository.saveAndFlush(consultorio);

        int databaseSizeBeforeUpdate = consultorioRepository.findAll().size();

        // Update the consultorio using partial update
        Consultorio partialUpdatedConsultorio = new Consultorio();
        partialUpdatedConsultorio.setId(consultorio.getId());

        partialUpdatedConsultorio.numero(UPDATED_NUMERO);

        restConsultorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultorio))
            )
            .andExpect(status().isOk());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeUpdate);
        Consultorio testConsultorio = consultorioList.get(consultorioList.size() - 1);
        assertThat(testConsultorio.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testConsultorio.getPiso()).isEqualTo(DEFAULT_PISO);
    }

    @Test
    @Transactional
    void fullUpdateConsultorioWithPatch() throws Exception {
        // Initialize the database
        consultorioRepository.saveAndFlush(consultorio);

        int databaseSizeBeforeUpdate = consultorioRepository.findAll().size();

        // Update the consultorio using partial update
        Consultorio partialUpdatedConsultorio = new Consultorio();
        partialUpdatedConsultorio.setId(consultorio.getId());

        partialUpdatedConsultorio.numero(UPDATED_NUMERO).piso(UPDATED_PISO);

        restConsultorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultorio))
            )
            .andExpect(status().isOk());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeUpdate);
        Consultorio testConsultorio = consultorioList.get(consultorioList.size() - 1);
        assertThat(testConsultorio.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testConsultorio.getPiso()).isEqualTo(UPDATED_PISO);
    }

    @Test
    @Transactional
    void patchNonExistingConsultorio() throws Exception {
        int databaseSizeBeforeUpdate = consultorioRepository.findAll().size();
        consultorio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consultorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsultorio() throws Exception {
        int databaseSizeBeforeUpdate = consultorioRepository.findAll().size();
        consultorio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsultorio() throws Exception {
        int databaseSizeBeforeUpdate = consultorioRepository.findAll().size();
        consultorio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultorioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(consultorio))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultorio in the database
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsultorio() throws Exception {
        // Initialize the database
        consultorioRepository.saveAndFlush(consultorio);

        int databaseSizeBeforeDelete = consultorioRepository.findAll().size();

        // Delete the consultorio
        restConsultorioMockMvc
            .perform(delete(ENTITY_API_URL_ID, consultorio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consultorio> consultorioList = consultorioRepository.findAll();
        assertThat(consultorioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
