package com.usco.ingenieria.software.pagina.modalidades.grado.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto} entity.
 */
public class ProyectoDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    @NotNull(message = "must not be null")
    private Boolean acta;

    @NotNull(message = "must not be null")
    private Instant fechaInicio;

    private Instant fechaTermino;

    private TablaContenidoDTO tipoModalidad;

    private EmpresaDTO empresa;

    private ArlDTO arl;

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

    public Boolean getActa() {
        return acta;
    }

    public void setActa(Boolean acta) {
        this.acta = acta;
    }

    public Instant getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Instant fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public TablaContenidoDTO getTipoModalidad() {
        return tipoModalidad;
    }

    public void setTipoModalidad(TablaContenidoDTO tipoModalidad) {
        this.tipoModalidad = tipoModalidad;
    }

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    public ArlDTO getArl() {
        return arl;
    }

    public void setArl(ArlDTO arl) {
        this.arl = arl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProyectoDTO)) {
            return false;
        }

        ProyectoDTO proyectoDTO = (ProyectoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proyectoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProyectoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", acta='" + getActa() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaTermino='" + getFechaTermino() + "'" +
            ", tipoModalidad=" + getTipoModalidad() +
            ", empresa=" + getEmpresa() +
            ", arl=" + getArl() +
            "}";
    }
}
