package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.TablaContenidoRowMapper;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.TablaMaestraRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the TablaContenido entity.
 */
@SuppressWarnings("unused")
class TablaContenidoRepositoryInternalImpl extends SimpleR2dbcRepository<TablaContenido, Long> implements TablaContenidoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TablaMaestraRowMapper tablamaestraMapper;
    private final TablaContenidoRowMapper tablacontenidoMapper;

    private static final Table entityTable = Table.aliased("tabla_contenido", EntityManager.ENTITY_ALIAS);
    private static final Table tablaMaestraTable = Table.aliased("tabla_maestra", "tablaMaestra");

    public TablaContenidoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TablaMaestraRowMapper tablamaestraMapper,
        TablaContenidoRowMapper tablacontenidoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TablaContenido.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tablamaestraMapper = tablamaestraMapper;
        this.tablacontenidoMapper = tablacontenidoMapper;
    }

    @Override
    public Flux<TablaContenido> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<TablaContenido> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TablaContenidoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaMaestraSqlHelper.getColumns(tablaMaestraTable, "tablaMaestra"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tablaMaestraTable)
            .on(Column.create("tabla_maestra_id", entityTable))
            .equals(Column.create("id", tablaMaestraTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TablaContenido.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TablaContenido> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TablaContenido> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<TablaContenido> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<TablaContenido> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<TablaContenido> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private TablaContenido process(Row row, RowMetadata metadata) {
        TablaContenido entity = tablacontenidoMapper.apply(row, "e");
        entity.setTablaMaestra(tablamaestraMapper.apply(row, "tablaMaestra"));
        return entity;
    }

    @Override
    public <S extends TablaContenido> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
