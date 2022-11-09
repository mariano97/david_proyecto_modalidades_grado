package com.usco.ingenieria.software.pagina.modalidades.grado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Proyecto.
 */
@Table("proyecto")
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("acta")
    private Boolean acta;

    @NotNull(message = "must not be null")
    @Column("fecha_inicio")
    private Instant fechaInicio;

    @Column("fecha_termino")
    private Instant fechaTermino;

    @Transient
    @JsonIgnoreProperties(value = { "tablaMaestra" }, allowSetters = true)
    private TablaContenido tipoModalidad;

    @Transient
    private Empresa empresa;

    @Transient
    private Arl arl;

    @Column("tipo_modalidad_id")
    private Long tipoModalidadId;

    @Column("empresa_id")
    private Long empresaId;

    @Column("arl_id")
    private Long arlId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proyecto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Proyecto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActa() {
        return this.acta;
    }

    public Proyecto acta(Boolean acta) {
        this.setActa(acta);
        return this;
    }

    public void setActa(Boolean acta) {
        this.acta = acta;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public Proyecto fechaInicio(Instant fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaTermino() {
        return this.fechaTermino;
    }

    public Proyecto fechaTermino(Instant fechaTermino) {
        this.setFechaTermino(fechaTermino);
        return this;
    }

    public void setFechaTermino(Instant fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public TablaContenido getTipoModalidad() {
        return this.tipoModalidad;
    }

    public void setTipoModalidad(TablaContenido tablaContenido) {
        this.tipoModalidad = tablaContenido;
        this.tipoModalidadId = tablaContenido != null ? tablaContenido.getId() : null;
    }

    public Proyecto tipoModalidad(TablaContenido tablaContenido) {
        this.setTipoModalidad(tablaContenido);
        return this;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        this.empresaId = empresa != null ? empresa.getId() : null;
    }

    public Proyecto empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public Arl getArl() {
        return this.arl;
    }

    public void setArl(Arl arl) {
        this.arl = arl;
        this.arlId = arl != null ? arl.getId() : null;
    }

    public Proyecto arl(Arl arl) {
        this.setArl(arl);
        return this;
    }

    public Long getTipoModalidadId() {
        return this.tipoModalidadId;
    }

    public void setTipoModalidadId(Long tablaContenido) {
        this.tipoModalidadId = tablaContenido;
    }

    public Long getEmpresaId() {
        return this.empresaId;
    }

    public void setEmpresaId(Long empresa) {
        this.empresaId = empresa;
    }

    public Long getArlId() {
        return this.arlId;
    }

    public void setArlId(Long arl) {
        this.arlId = arl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proyecto)) {
            return false;
        }
        return id != null && id.equals(((Proyecto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proyecto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", acta='" + getActa() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaTermino='" + getFechaTermino() + "'" +
            "}";
    }
}
