package com.usco.ingenieria.software.pagina.modalidades.grado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Observaciones.
 */
@Table("observaciones")
public class Observaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("observacion")
    private String observacion;

    @Transient
    @JsonIgnoreProperties(value = { "tipoModalidad", "empresa", "arl" }, allowSetters = true)
    private Proyecto proyecto;

    @Column("proyecto_id")
    private Long proyectoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Observaciones id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public Observaciones observacion(String observacion) {
        this.setObservacion(observacion);
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Proyecto getProyecto() {
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        this.proyectoId = proyecto != null ? proyecto.getId() : null;
    }

    public Observaciones proyecto(Proyecto proyecto) {
        this.setProyecto(proyecto);
        return this;
    }

    public Long getProyectoId() {
        return this.proyectoId;
    }

    public void setProyectoId(Long proyecto) {
        this.proyectoId = proyecto;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Observaciones)) {
            return false;
        }
        return id != null && id.equals(((Observaciones) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Observaciones{" +
            "id=" + getId() +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
