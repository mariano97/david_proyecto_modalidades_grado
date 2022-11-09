package com.usco.ingenieria.software.pagina.modalidades.grado.service;

import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.EstudianteDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Estudiante}.
 */
public interface EstudianteService {
    /**
     * Save a estudiante.
     *
     * @param estudianteDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EstudianteDTO> save(EstudianteDTO estudianteDTO);

    /**
     * Updates a estudiante.
     *
     * @param estudianteDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EstudianteDTO> update(EstudianteDTO estudianteDTO);

    /**
     * Partially updates a estudiante.
     *
     * @param estudianteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EstudianteDTO> partialUpdate(EstudianteDTO estudianteDTO);

    /**
     * Get all the estudiantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EstudianteDTO> findAll(Pageable pageable);

    /**
     * Get all the estudiantes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EstudianteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of estudiantes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" estudiante.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EstudianteDTO> findOne(Long id);

    /**
     * Delete the "id" estudiante.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
