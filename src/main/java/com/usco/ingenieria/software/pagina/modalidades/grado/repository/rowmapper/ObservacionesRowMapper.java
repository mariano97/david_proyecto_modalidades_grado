package com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Observaciones;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Observaciones}, with proper type conversions.
 */
@Service
public class ObservacionesRowMapper implements BiFunction<Row, String, Observaciones> {

    private final ColumnConverter converter;

    public ObservacionesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Observaciones} stored in the database.
     */
    @Override
    public Observaciones apply(Row row, String prefix) {
        Observaciones entity = new Observaciones();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setObservacion(converter.fromRow(row, prefix + "_observacion", String.class));
        entity.setProyectoId(converter.fromRow(row, prefix + "_proyecto_id", Long.class));
        return entity;
    }
}
