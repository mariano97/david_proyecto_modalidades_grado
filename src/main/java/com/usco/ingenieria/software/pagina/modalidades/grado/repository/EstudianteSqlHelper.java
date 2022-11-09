package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EstudianteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("primer_nombre", table, columnPrefix + "_primer_nombre"));
        columns.add(Column.aliased("segundo_nombre", table, columnPrefix + "_segundo_nombre"));
        columns.add(Column.aliased("apellidos", table, columnPrefix + "_apellidos"));
        columns.add(Column.aliased("numero_documento", table, columnPrefix + "_numero_documento"));
        columns.add(Column.aliased("codigo_estudiantil", table, columnPrefix + "_codigo_estudiantil"));
        columns.add(Column.aliased("celular", table, columnPrefix + "_celular"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));

        columns.add(Column.aliased("tipo_documento_id", table, columnPrefix + "_tipo_documento_id"));
        columns.add(Column.aliased("proyecto_id", table, columnPrefix + "_proyecto_id"));
        return columns;
    }
}
