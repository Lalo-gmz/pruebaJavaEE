package com.prueba.consulta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Doctor.
 */
@Entity
@Table(name = "doctor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @Column(name = "especialidad")
    private String especialidad;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "doctor", "consultorio", "paciente" }, allowSetters = true)
    private Set<Cita> citas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Doctor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Doctor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return this.apellidoPaterno;
    }

    public Doctor apellidoPaterno(String apellidoPaterno) {
        this.setApellidoPaterno(apellidoPaterno);
        return this;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return this.apellidoMaterno;
    }

    public Doctor apellidoMaterno(String apellidoMaterno) {
        this.setApellidoMaterno(apellidoMaterno);
        return this;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEspecialidad() {
        return this.especialidad;
    }

    public Doctor especialidad(String especialidad) {
        this.setEspecialidad(especialidad);
        return this;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Set<Cita> getCitas() {
        return this.citas;
    }

    public void setCitas(Set<Cita> citas) {
        if (this.citas != null) {
            this.citas.forEach(i -> i.setDoctor(null));
        }
        if (citas != null) {
            citas.forEach(i -> i.setDoctor(this));
        }
        this.citas = citas;
    }

    public Doctor citas(Set<Cita> citas) {
        this.setCitas(citas);
        return this;
    }

    public Doctor addCita(Cita cita) {
        this.citas.add(cita);
        cita.setDoctor(this);
        return this;
    }

    public Doctor removeCita(Cita cita) {
        this.citas.remove(cita);
        cita.setDoctor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Doctor)) {
            return false;
        }
        return getId() != null && getId().equals(((Doctor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Doctor{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellidoPaterno='" + getApellidoPaterno() + "'" +
            ", apellidoMaterno='" + getApellidoMaterno() + "'" +
            ", especialidad='" + getEspecialidad() + "'" +
            "}";
    }
}
