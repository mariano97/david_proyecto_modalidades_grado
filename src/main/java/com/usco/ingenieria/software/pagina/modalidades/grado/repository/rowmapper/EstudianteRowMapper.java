package com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Estudiante;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Estudiante}, with proper type conversions.
 */
@Service
public class EstudianteRowMapper implements BiFunction<Row, String, Estudiante> {

    private final ColumnConverter converter;

    public EstudianteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Estudiante} stored in the database.
     */
    @Override
    public Estudiante apply(Row row, String prefix) {
        Estudiante entity = new Estudiante();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPrimerNombre(converter.fromRow(row, prefix + "_primer_nombre", String.class));
        entity.setSegundoNombre(converter.fromRow(row, prefix + "_segundo_nombre", String.class));
        entity.setApellidos(converter.fromRow(row, prefix + "_apellidos", String.class));
        entity.setNumeroDocumento(converter.fromRow(row, prefix + "_numero_documento", String.class));
        entity.setCodigoEstudiantil(converter.fromRow(row, prefix + "_codigo_estudiantil", String.class));
        entity.setCelular(converter.fromRow(row, prefix + "_celular", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setTipoDocumentoId(converter.fromRow(row, prefix + "_tipo_documento_id", Long.class));
        entity.setProyectoId(converter.fromRow(row, prefix + "_proyecto_id", Long.class));
        return entity;
    }
}
