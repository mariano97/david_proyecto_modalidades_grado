package com.usco.ingenieria.software.pagina.modalidades.grado.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Estudiante} entity.
 */
public class EstudianteDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String primerNombre;

    private String segundoNombre;

    @NotNull(message = "must not be null")
    private String apellidos;

    @NotNull(message = "must not be null")
    private String numeroDocumento;

    @NotNull(message = "must not be null")
    private String codigoEstudiantil;

    @NotNull(message = "must not be null")
    private String celular;

    @NotNull(message = "must not be null")
    private String email;

    private TablaContenidoDTO tipoDocumento;

    private ProyectoDTO proyecto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getCodigoEstudiantil() {
        return codigoEstudiantil;
    }

    public void setCodigoEstudiantil(String codigoEstudiantil) {
        this.codigoEstudiantil = codigoEstudiantil;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TablaContenidoDTO getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TablaContenidoDTO tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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
        if (!(o instanceof EstudianteDTO)) {
            return false;
        }

        EstudianteDTO estudianteDTO = (EstudianteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, estudianteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstudianteDTO{" +
            "id=" + getId() +
            ", primerNombre='" + getPrimerNombre() + "'" +
            ", segundoNombre='" + getSegundoNombre() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", numeroDocumento='" + getNumeroDocumento() + "'" +
            ", codigoEstudiantil='" + getCodigoEstudiantil() + "'" +
            ", celular='" + getCelular() + "'" +
            ", email='" + getEmail() + "'" +
            ", tipoDocumento=" + getTipoDocumento() +
            ", proyecto=" + getProyecto() +
            "}";
    }
}
