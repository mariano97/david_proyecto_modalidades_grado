package com.usco.ingenieria.software.pagina.modalidades.grado.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Arl} entity.
 */
public class ArlDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArlDTO)) {
            return false;
        }

        ArlDTO arlDTO = (ArlDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, arlDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArlDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
