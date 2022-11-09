package com.usco.ingenieria.software.pagina.modalidades.grado.service;

import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaMaestraDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaMaestra}.
 */
public interface TablaMaestraService {
    /**
     * Save a tablaMaestra.
     *
     * @param tablaMaestraDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TablaMaestraDTO> save(TablaMaestraDTO tablaMaestraDTO);

    /**
     * Updates a tablaMaestra.
     *
     * @param tablaMaestraDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TablaMaestraDTO> update(TablaMaestraDTO tablaMaestraDTO);

    /**
     * Partially updates a tablaMaestra.
     *
     * @param tablaMaestraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TablaMaestraDTO> partialUpdate(TablaMaestraDTO tablaMaestraDTO);

    /**
     * Get all the tablaMaestras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TablaMaestraDTO> findAll(Pageable pageable);

    /**
     * Returns the number of tablaMaestras available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" tablaMaestra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TablaMaestraDTO> findOne(Long id);

    /**
     * Delete the "id" tablaMaestra.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
