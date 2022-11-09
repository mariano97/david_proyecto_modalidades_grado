package com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TablaContenido}, with proper type conversions.
 */
@Service
public class TablaContenidoRowMapper implements BiFunction<Row, String, TablaContenido> {

    private final ColumnConverter converter;

    public TablaContenidoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TablaContenido} stored in the database.
     */
    @Override
    public TablaContenido apply(Row row, String prefix) {
        TablaContenido entity = new TablaContenido();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setTablaMaestraId(converter.fromRow(row, prefix + "_tabla_maestra_id", Long.class));
        return entity;
    }
}
