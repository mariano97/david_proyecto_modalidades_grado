package com.usco.ingenieria.software.pagina.modalidades.grado.service;

import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.EmpresaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Empresa}.
 */
public interface EmpresaService {
    /**
     * Save a empresa.
     *
     * @param empresaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EmpresaDTO> save(EmpresaDTO empresaDTO);

    /**
     * Updates a empresa.
     *
     * @param empresaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EmpresaDTO> update(EmpresaDTO empresaDTO);

    /**
     * Partially updates a empresa.
     *
     * @param empresaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EmpresaDTO> partialUpdate(EmpresaDTO empresaDTO);

    /**
     * Get all the empresas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EmpresaDTO> findAll(Pageable pageable);

    /**
     * Returns the number of empresas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" empresa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EmpresaDTO> findOne(Long id);

    /**
     * Delete the "id" empresa.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
