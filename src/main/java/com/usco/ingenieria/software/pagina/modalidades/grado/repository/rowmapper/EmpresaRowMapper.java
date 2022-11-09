package com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Empresa;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Empresa}, with proper type conversions.
 */
@Service
public class EmpresaRowMapper implements BiFunction<Row, String, Empresa> {

    private final ColumnConverter converter;

    public EmpresaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Empresa} stored in the database.
     */
    @Override
    public Empresa apply(Row row, String prefix) {
        Empresa entity = new Empresa();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setNit(converter.fromRow(row, prefix + "_nit", String.class));
        entity.setTelefono(converter.fromRow(row, prefix + "_telefono", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        return entity;
    }
}
