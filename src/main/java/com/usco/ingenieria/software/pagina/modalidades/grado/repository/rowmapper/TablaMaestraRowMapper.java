package com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaMaestra;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TablaMaestra}, with proper type conversions.
 */
@Service
public class TablaMaestraRowMapper implements BiFunction<Row, String, TablaMaestra> {

    private final ColumnConverter converter;

    public TablaMaestraRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TablaMaestra} stored in the database.
     */
    @Override
    public TablaMaestra apply(Row row, String prefix) {
        TablaMaestra entity = new TablaMaestra();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        return entity;
    }
}
