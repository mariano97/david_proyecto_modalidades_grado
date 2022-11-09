package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the TablaContenido entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TablaContenidoRepository extends ReactiveCrudRepository<TablaContenido, Long>, TablaContenidoRepositoryInternal {
    Flux<TablaContenido> findAllBy(Pageable pageable);

    @Override
    Mono<TablaContenido> findOneWithEagerRelationships(Long id);

    @Override
    Flux<TablaContenido> findAllWithEagerRelationships();

    @Override
    Flux<TablaContenido> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM tabla_contenido entity WHERE entity.tabla_maestra_id = :id")
    Flux<TablaContenido> findByTablaMaestra(Long id);

    @Query("SELECT * FROM tabla_contenido entity WHERE entity.tabla_maestra_id IS NULL")
    Flux<TablaContenido> findAllWhereTablaMaestraIsNull();

    @Override
    <S extends TablaContenido> Mono<S> save(S entity);

    @Override
    Flux<TablaContenido> findAll();

    @Override
    Mono<TablaContenido> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TablaContenidoRepositoryInternal {
    <S extends TablaContenido> Mono<S> save(S entity);

    Flux<TablaContenido> findAllBy(Pageable pageable);

    Flux<TablaContenido> findAll();

    Mono<TablaContenido> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TablaContenido> findAllBy(Pageable pageable, Criteria criteria);

    Mono<TablaContenido> findOneWithEagerRelationships(Long id);

    Flux<TablaContenido> findAllWithEagerRelationships();

    Flux<TablaContenido> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
