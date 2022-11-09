package com.usco.ingenieria.software.pagina.modalidades.grado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Estudiante.
 */
@Table("estudiante")
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("primer_nombre")
    private String primerNombre;

    @Column("segundo_nombre")
    private String segundoNombre;

    @NotNull(message = "must not be null")
    @Column("apellidos")
    private String apellidos;

    @NotNull(message = "must not be null")
    @Column("numero_documento")
    private String numeroDocumento;

    @NotNull(message = "must not be null")
    @Column("codigo_estudiantil")
    private String codigoEstudiantil;

    @NotNull(message = "must not be null")
    @Column("celular")
    private String celular;

    @NotNull(message = "must not be null")
    @Column("email")
    private String email;

    @Transient
    @JsonIgnoreProperties(value = { "tablaMaestra" }, allowSetters = true)
    private TablaContenido tipoDocumento;

    @Transient
    @JsonIgnoreProperties(value = { "tipoModalidad", "empresa", "arl" }, allowSetters = true)
    private Proyecto proyecto;

    @Column("tipo_documento_id")
    private Long tipoDocumentoId;

    @Column("proyecto_id")
    private Long proyectoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Estudiante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimerNombre() {
        return this.primerNombre;
    }

    public Estudiante primerNombre(String primerNombre) {
        this.setPrimerNombre(primerNombre);
        return this;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return this.segundoNombre;
    }

    public Estudiante segundoNombre(String segundoNombre) {
        this.setSegundoNombre(segundoNombre);
        return this;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Estudiante apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumeroDocumento() {
        return this.numeroDocumento;
    }

    public Estudiante numeroDocumento(String numeroDocumento) {
        this.setNumeroDocumento(numeroDocumento);
        return this;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getCodigoEstudiantil() {
        return this.codigoEstudiantil;
    }

    public Estudiante codigoEstudiantil(String codigoEstudiantil) {
        this.setCodigoEstudiantil(codigoEstudiantil);
        return this;
    }

    public void setCodigoEstudiantil(String codigoEstudiantil) {
        this.codigoEstudiantil = codigoEstudiantil;
    }

    public String getCelular() {
        return this.celular;
    }

    public Estudiante celular(String celular) {
        this.setCelular(celular);
        return this;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return this.email;
    }

    public Estudiante email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TablaContenido getTipoDocumento() {
        return this.tipoDocumento;
    }

    public void setTipoDocumento(TablaContenido tablaContenido) {
        this.tipoDocumento = tablaContenido;
        this.tipoDocumentoId = tablaContenido != null ? tablaContenido.getId() : null;
    }

    public Estudiante tipoDocumento(TablaContenido tablaContenido) {
        this.setTipoDocumento(tablaContenido);
        return this;
    }

    public Proyecto getProyecto() {
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        this.proyectoId = proyecto != null ? proyecto.getId() : null;
    }

    public Estudiante proyecto(Proyecto proyecto) {
        this.setProyecto(proyecto);
        return this;
    }

    public Long getTipoDocumentoId() {
        return this.tipoDocumentoId;
    }

    public void setTipoDocumentoId(Long tablaContenido) {
        this.tipoDocumentoId = tablaContenido;
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
        if (!(o instanceof Estudiante)) {
            return false;
        }
        return id != null && id.equals(((Estudiante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Estudiante{" +
            "id=" + getId() +
            ", primerNombre='" + getPrimerNombre() + "'" +
            ", segundoNombre='" + getSegundoNombre() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", numeroDocumento='" + getNumeroDocumento() + "'" +
            ", codigoEstudiantil='" + getCodigoEstudiantil() + "'" +
            ", celular='" + getCelular() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
