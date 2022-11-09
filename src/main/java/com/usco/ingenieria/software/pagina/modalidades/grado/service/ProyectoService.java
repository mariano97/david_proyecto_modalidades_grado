package com.usco.ingenieria.software.pagina.modalidades.grado.service;

import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ProyectoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto}.
 */
public interface ProyectoService {
    /**
     * Save a proyecto.
     *
     * @param proyectoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ProyectoDTO> save(ProyectoDTO proyectoDTO);

    /**
     * Updates a proyecto.
     *
     * @param proyectoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ProyectoDTO> update(ProyectoDTO proyectoDTO);

    /**
     * Partially updates a proyecto.
     *
     * @param proyectoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ProyectoDTO> partialUpdate(ProyectoDTO proyectoDTO);

    /**
     * Get all the proyectos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProyectoDTO> findAll(Pageable pageable);

    /**
     * Get all the proyectos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProyectoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of proyectos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" proyecto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ProyectoDTO> findOne(Long id);

    /**
     * Delete the "id" proyecto.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
