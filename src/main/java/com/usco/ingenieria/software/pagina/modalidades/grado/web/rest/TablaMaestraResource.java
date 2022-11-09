package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import com.usco.ingenieria.software.pagina.modalidades.grado.repository.TablaMaestraRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.TablaMaestraService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaMaestraDTO;
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
 * REST controller for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaMaestra}.
 */
@RestController
@RequestMapping("/api")
public class TablaMaestraResource {

    private final Logger log = LoggerFactory.getLogger(TablaMaestraResource.class);

    private static final String ENTITY_NAME = "tablaMaestra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TablaMaestraService tablaMaestraService;

    private final TablaMaestraRepository tablaMaestraRepository;

    public TablaMaestraResource(TablaMaestraService tablaMaestraService, TablaMaestraRepository tablaMaestraRepository) {
        this.tablaMaestraService = tablaMaestraService;
        this.tablaMaestraRepository = tablaMaestraRepository;
    }

    /**
     * {@code POST  /tabla-maestras} : Create a new tablaMaestra.
     *
     * @param tablaMaestraDTO the tablaMaestraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tablaMaestraDTO, or with status {@code 400 (Bad Request)} if the tablaMaestra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tabla-maestras")
    public Mono<ResponseEntity<TablaMaestraDTO>> createTablaMaestra(@Valid @RequestBody TablaMaestraDTO tablaMaestraDTO)
        throws URISyntaxException {
        log.debug("REST request to save TablaMaestra : {}", tablaMaestraDTO);
        if (tablaMaestraDTO.getId() != null) {
            throw new BadRequestAlertException("A new tablaMaestra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return tablaMaestraService
            .save(tablaMaestraDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tabla-maestras/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tabla-maestras/:id} : Updates an existing tablaMaestra.
     *
     * @param id the id of the tablaMaestraDTO to save.
     * @param tablaMaestraDTO the tablaMaestraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tablaMaestraDTO,
     * or with status {@code 400 (Bad Request)} if the tablaMaestraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tablaMaestraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tabla-maestras/{id}")
    public Mono<ResponseEntity<TablaMaestraDTO>> updateTablaMaestra(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TablaMaestraDTO tablaMaestraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TablaMaestra : {}, {}", id, tablaMaestraDTO);
        if (tablaMaestraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tablaMaestraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tablaMaestraRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return tablaMaestraService
                    .update(tablaMaestraDTO)
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
     * {@code PATCH  /tabla-maestras/:id} : Partial updates given fields of an existing tablaMaestra, field will ignore if it is null
     *
     * @param id the id of the tablaMaestraDTO to save.
     * @param tablaMaestraDTO the tablaMaestraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tablaMaestraDTO,
     * or with status {@code 400 (Bad Request)} if the tablaMaestraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tablaMaestraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tablaMaestraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tabla-maestras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TablaMaestraDTO>> partialUpdateTablaMaestra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TablaMaestraDTO tablaMaestraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TablaMaestra partially : {}, {}", id, tablaMaestraDTO);
        if (tablaMaestraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tablaMaestraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tablaMaestraRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TablaMaestraDTO> result = tablaMaestraService.partialUpdate(tablaMaestraDTO);

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
     * {@code GET  /tabla-maestras} : get all the tablaMaestras.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tablaMaestras in body.
     */
    @GetMapping("/tabla-maestras")
    public Mono<ResponseEntity<List<TablaMaestraDTO>>> getAllTablaMaestras(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of TablaMaestras");
        return tablaMaestraService
            .countAll()
            .zipWith(tablaMaestraService.findAll(pageable).collectList())
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
     * {@code GET  /tabla-maestras/:id} : get the "id" tablaMaestra.
     *
     * @param id the id of the tablaMaestraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tablaMaestraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tabla-maestras/{id}")
    public Mono<ResponseEntity<TablaMaestraDTO>> getTablaMaestra(@PathVariable Long id) {
        log.debug("REST request to get TablaMaestra : {}", id);
        Mono<TablaMaestraDTO> tablaMaestraDTO = tablaMaestraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tablaMaestraDTO);
    }

    /**
     * {@code DELETE  /tabla-maestras/:id} : delete the "id" tablaMaestra.
     *
     * @param id the id of the tablaMaestraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tabla-maestras/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTablaMaestra(@PathVariable Long id) {
        log.debug("REST request to delete TablaMaestra : {}", id);
        return tablaMaestraService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
