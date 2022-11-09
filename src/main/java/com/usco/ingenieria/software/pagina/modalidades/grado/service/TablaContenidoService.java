package com.usco.ingenieria.software.pagina.modalidades.grado.service;

import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaContenidoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido}.
 */
public interface TablaContenidoService {
    /**
     * Save a tablaContenido.
     *
     * @param tablaContenidoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TablaContenidoDTO> save(TablaContenidoDTO tablaContenidoDTO);

    /**
     * Updates a tablaContenido.
     *
     * @param tablaContenidoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TablaContenidoDTO> update(TablaContenidoDTO tablaContenidoDTO);

    /**
     * Partially updates a tablaContenido.
     *
     * @param tablaContenidoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TablaContenidoDTO> partialUpdate(TablaContenidoDTO tablaContenidoDTO);

    /**
     * Get all the tablaContenidos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TablaContenidoDTO> findAll(Pageable pageable);

    /**
     * Get all the tablaContenidos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TablaContenidoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of tablaContenidos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" tablaContenido.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TablaContenidoDTO> findOne(Long id);

    /**
     * Delete the "id" tablaContenido.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
