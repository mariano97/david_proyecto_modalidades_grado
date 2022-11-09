package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.usco.ingenieria.software.pagina.modalidades.grado.IntegrationTest;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Observaciones;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EntityManager;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ObservacionesRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.ObservacionesService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ObservacionesDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.ObservacionesMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link ObservacionesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ObservacionesResourceIT {

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/observaciones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObservacionesRepository observacionesRepository;

    @Mock
    private ObservacionesRepository observacionesRepositoryMock;

    @Autowired
    private ObservacionesMapper observacionesMapper;

    @Mock
    private ObservacionesService observacionesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Observaciones observaciones;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Observaciones createEntity(EntityManager em) {
        Observaciones observaciones = new Observaciones().observacion(DEFAULT_OBSERVACION);
        // Add required entity
        Proyecto proyecto;
        proyecto = em.insert(ProyectoResourceIT.createEntity(em)).block();
        observaciones.setProyecto(proyecto);
        return observaciones;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Observaciones createUpdatedEntity(EntityManager em) {
        Observaciones observaciones = new Observaciones().observacion(UPDATED_OBSERVACION);
        // Add required entity
        Proyecto proyecto;
        proyecto = em.insert(ProyectoResourceIT.createUpdatedEntity(em)).block();
        observaciones.setProyecto(proyecto);
        return observaciones;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Observaciones.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        ProyectoResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        observaciones = createEntity(em);
    }

    @Test
    void createObservaciones() throws Exception {
        int databaseSizeBeforeCreate = observacionesRepository.findAll().collectList().block().size();
        // Create the Observaciones
        ObservacionesDTO observacionesDTO = observacionesMapper.toDto(observaciones);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(observacionesDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeCreate + 1);
        Observaciones testObservaciones = observacionesList.get(observacionesList.size() - 1);
        assertThat(testObservaciones.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    void createObservacionesWithExistingId() throws Exception {
        // Create the Observaciones with an existing ID
        observaciones.setId(1L);
        ObservacionesDTO observacionesDTO = observacionesMapper.toDto(observaciones);

        int databaseSizeBeforeCreate = observacionesRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(observacionesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkObservacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = observacionesRepository.findAll().collectList().block().size();
        // set the field null
        observaciones.setObservacion(null);

        // Create the Observaciones, which fails.
        ObservacionesDTO observacionesDTO = observacionesMapper.toDto(observaciones);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(observacionesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllObservaciones() {
        // Initialize the database
        observacionesRepository.save(observaciones).block();

        // Get all the observacionesList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(observaciones.getId().intValue()))
            .jsonPath("$.[*].observacion")
            .value(hasItem(DEFAULT_OBSERVACION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllObservacionesWithEagerRelationshipsIsEnabled() {
        when(observacionesServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(observacionesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllObservacionesWithEagerRelationshipsIsNotEnabled() {
        when(observacionesServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(observacionesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getObservaciones() {
        // Initialize the database
        observacionesRepository.save(observaciones).block();

        // Get the observaciones
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, observaciones.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(observaciones.getId().intValue()))
            .jsonPath("$.observacion")
            .value(is(DEFAULT_OBSERVACION));
    }

    @Test
    void getNonExistingObservaciones() {
        // Get the observaciones
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewObservaciones() throws Exception {
        // Initialize the database
        observacionesRepository.save(observaciones).block();

        int databaseSizeBeforeUpdate = observacionesRepository.findAll().collectList().block().size();

        // Update the observaciones
        Observaciones updatedObservaciones = observacionesRepository.findById(observaciones.getId()).block();
        updatedObservaciones.observacion(UPDATED_OBSERVACION);
        ObservacionesDTO observacionesDTO = observacionesMapper.toDto(updatedObservaciones);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, observacionesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(observacionesDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeUpdate);
        Observaciones testObservaciones = observacionesList.get(observacionesList.size() - 1);
        assertThat(testObservaciones.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    void putNonExistingObservaciones() throws Exception {
        int databaseSizeBeforeUpdate = observacionesRepository.findAll().collectList().block().size();
        observaciones.setId(count.incrementAndGet());

        // Create the Observaciones
        ObservacionesDTO observacionesDTO = observacionesMapper.toDto(observaciones);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, observacionesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(observacionesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchObservaciones() throws Exception {
        int databaseSizeBeforeUpdate = observacionesRepository.findAll().collectList().block().size();
        observaciones.setId(count.incrementAndGet());

        // Create the Observaciones
        ObservacionesDTO observacionesDTO = observacionesMapper.toDto(observaciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(observacionesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamObservaciones() throws Exception {
        int databaseSizeBeforeUpdate = observacionesRepository.findAll().collectList().block().size();
        observaciones.setId(count.incrementAndGet());

        // Create the Observaciones
        ObservacionesDTO observacionesDTO = observacionesMapper.toDto(observaciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(observacionesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateObservacionesWithPatch() throws Exception {
        // Initialize the database
        observacionesRepository.save(observaciones).block();

        int databaseSizeBeforeUpdate = observacionesRepository.findAll().collectList().block().size();

        // Update the observaciones using partial update
        Observaciones partialUpdatedObservaciones = new Observaciones();
        partialUpdatedObservaciones.setId(observaciones.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedObservaciones.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedObservaciones))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeUpdate);
        Observaciones testObservaciones = observacionesList.get(observacionesList.size() - 1);
        assertThat(testObservaciones.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    void fullUpdateObservacionesWithPatch() throws Exception {
        // Initialize the database
        observacionesRepository.save(observaciones).block();

        int databaseSizeBeforeUpdate = observacionesRepository.findAll().collectList().block().size();

        // Update the observaciones using partial update
        Observaciones partialUpdatedObservaciones = new Observaciones();
        partialUpdatedObservaciones.setId(observaciones.getId());

        partialUpdatedObservaciones.observacion(UPDATED_OBSERVACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedObservaciones.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedObservaciones))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeUpdate);
        Observaciones testObservaciones = observacionesList.get(observacionesList.size() - 1);
        assertThat(testObservaciones.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    void patchNonExistingObservaciones() throws Exception {
        int databaseSizeBeforeUpdate = observacionesRepository.findAll().collectList().block().size();
        observaciones.setId(count.incrementAndGet());

        // Create the Observaciones
        ObservacionesDTO observacionesDTO = observacionesMapper.toDto(observaciones);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, observacionesDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(observacionesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchObservaciones() throws Exception {
        int databaseSizeBeforeUpdate = observacionesRepository.findAll().collectList().block().size();
        observaciones.setId(count.incrementAndGet());

        // Create the Observaciones
        ObservacionesDTO observacionesDTO = observacionesMapper.toDto(observaciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(observacionesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamObservaciones() throws Exception {
        int databaseSizeBeforeUpdate = observacionesRepository.findAll().collectList().block().size();
        observaciones.setId(count.incrementAndGet());

        // Create the Observaciones
        ObservacionesDTO observacionesDTO = observacionesMapper.toDto(observaciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(observacionesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Observaciones in the database
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteObservaciones() {
        // Initialize the database
        observacionesRepository.save(observaciones).block();

        int databaseSizeBeforeDelete = observacionesRepository.findAll().collectList().block().size();

        // Delete the observaciones
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, observaciones.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Observaciones> observacionesList = observacionesRepository.findAll().collectList().block();
        assertThat(observacionesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
