package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ObservacionesSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("observacion", table, columnPrefix + "_observacion"));

        columns.add(Column.aliased("proyecto_id", table, columnPrefix + "_proyecto_id"));
        return columns;
    }
}
