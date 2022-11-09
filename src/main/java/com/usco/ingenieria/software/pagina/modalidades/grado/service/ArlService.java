package com.usco.ingenieria.software.pagina.modalidades.grado.service;

import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ArlDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Arl}.
 */
public interface ArlService {
    /**
     * Save a arl.
     *
     * @param arlDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ArlDTO> save(ArlDTO arlDTO);

    /**
     * Updates a arl.
     *
     * @param arlDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ArlDTO> update(ArlDTO arlDTO);

    /**
     * Partially updates a arl.
     *
     * @param arlDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ArlDTO> partialUpdate(ArlDTO arlDTO);

    /**
     * Get all the arls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ArlDTO> findAll(Pageable pageable);

    /**
     * Returns the number of arls available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" arl.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ArlDTO> findOne(Long id);

    /**
     * Delete the "id" arl.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
