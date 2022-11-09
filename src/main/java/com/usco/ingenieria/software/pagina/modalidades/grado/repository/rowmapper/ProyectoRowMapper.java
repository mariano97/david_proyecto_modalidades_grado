package com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Proyecto}, with proper type conversions.
 */
@Service
public class ProyectoRowMapper implements BiFunction<Row, String, Proyecto> {

    private final ColumnConverter converter;

    public ProyectoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Proyecto} stored in the database.
     */
    @Override
    public Proyecto apply(Row row, String prefix) {
        Proyecto entity = new Proyecto();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setActa(converter.fromRow(row, prefix + "_acta", Boolean.class));
        entity.setFechaInicio(converter.fromRow(row, prefix + "_fecha_inicio", Instant.class));
        entity.setFechaTermino(converter.fromRow(row, prefix + "_fecha_termino", Instant.class));
        entity.setTipoModalidadId(converter.fromRow(row, prefix + "_tipo_modalidad_id", Long.class));
        entity.setEmpresaId(converter.fromRow(row, prefix + "_empresa_id", Long.class));
        entity.setArlId(converter.fromRow(row, prefix + "_arl_id", Long.class));
        return entity;
    }
}
