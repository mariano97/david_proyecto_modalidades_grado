package com.usco.ingenieria.software.pagina.modalidades.grado.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Observaciones} entity.
 */
public class ObservacionesDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String observacion;

    private ProyectoDTO proyecto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public ProyectoDTO getProyecto() {
        return proyecto;
    }

    public void setProyecto(ProyectoDTO proyecto) {
        this.proyecto = proyecto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObservacionesDTO)) {
            return false;
        }

        ObservacionesDTO observacionesDTO = (ObservacionesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, observacionesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ObservacionesDTO{" +
            "id=" + getId() +
            ", observacion='" + getObservacion() + "'" +
            ", proyecto=" + getProyecto() +
            "}";
    }
}
