package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaMaestra;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the TablaMaestra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TablaMaestraRepository extends ReactiveCrudRepository<TablaMaestra, Long>, TablaMaestraRepositoryInternal {
    Flux<TablaMaestra> findAllBy(Pageable pageable);

    @Override
    <S extends TablaMaestra> Mono<S> save(S entity);

    @Override
    Flux<TablaMaestra> findAll();

    @Override
    Mono<TablaMaestra> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TablaMaestraRepositoryInternal {
    <S extends TablaMaestra> Mono<S> save(S entity);

    Flux<TablaMaestra> findAllBy(Pageable pageable);

    Flux<TablaMaestra> findAll();

    Mono<TablaMaestra> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TablaMaestra> findAllBy(Pageable pageable, Criteria criteria);

}
