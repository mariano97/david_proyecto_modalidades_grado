package com.usco.ingenieria.software.pagina.modalidades.grado.service;

import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ObservacionesDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Observaciones}.
 */
public interface ObservacionesService {
    /**
     * Save a observaciones.
     *
     * @param observacionesDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ObservacionesDTO> save(ObservacionesDTO observacionesDTO);

    /**
     * Updates a observaciones.
     *
     * @param observacionesDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ObservacionesDTO> update(ObservacionesDTO observacionesDTO);

    /**
     * Partially updates a observaciones.
     *
     * @param observacionesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ObservacionesDTO> partialUpdate(ObservacionesDTO observacionesDTO);

    /**
     * Get all the observaciones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ObservacionesDTO> findAll(Pageable pageable);

    /**
     * Get all the observaciones with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ObservacionesDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of observaciones available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" observaciones.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ObservacionesDTO> findOne(Long id);

    /**
     * Delete the "id" observaciones.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
