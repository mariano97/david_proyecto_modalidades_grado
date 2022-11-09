package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ObservacionesRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.ObservacionesService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ObservacionesDTO;
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
 * REST controller for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Observaciones}.
 */
@RestController
@RequestMapping("/api")
public class ObservacionesResource {

    private final Logger log = LoggerFactory.getLogger(ObservacionesResource.class);

    private static final String ENTITY_NAME = "observaciones";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObservacionesService observacionesService;

    private final ObservacionesRepository observacionesRepository;

    public ObservacionesResource(ObservacionesService observacionesService, ObservacionesRepository observacionesRepository) {
        this.observacionesService = observacionesService;
        this.observacionesRepository = observacionesRepository;
    }

    /**
     * {@code POST  /observaciones} : Create a new observaciones.
     *
     * @param observacionesDTO the observacionesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new observacionesDTO, or with status {@code 400 (Bad Request)} if the observaciones has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/observaciones")
    public Mono<ResponseEntity<ObservacionesDTO>> createObservaciones(@Valid @RequestBody ObservacionesDTO observacionesDTO)
        throws URISyntaxException {
        log.debug("REST request to save Observaciones : {}", observacionesDTO);
        if (observacionesDTO.getId() != null) {
            throw new BadRequestAlertException("A new observaciones cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return observacionesService
            .save(observacionesDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/observaciones/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /observaciones/:id} : Updates an existing observaciones.
     *
     * @param id the id of the observacionesDTO to save.
     * @param observacionesDTO the observacionesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated observacionesDTO,
     * or with status {@code 400 (Bad Request)} if the observacionesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the observacionesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/observaciones/{id}")
    public Mono<ResponseEntity<ObservacionesDTO>> updateObservaciones(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ObservacionesDTO observacionesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Observaciones : {}, {}", id, observacionesDTO);
        if (observacionesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, observacionesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return observacionesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return observacionesService
                    .update(observacionesDTO)
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
     * {@code PATCH  /observaciones/:id} : Partial updates given fields of an existing observaciones, field will ignore if it is null
     *
     * @param id the id of the observacionesDTO to save.
     * @param observacionesDTO the observacionesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated observacionesDTO,
     * or with status {@code 400 (Bad Request)} if the observacionesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the observacionesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the observacionesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/observaciones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ObservacionesDTO>> partialUpdateObservaciones(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ObservacionesDTO observacionesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Observaciones partially : {}, {}", id, observacionesDTO);
        if (observacionesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, observacionesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return observacionesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ObservacionesDTO> result = observacionesService.partialUpdate(observacionesDTO);

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
     * {@code GET  /observaciones} : get all the observaciones.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of observaciones in body.
     */
    @GetMapping("/observaciones")
    public Mono<ResponseEntity<List<ObservacionesDTO>>> getAllObservaciones(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Observaciones");
        return observacionesService
            .countAll()
            .zipWith(observacionesService.findAll(pageable).collectList())
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
     * {@code GET  /observaciones/:id} : get the "id" observaciones.
     *
     * @param id the id of the observacionesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the observacionesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/observaciones/{id}")
    public Mono<ResponseEntity<ObservacionesDTO>> getObservaciones(@PathVariable Long id) {
        log.debug("REST request to get Observaciones : {}", id);
        Mono<ObservacionesDTO> observacionesDTO = observacionesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(observacionesDTO);
    }

    /**
     * {@code DELETE  /observaciones/:id} : delete the "id" observaciones.
     *
     * @param id the id of the observacionesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/observaciones/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteObservaciones(@PathVariable Long id) {
        log.debug("REST request to delete Observaciones : {}", id);
        return observacionesService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
