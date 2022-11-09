package com.usco.ingenieria.software.pagina.modalidades.grado.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido} entity.
 */
public class TablaContenidoDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    @NotNull(message = "must not be null")
    private String codigo;

    private TablaMaestraDTO tablaMaestra;

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TablaMaestraDTO getTablaMaestra() {
        return tablaMaestra;
    }

    public void setTablaMaestra(TablaMaestraDTO tablaMaestra) {
        this.tablaMaestra = tablaMaestra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TablaContenidoDTO)) {
            return false;
        }

        TablaContenidoDTO tablaContenidoDTO = (TablaContenidoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tablaContenidoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TablaContenidoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", tablaMaestra=" + getTablaMaestra() +
            "}";
    }
}
