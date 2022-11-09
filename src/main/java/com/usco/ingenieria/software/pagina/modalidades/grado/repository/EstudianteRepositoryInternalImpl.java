package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Estudiante;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.EstudianteRowMapper;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.ProyectoRowMapper;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.rowmapper.TablaContenidoRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Estudiante entity.
 */
@SuppressWarnings("unused")
class EstudianteRepositoryInternalImpl extends SimpleR2dbcRepository<Estudiante, Long> implements EstudianteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TablaContenidoRowMapper tablacontenidoMapper;
    private final ProyectoRowMapper proyectoMapper;
    private final EstudianteRowMapper estudianteMapper;

    private static final Table entityTable = Table.aliased("estudiante", EntityManager.ENTITY_ALIAS);
    private static final Table tipoDocumentoTable = Table.aliased("tabla_contenido", "tipoDocumento");
    private static final Table proyectoTable = Table.aliased("proyecto", "proyecto");

    public EstudianteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TablaContenidoRowMapper tablacontenidoMapper,
        ProyectoRowMapper proyectoMapper,
        EstudianteRowMapper estudianteMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Estudiante.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tablacontenidoMapper = tablacontenidoMapper;
        this.proyectoMapper = proyectoMapper;
        this.estudianteMapper = estudianteMapper;
    }

    @Override
    public Flux<Estudiante> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Estudiante> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EstudianteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaContenidoSqlHelper.getColumns(tipoDocumentoTable, "tipoDocumento"));
        columns.addAll(ProyectoSqlHelper.getColumns(proyectoTable, "proyecto"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tipoDocumentoTable)
            .on(Column.create("tipo_documento_id", entityTable))
            .equals(Column.create("id", tipoDocumentoTable))
            .leftOuterJoin(proyectoTable)
            .on(Column.create("proyecto_id", entityTable))
            .equals(Column.create("id", proyectoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Estudiante.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Estudiante> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Estudiante> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Estudiante> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Estudiante> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Estudiante> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Estudiante process(Row row, RowMetadata metadata) {
        Estudiante entity = estudianteMapper.apply(row, "e");
        entity.setTipoDocumento(tablacontenidoMapper.apply(row, "tipoDocumento"));
        entity.setProyecto(proyectoMapper.apply(row, "proyecto"));
        return entity;
    }

    @Override
    public <S extends Estudiante> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
