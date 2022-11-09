package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Arl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Arl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArlRepository extends ReactiveCrudRepository<Arl, Long>, ArlRepositoryInternal {
    Flux<Arl> findAllBy(Pageable pageable);

    @Override
    <S extends Arl> Mono<S> save(S entity);

    @Override
    Flux<Arl> findAll();

    @Override
    Mono<Arl> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ArlRepositoryInternal {
    <S extends Arl> Mono<S> save(S entity);

    Flux<Arl> findAllBy(Pageable pageable);

    Flux<Arl> findAll();

    Mono<Arl> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Arl> findAllBy(Pageable pageable, Criteria criteria);

}
