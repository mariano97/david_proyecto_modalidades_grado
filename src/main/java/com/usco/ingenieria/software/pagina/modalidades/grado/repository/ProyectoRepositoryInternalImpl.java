package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.ArlRowMapper;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.EmpresaRowMapper;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.ProyectoRowMapper;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.TablaContenidoRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Proyecto entity.
 */
@SuppressWarnings("unused")
class ProyectoRepositoryInternalImpl extends SimpleR2dbcRepository<Proyecto, Long> implements ProyectoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TablaContenidoRowMapper tablacontenidoMapper;
    private final EmpresaRowMapper empresaMapper;
    private final ArlRowMapper arlMapper;
    private final ProyectoRowMapper proyectoMapper;

    private static final Table entityTable = Table.aliased("proyecto", EntityManager.ENTITY_ALIAS);
    private static final Table tipoModalidadTable = Table.aliased("tabla_contenido", "tipoModalidad");
    private static final Table empresaTable = Table.aliased("empresa", "empresa");
    private static final Table arlTable = Table.aliased("arl", "arl");

    public ProyectoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TablaContenidoRowMapper tablacontenidoMapper,
        EmpresaRowMapper empresaMapper,
        ArlRowMapper arlMapper,
        ProyectoRowMapper proyectoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Proyecto.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tablacontenidoMapper = tablacontenidoMapper;
        this.empresaMapper = empresaMapper;
        this.arlMapper = arlMapper;
        this.proyectoMapper = proyectoMapper;
    }

    @Override
    public Flux<Proyecto> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Proyecto> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProyectoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaContenidoSqlHelper.getColumns(tipoModalidadTable, "tipoModalidad"));
        columns.addAll(EmpresaSqlHelper.getColumns(empresaTable, "empresa"));
        columns.addAll(ArlSqlHelper.getColumns(arlTable, "arl"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tipoModalidadTable)
            .on(Column.create("tipo_modalidad_id", entityTable))
            .equals(Column.create("id", tipoModalidadTable))
            .leftOuterJoin(empresaTable)
            .on(Column.create("empresa_id", entityTable))
            .equals(Column.create("id", empresaTable))
            .leftOuterJoin(arlTable)
            .on(Column.create("arl_id", entityTable))
            .equals(Column.create("id", arlTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Proyecto.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Proyecto> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Proyecto> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Proyecto> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Proyecto> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Proyecto> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Proyecto process(Row row, RowMetadata metadata) {
        Proyecto entity = proyectoMapper.apply(row, "e");
        entity.setTipoModalidad(tablacontenidoMapper.apply(row, "tipoModalidad"));
        entity.setEmpresa(empresaMapper.apply(row, "empresa"));
        entity.setArl(arlMapper.apply(row, "arl"));
        return entity;
    }

    @Override
    public <S extends Proyecto> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
