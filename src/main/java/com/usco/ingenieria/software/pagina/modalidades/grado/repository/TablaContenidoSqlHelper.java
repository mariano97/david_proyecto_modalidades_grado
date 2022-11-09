package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TablaContenidoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));

        columns.add(Column.aliased("tabla_maestra_id", table, columnPrefix + "_tabla_maestra_id"));
        return columns;
    }
}
