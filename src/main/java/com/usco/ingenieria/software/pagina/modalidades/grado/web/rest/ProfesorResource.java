package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ProfesorRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.ProfesorService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ProfesorDTO;
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
 * REST controller for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Profesor}.
 */
@RestController
@RequestMapping("/api")
public class ProfesorResource {

    private final Logger log = LoggerFactory.getLogger(ProfesorResource.class);

    private static final String ENTITY_NAME = "profesor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfesorService profesorService;

    private final ProfesorRepository profesorRepository;

    public ProfesorResource(ProfesorService profesorService, ProfesorRepository profesorRepository) {
        this.profesorService = profesorService;
        this.profesorRepository = profesorRepository;
    }

    /**
     * {@code POST  /profesors} : Create a new profesor.
     *
     * @param profesorDTO the profesorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profesorDTO, or with status {@code 400 (Bad Request)} if the profesor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/profesors")
    public Mono<ResponseEntity<ProfesorDTO>> createProfesor(@Valid @RequestBody ProfesorDTO profesorDTO) throws URISyntaxException {
        log.debug("REST request to save Profesor : {}", profesorDTO);
        if (profesorDTO.getId() != null) {
            throw new BadRequestAlertException("A new profesor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return profesorService
            .save(profesorDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/profesors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /profesors/:id} : Updates an existing profesor.
     *
     * @param id the id of the profesorDTO to save.
     * @param profesorDTO the profesorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profesorDTO,
     * or with status {@code 400 (Bad Request)} if the profesorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profesorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/profesors/{id}")
    public Mono<ResponseEntity<ProfesorDTO>> updateProfesor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfesorDTO profesorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Profesor : {}, {}", id, profesorDTO);
        if (profesorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profesorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return profesorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return profesorService
                    .update(profesorDTO)
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
     * {@code PATCH  /profesors/:id} : Partial updates given fields of an existing profesor, field will ignore if it is null
     *
     * @param id the id of the profesorDTO to save.
     * @param profesorDTO the profesorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profesorDTO,
     * or with status {@code 400 (Bad Request)} if the profesorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the profesorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the profesorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/profesors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ProfesorDTO>> partialUpdateProfesor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfesorDTO profesorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Profesor partially : {}, {}", id, profesorDTO);
        if (profesorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profesorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return profesorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ProfesorDTO> result = profesorService.partialUpdate(profesorDTO);

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
     * {@code GET  /profesors} : get all the profesors.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profesors in body.
     */
    @GetMapping("/profesors")
    public Mono<ResponseEntity<List<ProfesorDTO>>> getAllProfesors(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Profesors");
        return profesorService
            .countAll()
            .zipWith(profesorService.findAll(pageable).collectList())
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
     * {@code GET  /profesors/:id} : get the "id" profesor.
     *
     * @param id the id of the profesorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profesorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profesors/{id}")
    public Mono<ResponseEntity<ProfesorDTO>> getProfesor(@PathVariable Long id) {
        log.debug("REST request to get Profesor : {}", id);
        Mono<ProfesorDTO> profesorDTO = profesorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profesorDTO);
    }

    /**
     * {@code DELETE  /profesors/:id} : delete the "id" profesor.
     *
     * @param id the id of the profesorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/profesors/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteProfesor(@PathVariable Long id) {
        log.debug("REST request to delete Profesor : {}", id);
        return profesorService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
