package com.usco.ingenieria.software.pagina.modalidades.grado.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TablaContenido.
 */
@Table("tabla_contenido")
public class TablaContenido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("codigo")
    private String codigo;

    @Transient
    private TablaMaestra tablaMaestra;

    @Column("tabla_maestra_id")
    private Long tablaMaestraId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TablaContenido id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public TablaContenido nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public TablaContenido codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TablaMaestra getTablaMaestra() {
        return this.tablaMaestra;
    }

    public void setTablaMaestra(TablaMaestra tablaMaestra) {
        this.tablaMaestra = tablaMaestra;
        this.tablaMaestraId = tablaMaestra != null ? tablaMaestra.getId() : null;
    }

    public TablaContenido tablaMaestra(TablaMaestra tablaMaestra) {
        this.setTablaMaestra(tablaMaestra);
        return this;
    }

    public Long getTablaMaestraId() {
        return this.tablaMaestraId;
    }

    public void setTablaMaestraId(Long tablaMaestra) {
        this.tablaMaestraId = tablaMaestra;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TablaContenido)) {
            return false;
        }
        return id != null && id.equals(((TablaContenido) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TablaContenido{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            "}";
    }
}
