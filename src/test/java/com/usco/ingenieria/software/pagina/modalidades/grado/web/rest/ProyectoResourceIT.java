package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.usco.ingenieria.software.pagina.modalidades.grado.IntegrationTest;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EntityManager;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ProyectoRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.ProyectoService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ProyectoDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.ProyectoMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ProyectoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProyectoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTA = false;
    private static final Boolean UPDATED_ACTA = true;

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_TERMINO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_TERMINO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/proyectos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Mock
    private ProyectoRepository proyectoRepositoryMock;

    @Autowired
    private ProyectoMapper proyectoMapper;

    @Mock
    private ProyectoService proyectoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Proyecto proyecto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyecto createEntity(EntityManager em) {
        Proyecto proyecto = new Proyecto()
            .nombre(DEFAULT_NOMBRE)
            .acta(DEFAULT_ACTA)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaTermino(DEFAULT_FECHA_TERMINO);
        // Add required entity
        TablaContenido tablaContenido;
        tablaContenido = em.insert(TablaContenidoResourceIT.createEntity(em)).block();
        proyecto.setTipoModalidad(tablaContenido);
        return proyecto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyecto createUpdatedEntity(EntityManager em) {
        Proyecto proyecto = new Proyecto()
            .nombre(UPDATED_NOMBRE)
            .acta(UPDATED_ACTA)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaTermino(UPDATED_FECHA_TERMINO);
        // Add required entity
        TablaContenido tablaContenido;
        tablaContenido = em.insert(TablaContenidoResourceIT.createUpdatedEntity(em)).block();
        proyecto.setTipoModalidad(tablaContenido);
        return proyecto;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Proyecto.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        TablaContenidoResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        proyecto = createEntity(em);
    }

    @Test
    void createProyecto() throws Exception {
        int databaseSizeBeforeCreate = proyectoRepository.findAll().collectList().block().size();
        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeCreate + 1);
        Proyecto testProyecto = proyectoList.get(proyectoList.size() - 1);
        assertThat(testProyecto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProyecto.getActa()).isEqualTo(DEFAULT_ACTA);
        assertThat(testProyecto.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testProyecto.getFechaTermino()).isEqualTo(DEFAULT_FECHA_TERMINO);
    }

    @Test
    void createProyectoWithExistingId() throws Exception {
        // Create the Proyecto with an existing ID
        proyecto.setId(1L);
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        int databaseSizeBeforeCreate = proyectoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectoRepository.findAll().collectList().block().size();
        // set the field null
        proyecto.setNombre(null);

        // Create the Proyecto, which fails.
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkActaIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectoRepository.findAll().collectList().block().size();
        // set the field null
        proyecto.setActa(null);

        // Create the Proyecto, which fails.
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = proyectoRepository.findAll().collectList().block().size();
        // set the field null
        proyecto.setFechaInicio(null);

        // Create the Proyecto, which fails.
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProyectos() {
        // Initialize the database
        proyectoRepository.save(proyecto).block();

        // Get all the proyectoList
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
            .value(hasItem(proyecto.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].acta")
            .value(hasItem(DEFAULT_ACTA.booleanValue()))
            .jsonPath("$.[*].fechaInicio")
            .value(hasItem(DEFAULT_FECHA_INICIO.toString()))
            .jsonPath("$.[*].fechaTermino")
            .value(hasItem(DEFAULT_FECHA_TERMINO.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProyectosWithEagerRelationshipsIsEnabled() {
        when(proyectoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(proyectoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProyectosWithEagerRelationshipsIsNotEnabled() {
        when(proyectoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(proyectoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getProyecto() {
        // Initialize the database
        proyectoRepository.save(proyecto).block();

        // Get the proyecto
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, proyecto.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(proyecto.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.acta")
            .value(is(DEFAULT_ACTA.booleanValue()))
            .jsonPath("$.fechaInicio")
            .value(is(DEFAULT_FECHA_INICIO.toString()))
            .jsonPath("$.fechaTermino")
            .value(is(DEFAULT_FECHA_TERMINO.toString()));
    }

    @Test
    void getNonExistingProyecto() {
        // Get the proyecto
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProyecto() throws Exception {
        // Initialize the database
        proyectoRepository.save(proyecto).block();

        int databaseSizeBeforeUpdate = proyectoRepository.findAll().collectList().block().size();

        // Update the proyecto
        Proyecto updatedProyecto = proyectoRepository.findById(proyecto.getId()).block();
        updatedProyecto.nombre(UPDATED_NOMBRE).acta(UPDATED_ACTA).fechaInicio(UPDATED_FECHA_INICIO).fechaTermino(UPDATED_FECHA_TERMINO);
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(updatedProyecto);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, proyectoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
        Proyecto testProyecto = proyectoList.get(proyectoList.size() - 1);
        assertThat(testProyecto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProyecto.getActa()).isEqualTo(UPDATED_ACTA);
        assertThat(testProyecto.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testProyecto.getFechaTermino()).isEqualTo(UPDATED_FECHA_TERMINO);
    }

    @Test
    void putNonExistingProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().collectList().block().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, proyectoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().collectList().block().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().collectList().block().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProyectoWithPatch() throws Exception {
        // Initialize the database
        proyectoRepository.save(proyecto).block();

        int databaseSizeBeforeUpdate = proyectoRepository.findAll().collectList().block().size();

        // Update the proyecto using partial update
        Proyecto partialUpdatedProyecto = new Proyecto();
        partialUpdatedProyecto.setId(proyecto.getId());

        partialUpdatedProyecto.acta(UPDATED_ACTA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProyecto.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProyecto))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
        Proyecto testProyecto = proyectoList.get(proyectoList.size() - 1);
        assertThat(testProyecto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProyecto.getActa()).isEqualTo(UPDATED_ACTA);
        assertThat(testProyecto.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testProyecto.getFechaTermino()).isEqualTo(DEFAULT_FECHA_TERMINO);
    }

    @Test
    void fullUpdateProyectoWithPatch() throws Exception {
        // Initialize the database
        proyectoRepository.save(proyecto).block();

        int databaseSizeBeforeUpdate = proyectoRepository.findAll().collectList().block().size();

        // Update the proyecto using partial update
        Proyecto partialUpdatedProyecto = new Proyecto();
        partialUpdatedProyecto.setId(proyecto.getId());

        partialUpdatedProyecto
            .nombre(UPDATED_NOMBRE)
            .acta(UPDATED_ACTA)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaTermino(UPDATED_FECHA_TERMINO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProyecto.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProyecto))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
        Proyecto testProyecto = proyectoList.get(proyectoList.size() - 1);
        assertThat(testProyecto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProyecto.getActa()).isEqualTo(UPDATED_ACTA);
        assertThat(testProyecto.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testProyecto.getFechaTermino()).isEqualTo(UPDATED_FECHA_TERMINO);
    }

    @Test
    void patchNonExistingProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().collectList().block().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, proyectoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().collectList().block().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().collectList().block().size();
        proyecto.setId(count.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(proyectoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProyecto() {
        // Initialize the database
        proyectoRepository.save(proyecto).block();

        int databaseSizeBeforeDelete = proyectoRepository.findAll().collectList().block().size();

        // Delete the proyecto
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, proyecto.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Proyecto> proyectoList = proyectoRepository.findAll().collectList().block();
        assertThat(proyectoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
