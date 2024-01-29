package com.prueba.consulta.service;

import com.prueba.consulta.domain.Cita;
import com.prueba.consulta.repository.CitaRepository;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prueba.consulta.domain.Cita}.
 */
@Service
@Transactional
public class CitaService {

    private final Logger log = LoggerFactory.getLogger(CitaService.class);

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    /**
     * Save a cita.
     *
     * @param cita the entity to save.
     * @return the persisted entity.
     */
    public Cita save(Cita cita) {
        log.debug("Request to save Cita : {}", cita);
        validarCita(cita, false);

        return citaRepository.save(cita);
    }

    private void validarCita(Cita cita, boolean isUpdate) {
        //no se puede agendar citas en un mismo consultorio a la misma hora.
        List<Cita> citasExistentes = citaRepository.findAll();

        // si se edita se remueve el existente para no contarlo en las reglas de negocio
        if (isUpdate) {
            citasExistentes = citasExistentes.stream().filter(cita1 -> !cita1.getId().equals(cita.getId())).collect(Collectors.toList());
        }

        if (
            citasExistentes
                .stream()
                .anyMatch(citas1 ->
                    cita.getConsultorio().getId().equals(citas1.getId()) &&
                    cita.getHorarioConsultaFin().equals(citas1.getHorarioConsultaFin()) &&
                    cita.getHorarioConsultaInicio().equals(citas1.getHorarioConsultaInicio())
                )
        ) {
            throw new RuntimeException("Error: No se puedo agendar porque el consultorio o la hora ya está ocupada");
        }

        //No se puede agendar cita para un mismo Dr. a la misma hora.
        if (
            citasExistentes
                .stream()
                .anyMatch(citas1 ->
                    citas1.getDoctor().getId().equals(cita.getDoctor().getId()) &&
                    citas1.getHorarioConsultaInicio().equals(cita.getHorarioConsultaInicio()) &&
                    citas1.getHorarioConsultaFin().equals(cita.getHorarioConsultaFin())
                )
        ) {
            throw new RuntimeException("Error: El doctor que desea agendar ya tiene una cita en este horario");
        }

        /*
        No se puede agendar cita para un paciente a la una misma hora ni con menos de 2 horas
        de diferencia para el mismo día. Es decir, si un paciente tiene 1 cita de 2 a 3pm, solo
        podría tener otra el mismo día a partir de las 5pm.
        */
        if (
            citasExistentes
                .stream()
                .anyMatch(cita1 ->
                    cita1.getPaciente().getId().equals(cita.getPaciente().getId()) &&
                    isFechaEnRango2Horas(
                        cita.getHorarioConsultaInicio(),
                        cita.getHorarioConsultaFin(),
                        cita1.getHorarioConsultaInicio(),
                        cita1.getHorarioConsultaFin()
                    )
                )
        ) {
            throw new RuntimeException(
                "Error: Para agendar una cita a este paciente debe exister al menos 2 horas de diferencia entre cita"
            );
        }
        int numeroCitasDoctor = 0;
        //Un mismo doctor no puede tener más de 8 citas en el día.
        for (Cita cita1 : citasExistentes) {
            if (cita1.getDoctor().getId().equals(cita.getDoctor().getId())) numeroCitasDoctor++;
        }

        if (numeroCitasDoctor >= 8) {
            throw new RuntimeException("Error: El doctor alcanzó el número de citas permitidas por día");
        }
    }

    private static boolean isFechaEnRango2Horas(ZonedDateTime fInicio, ZonedDateTime fFin, ZonedDateTime fEInicio, ZonedDateTime fEFin) {
        // Aumentar 2 horas a fEFin y restar 2 horas a fEInicio
        ZonedDateTime fEInicioModificado = fEInicio.minusHours(2);
        ZonedDateTime fEFinModificado = fEFin.plusHours(2);

        // Verificar si los primeros dos rangos de fechas están dentro del rango modificado
        return (
            (fInicio.isAfter(fEInicioModificado) || fInicio.isEqual(fEInicioModificado)) &&
            (fFin.isBefore(fEFinModificado) || fFin.isEqual(fEFinModificado))
        );
    }

    /**
     * Update a cita.
     *
     * @param cita the entity to save.
     * @return the persisted entity.
     */
    public Cita update(Cita cita) {
        log.debug("Request to update Cita : {}", cita);
        validarCita(cita, true);
        return citaRepository.save(cita);
    }

    /**
     * Partially update a cita.
     *
     * @param cita the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Cita> partialUpdate(Cita cita) {
        log.debug("Request to partially update Cita : {}", cita);

        return citaRepository
            .findById(cita.getId())
            .map(existingCita -> {
                if (cita.getHorarioConsultaInicio() != null) {
                    existingCita.setHorarioConsultaInicio(cita.getHorarioConsultaInicio());
                }
                if (cita.getHorarioConsultaFin() != null) {
                    existingCita.setHorarioConsultaFin(cita.getHorarioConsultaFin());
                }

                return existingCita;
            })
            .map(citaRepository::save);
    }

    /**
     * Get all the citas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Cita> findAll(Pageable pageable) {
        log.debug("Request to get all Citas");
        return citaRepository.findAll(pageable);
    }

    /**
     * Get one cita by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Cita> findOne(Long id) {
        log.debug("Request to get Cita : {}", id);
        return citaRepository.findById(id);
    }

    /**
     * Delete the cita by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cita : {}", id);
        citaRepository.deleteById(id);
    }
}
