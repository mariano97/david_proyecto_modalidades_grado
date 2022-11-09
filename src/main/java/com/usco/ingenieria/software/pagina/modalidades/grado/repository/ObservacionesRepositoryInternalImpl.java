package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Observaciones;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.ObservacionesRowMapper;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.ProyectoRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
 * Spring Data SQL reactive custom repository implementation for the Observaciones entity.
 */
@SuppressWarnings("unused")
class ObservacionesRepositoryInternalImpl extends SimpleR2dbcRepository<Observaciones, Long> implements ObservacionesRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProyectoRowMapper proyectoMapper;
    private final ObservacionesRowMapper observacionesMapper;

    private static final Table entityTable = Table.aliased("observaciones", EntityManager.ENTITY_ALIAS);
    private static final Table proyectoTable = Table.aliased("proyecto", "proyecto");

    public ObservacionesRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProyectoRowMapper proyectoMapper,
        ObservacionesRowMapper observacionesMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Observaciones.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.proyectoMapper = proyectoMapper;
        this.observacionesMapper = observacionesMapper;
    }

    @Override
    public Flux<Observaciones> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Observaciones> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ObservacionesSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProyectoSqlHelper.getColumns(proyectoTable, "proyecto"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(proyectoTable)
            .on(Column.create("proyecto_id", entityTable))
            .equals(Column.create("id", proyectoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Observaciones.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Observaciones> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Observaciones> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Observaciones> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Observaciones> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Observaciones> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Observaciones process(Row row, RowMetadata metadata) {
        Observaciones entity = observacionesMapper.apply(row, "e");
        entity.setProyecto(proyectoMapper.apply(row, "proyecto"));
        return entity;
    }

    @Override
    public <S extends Observaciones> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
