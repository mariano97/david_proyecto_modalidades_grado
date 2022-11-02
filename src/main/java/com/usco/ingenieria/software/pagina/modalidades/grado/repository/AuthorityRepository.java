package com.usco.ingenieria.software.pagina.modalidades.grado.repository;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Authority;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * Spring Data R2DBC repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends R2dbcRepository<Authority, String> {}
