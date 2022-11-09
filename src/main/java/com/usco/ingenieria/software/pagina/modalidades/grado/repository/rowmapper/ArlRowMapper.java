package com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Arl;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Arl}, with proper type conversions.
 */
@Service
public class ArlRowMapper implements BiFunction<Row, String, Arl> {

    private final ColumnConverter converter;

    public ArlRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Arl} stored in the database.
     */
    @Override
    public Arl apply(Row row, String prefix) {
        Arl entity = new Arl();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        return entity;
    }
}
