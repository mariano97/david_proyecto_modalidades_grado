package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Estudiante;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Estudiante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstudianteRepository extends ReactiveCrudRepository<Estudiante, Long>, EstudianteRepositoryInternal {
    Flux<Estudiante> findAllBy(Pageable pageable);

    @Override
    Mono<Estudiante> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Estudiante> findAllWithEagerRelationships();

    @Override
    Flux<Estudiante> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM estudiante entity WHERE entity.tipo_documento_id = :id")
    Flux<Estudiante> findByTipoDocumento(Long id);

    @Query("SELECT * FROM estudiante entity WHERE entity.tipo_documento_id IS NULL")
    Flux<Estudiante> findAllWhereTipoDocumentoIsNull();

    @Query("SELECT * FROM estudiante entity WHERE entity.proyecto_id = :id")
    Flux<Estudiante> findByProyecto(Long id);

    @Query("SELECT * FROM estudiante entity WHERE entity.proyecto_id IS NULL")
    Flux<Estudiante> findAllWhereProyectoIsNull();

    @Override
    <S extends Estudiante> Mono<S> save(S entity);

    @Override
    Flux<Estudiante> findAll();

    @Override
    Mono<Estudiante> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EstudianteRepositoryInternal {
    <S extends Estudiante> Mono<S> save(S entity);

    Flux<Estudiante> findAllBy(Pageable pageable);

    Flux<Estudiante> findAll();

    Mono<Estudiante> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Estudiante> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Estudiante> findOneWithEagerRelationships(Long id);

    Flux<Estudiante> findAllWithEagerRelationships();

    Flux<Estudiante> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
