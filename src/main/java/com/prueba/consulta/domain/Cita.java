package com.prueba.consulta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cita.
 */
@Entity
@Table(name = "cita")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "horario_consulta_inicio")
    private ZonedDateTime horarioConsultaInicio;

    @Column(name = "horario_consulta_fin")
    private ZonedDateTime horarioConsultaFin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "citas" }, allowSetters = true)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "citas" }, allowSetters = true)
    private Consultorio consultorio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "citas" }, allowSetters = true)
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cita id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getHorarioConsultaInicio() {
        return this.horarioConsultaInicio;
    }

    public Cita horarioConsultaInicio(ZonedDateTime horarioConsultaInicio) {
        this.setHorarioConsultaInicio(horarioConsultaInicio);
        return this;
    }

    public void setHorarioConsultaInicio(ZonedDateTime horarioConsultaInicio) {
        this.horarioConsultaInicio = horarioConsultaInicio;
    }

    public ZonedDateTime getHorarioConsultaFin() {
        return this.horarioConsultaFin;
    }

    public Cita horarioConsultaFin(ZonedDateTime horarioConsultaFin) {
        this.setHorarioConsultaFin(horarioConsultaFin);
        return this;
    }

    public void setHorarioConsultaFin(ZonedDateTime horarioConsultaFin) {
        this.horarioConsultaFin = horarioConsultaFin;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Cita doctor(Doctor doctor) {
        this.setDoctor(doctor);
        return this;
    }

    public Consultorio getConsultorio() {
        return this.consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    public Cita consultorio(Consultorio consultorio) {
        this.setConsultorio(consultorio);
        return this;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Cita paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cita)) {
            return false;
        }
        return getId() != null && getId().equals(((Cita) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cita{" +
            "id=" + getId() +
            ", horarioConsultaInicio='" + getHorarioConsultaInicio() + "'" +
            ", horarioConsultaFin='" + getHorarioConsultaFin() + "'" +
            "}";
    }
}
