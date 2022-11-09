package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ArlRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.ArlService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ArlDTO;
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
 * REST controller for managing {@link com.usco.ingenieria.software.pagina.modalidades.grado.domain.Arl}.
 */
@RestController
@RequestMapping("/api")
public class ArlResource {

    private final Logger log = LoggerFactory.getLogger(ArlResource.class);

    private static final String ENTITY_NAME = "arl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArlService arlService;

    private final ArlRepository arlRepository;

    public ArlResource(ArlService arlService, ArlRepository arlRepository) {
        this.arlService = arlService;
        this.arlRepository = arlRepository;
    }

    /**
     * {@code POST  /arls} : Create a new arl.
     *
     * @param arlDTO the arlDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arlDTO, or with status {@code 400 (Bad Request)} if the arl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arls")
    public Mono<ResponseEntity<ArlDTO>> createArl(@Valid @RequestBody ArlDTO arlDTO) throws URISyntaxException {
        log.debug("REST request to save Arl : {}", arlDTO);
        if (arlDTO.getId() != null) {
            throw new BadRequestAlertException("A new arl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return arlService
            .save(arlDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/arls/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /arls/:id} : Updates an existing arl.
     *
     * @param id the id of the arlDTO to save.
     * @param arlDTO the arlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arlDTO,
     * or with status {@code 400 (Bad Request)} if the arlDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arls/{id}")
    public Mono<ResponseEntity<ArlDTO>> updateArl(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArlDTO arlDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Arl : {}, {}", id, arlDTO);
        if (arlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arlDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return arlRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return arlService
                    .update(arlDTO)
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
     * {@code PATCH  /arls/:id} : Partial updates given fields of an existing arl, field will ignore if it is null
     *
     * @param id the id of the arlDTO to save.
     * @param arlDTO the arlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arlDTO,
     * or with status {@code 400 (Bad Request)} if the arlDTO is not valid,
     * or with status {@code 404 (Not Found)} if the arlDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the arlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/arls/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ArlDTO>> partialUpdateArl(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArlDTO arlDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Arl partially : {}, {}", id, arlDTO);
        if (arlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arlDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return arlRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ArlDTO> result = arlService.partialUpdate(arlDTO);

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
     * {@code GET  /arls} : get all the arls.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arls in body.
     */
    @GetMapping("/arls")
    public Mono<ResponseEntity<List<ArlDTO>>> getAllArls(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Arls");
        return arlService
            .countAll()
            .zipWith(arlService.findAll(pageable).collectList())
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
     * {@code GET  /arls/:id} : get the "id" arl.
     *
     * @param id the id of the arlDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arlDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arls/{id}")
    public Mono<ResponseEntity<ArlDTO>> getArl(@PathVariable Long id) {
        log.debug("REST request to get Arl : {}", id);
        Mono<ArlDTO> arlDTO = arlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arlDTO);
    }

    /**
     * {@code DELETE  /arls/:id} : delete the "id" arl.
     *
     * @param id the id of the arlDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arls/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteArl(@PathVariable Long id) {
        log.debug("REST request to delete Arl : {}", id);
        return arlService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
