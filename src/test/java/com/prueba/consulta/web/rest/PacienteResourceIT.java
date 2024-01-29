package com.prueba.consulta.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prueba.consulta.IntegrationTest;
import com.prueba.consulta.domain.Paciente;
import com.prueba.consulta.repository.PacienteRepository;
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
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PacienteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity(EntityManager em) {
        Paciente paciente = new Paciente().nombre(DEFAULT_NOMBRE);
        return paciente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity(EntityManager em) {
        Paciente paciente = new Paciente().nombre(UPDATED_NOMBRE);
        return paciente;
    }

    @BeforeEach
    public void initTest() {
        paciente = createEntity(em);
    }

    @Test
    @Transactional
    void createPaciente() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();
        // Create the Paciente
        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isCreated());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate + 1);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createPacienteWithExistingId() throws Exception {
        // Create the Paciente with an existing ID
        paciente.setId(1L);

        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPacientes() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL_ID, paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaciente are not directly saved in db
        em.detach(updatedPaciente);
        updatedPaciente.nombre(UPDATED_NOMBRE);

        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaciente.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paciente.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente.nombre(UPDATED_NOMBRE);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente.nombre(UPDATED_NOMBRE);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeDelete = pacienteRepository.findAll().size();

        // Delete the paciente
        restPacienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, paciente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
