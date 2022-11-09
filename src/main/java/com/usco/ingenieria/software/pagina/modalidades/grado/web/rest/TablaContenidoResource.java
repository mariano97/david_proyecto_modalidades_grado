package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import com.usco.ingenieria.software.pagina.modalidades.grado.repository.TablaContenidoRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.TablaContenidoService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaContenidoDTO;
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
 * REST controller for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido}.
 */
@RestController
@RequestMapping("/api")
public class TablaContenidoResource {

    private final Logger log = LoggerFactory.getLogger(TablaContenidoResource.class);

    private static final String ENTITY_NAME = "tablaContenido";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TablaContenidoService tablaContenidoService;

    private final TablaContenidoRepository tablaContenidoRepository;

    public TablaContenidoResource(TablaContenidoService tablaContenidoService, TablaContenidoRepository tablaContenidoRepository) {
        this.tablaContenidoService = tablaContenidoService;
        this.tablaContenidoRepository = tablaContenidoRepository;
    }

    /**
     * {@code POST  /tabla-contenidos} : Create a new tablaContenido.
     *
     * @param tablaContenidoDTO the tablaContenidoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tablaContenidoDTO, or with status {@code 400 (Bad Request)} if the tablaContenido has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tabla-contenidos")
    public Mono<ResponseEntity<TablaContenidoDTO>> createTablaContenido(@Valid @RequestBody TablaContenidoDTO tablaContenidoDTO)
        throws URISyntaxException {
        log.debug("REST request to save TablaContenido : {}", tablaContenidoDTO);
        if (tablaContenidoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tablaContenido cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return tablaContenidoService
            .save(tablaContenidoDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tabla-contenidos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tabla-contenidos/:id} : Updates an existing tablaContenido.
     *
     * @param id the id of the tablaContenidoDTO to save.
     * @param tablaContenidoDTO the tablaContenidoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tablaContenidoDTO,
     * or with status {@code 400 (Bad Request)} if the tablaContenidoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tablaContenidoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tabla-contenidos/{id}")
    public Mono<ResponseEntity<TablaContenidoDTO>> updateTablaContenido(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TablaContenidoDTO tablaContenidoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TablaContenido : {}, {}", id, tablaContenidoDTO);
        if (tablaContenidoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tablaContenidoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tablaContenidoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return tablaContenidoService
                    .update(tablaContenidoDTO)
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
     * {@code PATCH  /tabla-contenidos/:id} : Partial updates given fields of an existing tablaContenido, field will ignore if it is null
     *
     * @param id the id of the tablaContenidoDTO to save.
     * @param tablaContenidoDTO the tablaContenidoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tablaContenidoDTO,
     * or with status {@code 400 (Bad Request)} if the tablaContenidoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tablaContenidoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tablaContenidoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tabla-contenidos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TablaContenidoDTO>> partialUpdateTablaContenido(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TablaContenidoDTO tablaContenidoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TablaContenido partially : {}, {}", id, tablaContenidoDTO);
        if (tablaContenidoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tablaContenidoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tablaContenidoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TablaContenidoDTO> result = tablaContenidoService.partialUpdate(tablaContenidoDTO);

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
     * {@code GET  /tabla-contenidos} : get all the tablaContenidos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tablaContenidos in body.
     */
    @GetMapping("/tabla-contenidos")
    public Mono<ResponseEntity<List<TablaContenidoDTO>>> getAllTablaContenidos(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TablaContenidos");
        return tablaContenidoService
            .countAll()
            .zipWith(tablaContenidoService.findAll(pageable).collectList())
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
     * {@code GET  /tabla-contenidos/:id} : get the "id" tablaContenido.
     *
     * @param id the id of the tablaContenidoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tablaContenidoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tabla-contenidos/{id}")
    public Mono<ResponseEntity<TablaContenidoDTO>> getTablaContenido(@PathVariable Long id) {
        log.debug("REST request to get TablaContenido : {}", id);
        Mono<TablaContenidoDTO> tablaContenidoDTO = tablaContenidoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tablaContenidoDTO);
    }

    /**
     * {@code DELETE  /tabla-contenidos/:id} : delete the "id" tablaContenido.
     *
     * @param id the id of the tablaContenidoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tabla-contenidos/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTablaContenido(@PathVariable Long id) {
        log.debug("REST request to delete TablaContenido : {}", id);
        return tablaContenidoService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
