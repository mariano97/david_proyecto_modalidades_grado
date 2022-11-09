package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Empresa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Empresa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpresaRepository extends ReactiveCrudRepository<Empresa, Long>, EmpresaRepositoryInternal {
    Flux<Empresa> findAllBy(Pageable pageable);

    @Override
    <S extends Empresa> Mono<S> save(S entity);

    @Override
    Flux<Empresa> findAll();

    @Override
    Mono<Empresa> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EmpresaRepositoryInternal {
    <S extends Empresa> Mono<S> save(S entity);

    Flux<Empresa> findAllBy(Pageable pageable);

    Flux<Empresa> findAll();

    Mono<Empresa> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Empresa> findAllBy(Pageable pageable, Criteria criteria);

}
