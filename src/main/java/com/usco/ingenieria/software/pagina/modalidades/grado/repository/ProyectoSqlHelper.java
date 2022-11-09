package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProyectoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("acta", table, columnPrefix + "_acta"));
        columns.add(Column.aliased("fecha_inicio", table, columnPrefix + "_fecha_inicio"));
        columns.add(Column.aliased("fecha_termino", table, columnPrefix + "_fecha_termino"));

        columns.add(Column.aliased("tipo_modalidad_id", table, columnPrefix + "_tipo_modalidad_id"));
        columns.add(Column.aliased("empresa_id", table, columnPrefix + "_empresa_id"));
        columns.add(Column.aliased("arl_id", table, columnPrefix + "_arl_id"));
        return columns;
    }
}
