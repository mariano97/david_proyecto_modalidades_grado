package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Observaciones;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Observaciones entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObservacionesRepository extends ReactiveCrudRepository<Observaciones, Long>, ObservacionesRepositoryInternal {
    Flux<Observaciones> findAllBy(Pageable pageable);

    @Override
    Mono<Observaciones> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Observaciones> findAllWithEagerRelationships();

    @Override
    Flux<Observaciones> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM observaciones entity WHERE entity.proyecto_id = :id")
    Flux<Observaciones> findByProyecto(Long id);

    @Query("SELECT * FROM observaciones entity WHERE entity.proyecto_id IS NULL")
    Flux<Observaciones> findAllWhereProyectoIsNull();

    @Override
    <S extends Observaciones> Mono<S> save(S entity);

    @Override
    Flux<Observaciones> findAll();

    @Override
    Mono<Observaciones> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ObservacionesRepositoryInternal {
    <S extends Observaciones> Mono<S> save(S entity);

    Flux<Observaciones> findAllBy(Pageable pageable);

    Flux<Observaciones> findAll();

    Mono<Observaciones> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Observaciones> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Observaciones> findOneWithEagerRelationships(Long id);

    Flux<Observaciones> findAllWithEagerRelationships();

    Flux<Observaciones> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
