package com.prueba.consulta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Consultorio.
 */
@Entity
@Table(name = "consultorio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Consultorio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "piso")
    private String piso;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "consultorio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "doctor", "consultorio", "paciente" }, allowSetters = true)
    private Set<Cita> citas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Consultorio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Consultorio numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPiso() {
        return this.piso;
    }

    public Consultorio piso(String piso) {
        this.setPiso(piso);
        return this;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public Set<Cita> getCitas() {
        return this.citas;
    }

    public void setCitas(Set<Cita> citas) {
        if (this.citas != null) {
            this.citas.forEach(i -> i.setConsultorio(null));
        }
        if (citas != null) {
            citas.forEach(i -> i.setConsultorio(this));
        }
        this.citas = citas;
    }

    public Consultorio citas(Set<Cita> citas) {
        this.setCitas(citas);
        return this;
    }

    public Consultorio addCita(Cita cita) {
        this.citas.add(cita);
        cita.setConsultorio(this);
        return this;
    }

    public Consultorio removeCita(Cita cita) {
        this.citas.remove(cita);
        cita.setConsultorio(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consultorio)) {
            return false;
        }
        return getId() != null && getId().equals(((Consultorio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consultorio{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", piso='" + getPiso() + "'" +
            "}";
    }
}
