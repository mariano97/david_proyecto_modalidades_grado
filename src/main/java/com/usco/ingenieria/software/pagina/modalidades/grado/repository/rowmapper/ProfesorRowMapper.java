package com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Profesor;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Profesor}, with proper type conversions.
 */
@Service
public class ProfesorRowMapper implements BiFunction<Row, String, Profesor> {

    private final ColumnConverter converter;

    public ProfesorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Profesor} stored in the database.
     */
    @Override
    public Profesor apply(Row row, String prefix) {
        Profesor entity = new Profesor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPrimerNombre(converter.fromRow(row, prefix + "_primer_nombre", String.class));
        entity.setSegundoNombre(converter.fromRow(row, prefix + "_segundo_nombre", String.class));
        entity.setApellidos(converter.fromRow(row, prefix + "_apellidos", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setTelefono(converter.fromRow(row, prefix + "_telefono", String.class));
        return entity;
    }
}
