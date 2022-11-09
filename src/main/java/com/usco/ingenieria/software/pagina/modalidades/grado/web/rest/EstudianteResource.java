package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EstudianteRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.EstudianteService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.EstudianteDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Estudiante}.
 */
@RestController
@RequestMapping("/api")
public class EstudianteResource {

    private final Logger log = LoggerFactory.getLogger(EstudianteResource.class);

    private static final String ENTITY_NAME = "estudiante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstudianteService estudianteService;

    private final EstudianteRepository estudianteRepository;

    public EstudianteResource(EstudianteService estudianteService, EstudianteRepository estudianteRepository) {
        this.estudianteService = estudianteService;
        this.estudianteRepository = estudianteRepository;
    }

    /**
     * {@code POST  /estudiantes} : Create a new estudiante.
     *
     * @param estudianteDTO the estudianteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estudianteDTO, or with status {@code 400 (Bad Request)} if the estudiante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/estudiantes")
    public Mono<ResponseEntity<EstudianteDTO>> createEstudiante(@Valid @RequestBody EstudianteDTO estudianteDTO) throws URISyntaxException {
        log.debug("REST request to save Estudiante : {}", estudianteDTO);
        if (estudianteDTO.getId() != null) {
            throw new BadRequestAlertException("A new estudiante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return estudianteService
            .save(estudianteDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/estudiantes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /estudiantes/:id} : Updates an existing estudiante.
     *
     * @param id the id of the estudianteDTO to save.
     * @param estudianteDTO the estudianteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estudianteDTO,
     * or with status {@code 400 (Bad Request)} if the estudianteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estudianteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/estudiantes/{id}")
    public Mono<ResponseEntity<EstudianteDTO>> updateEstudiante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EstudianteDTO estudianteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Estudiante : {}, {}", id, estudianteDTO);
        if (estudianteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estudianteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return estudianteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return estudianteService
                    .update(estudianteDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /estudiantes/:id} : Partial updates given fields of an existing estudiante, field will ignore if it is null
     *
     * @param id the id of the estudianteDTO to save.
     * @param estudianteDTO the estudianteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estudianteDTO,
     * or with status {@code 400 (Bad Request)} if the estudianteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the estudianteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the estudianteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/estudiantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EstudianteDTO>> partialUpdateEstudiante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EstudianteDTO estudianteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Estudiante partially : {}, {}", id, estudianteDTO);
        if (estudianteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estudianteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return estudianteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EstudianteDTO> result = estudianteService.partialUpdate(estudianteDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /estudiantes} : get all the estudiantes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estudiantes in body.
     */
    @GetMapping("/estudiantes")
    public Mono<ResponseEntity<List<EstudianteDTO>>> getAllEstudiantes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Estudiantes");
        return estudianteService
            .countAll()
            .zipWith(estudianteService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /estudiantes/:id} : get the "id" estudiante.
     *
     * @param id the id of the estudianteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estudianteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/estudiantes/{id}")
    public Mono<ResponseEntity<EstudianteDTO>> getEstudiante(@PathVariable Long id) {
        log.debug("REST request to get Estudiante : {}", id);
        Mono<EstudianteDTO> estudianteDTO = estudianteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estudianteDTO);
    }

    /**
     * {@code DELETE  /estudiantes/:id} : delete the "id" estudiante.
     *
     * @param id the id of the estudianteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/estudiantes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteEstudiante(@PathVariable Long id) {
        log.debug("REST request to delete Estudiante : {}", id);
        return estudianteService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
