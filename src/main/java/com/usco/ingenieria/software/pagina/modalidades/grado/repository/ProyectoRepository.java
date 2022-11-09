package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Proyecto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProyectoRepository extends ReactiveCrudRepository<Proyecto, Long>, ProyectoRepositoryInternal {
    Flux<Proyecto> findAllBy(Pageable pageable);

    @Override
    Mono<Proyecto> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Proyecto> findAllWithEagerRelationships();

    @Override
    Flux<Proyecto> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM proyecto entity WHERE entity.tipo_modalidad_id = :id")
    Flux<Proyecto> findByTipoModalidad(Long id);

    @Query("SELECT * FROM proyecto entity WHERE entity.tipo_modalidad_id IS NULL")
    Flux<Proyecto> findAllWhereTipoModalidadIsNull();

    @Query("SELECT * FROM proyecto entity WHERE entity.empresa_id = :id")
    Flux<Proyecto> findByEmpresa(Long id);

    @Query("SELECT * FROM proyecto entity WHERE entity.empresa_id IS NULL")
    Flux<Proyecto> findAllWhereEmpresaIsNull();

    @Query("SELECT * FROM proyecto entity WHERE entity.arl_id = :id")
    Flux<Proyecto> findByArl(Long id);

    @Query("SELECT * FROM proyecto entity WHERE entity.arl_id IS NULL")
    Flux<Proyecto> findAllWhereArlIsNull();

    @Override
    <S extends Proyecto> Mono<S> save(S entity);

    @Override
    Flux<Proyecto> findAll();

    @Override
    Mono<Proyecto> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProyectoRepositoryInternal {
    <S extends Proyecto> Mono<S> save(S entity);

    Flux<Proyecto> findAllBy(Pageable pageable);

    Flux<Proyecto> findAll();

    Mono<Proyecto> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Proyecto> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Proyecto> findOneWithEagerRelationships(Long id);

    Flux<Proyecto> findAllWithEagerRelationships();

    Flux<Proyecto> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
