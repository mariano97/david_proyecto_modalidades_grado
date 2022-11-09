package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.usco.ingenieria.software.pagina.modalidades.grado.IntegrationTest;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Estudiante;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EntityManager;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EstudianteRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.EstudianteService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.EstudianteDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.EstudianteMapper;
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
 * Integration tests for the {@link EstudianteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EstudianteResourceIT {

    private static final String DEFAULT_PRIMER_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_ESTUDIANTIL = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_ESTUDIANTIL = "BBBBBBBBBB";

    private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/estudiantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Mock
    private EstudianteRepository estudianteRepositoryMock;

    @Autowired
    private EstudianteMapper estudianteMapper;

    @Mock
    private EstudianteService estudianteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Estudiante estudiante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estudiante createEntity(EntityManager em) {
        Estudiante estudiante = new Estudiante()
            .primerNombre(DEFAULT_PRIMER_NOMBRE)
            .segundoNombre(DEFAULT_SEGUNDO_NOMBRE)
            .apellidos(DEFAULT_APELLIDOS)
            .numeroDocumento(DEFAULT_NUMERO_DOCUMENTO)
            .codigoEstudiantil(DEFAULT_CODIGO_ESTUDIANTIL)
            .celular(DEFAULT_CELULAR)
            .email(DEFAULT_EMAIL);
        // Add required entity
        TablaContenido tablaContenido;
        tablaContenido = em.insert(TablaContenidoResourceIT.createEntity(em)).block();
        estudiante.setTipoDocumento(tablaContenido);
        // Add required entity
        Proyecto proyecto;
        proyecto = em.insert(ProyectoResourceIT.createEntity(em)).block();
        estudiante.setProyecto(proyecto);
        return estudiante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estudiante createUpdatedEntity(EntityManager em) {
        Estudiante estudiante = new Estudiante()
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .codigoEstudiantil(UPDATED_CODIGO_ESTUDIANTIL)
            .celular(UPDATED_CELULAR)
            .email(UPDATED_EMAIL);
        // Add required entity
        TablaContenido tablaContenido;
        tablaContenido = em.insert(TablaContenidoResourceIT.createUpdatedEntity(em)).block();
        estudiante.setTipoDocumento(tablaContenido);
        // Add required entity
        Proyecto proyecto;
        proyecto = em.insert(ProyectoResourceIT.createUpdatedEntity(em)).block();
        estudiante.setProyecto(proyecto);
        return estudiante;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Estudiante.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        TablaContenidoResourceIT.deleteEntities(em);
        ProyectoResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        estudiante = createEntity(em);
    }

    @Test
    void createEstudiante() throws Exception {
        int databaseSizeBeforeCreate = estudianteRepository.findAll().collectList().block().size();
        // Create the Estudiante
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeCreate + 1);
        Estudiante testEstudiante = estudianteList.get(estudianteList.size() - 1);
        assertThat(testEstudiante.getPrimerNombre()).isEqualTo(DEFAULT_PRIMER_NOMBRE);
        assertThat(testEstudiante.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testEstudiante.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testEstudiante.getNumeroDocumento()).isEqualTo(DEFAULT_NUMERO_DOCUMENTO);
        assertThat(testEstudiante.getCodigoEstudiantil()).isEqualTo(DEFAULT_CODIGO_ESTUDIANTIL);
        assertThat(testEstudiante.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testEstudiante.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void createEstudianteWithExistingId() throws Exception {
        // Create the Estudiante with an existing ID
        estudiante.setId(1L);
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        int databaseSizeBeforeCreate = estudianteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPrimerNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = estudianteRepository.findAll().collectList().block().size();
        // set the field null
        estudiante.setPrimerNombre(null);

        // Create the Estudiante, which fails.
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkApellidosIsRequired() throws Exception {
        int databaseSizeBeforeTest = estudianteRepository.findAll().collectList().block().size();
        // set the field null
        estudiante.setApellidos(null);

        // Create the Estudiante, which fails.
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNumeroDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = estudianteRepository.findAll().collectList().block().size();
        // set the field null
        estudiante.setNumeroDocumento(null);

        // Create the Estudiante, which fails.
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoEstudiantilIsRequired() throws Exception {
        int databaseSizeBeforeTest = estudianteRepository.findAll().collectList().block().size();
        // set the field null
        estudiante.setCodigoEstudiantil(null);

        // Create the Estudiante, which fails.
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCelularIsRequired() throws Exception {
        int databaseSizeBeforeTest = estudianteRepository.findAll().collectList().block().size();
        // set the field null
        estudiante.setCelular(null);

        // Create the Estudiante, which fails.
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = estudianteRepository.findAll().collectList().block().size();
        // set the field null
        estudiante.setEmail(null);

        // Create the Estudiante, which fails.
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEstudiantes() {
        // Initialize the database
        estudianteRepository.save(estudiante).block();

        // Get all the estudianteList
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
            .value(hasItem(estudiante.getId().intValue()))
            .jsonPath("$.[*].primerNombre")
            .value(hasItem(DEFAULT_PRIMER_NOMBRE))
            .jsonPath("$.[*].segundoNombre")
            .value(hasItem(DEFAULT_SEGUNDO_NOMBRE))
            .jsonPath("$.[*].apellidos")
            .value(hasItem(DEFAULT_APELLIDOS))
            .jsonPath("$.[*].numeroDocumento")
            .value(hasItem(DEFAULT_NUMERO_DOCUMENTO))
            .jsonPath("$.[*].codigoEstudiantil")
            .value(hasItem(DEFAULT_CODIGO_ESTUDIANTIL))
            .jsonPath("$.[*].celular")
            .value(hasItem(DEFAULT_CELULAR))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEstudiantesWithEagerRelationshipsIsEnabled() {
        when(estudianteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(estudianteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEstudiantesWithEagerRelationshipsIsNotEnabled() {
        when(estudianteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(estudianteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getEstudiante() {
        // Initialize the database
        estudianteRepository.save(estudiante).block();

        // Get the estudiante
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, estudiante.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(estudiante.getId().intValue()))
            .jsonPath("$.primerNombre")
            .value(is(DEFAULT_PRIMER_NOMBRE))
            .jsonPath("$.segundoNombre")
            .value(is(DEFAULT_SEGUNDO_NOMBRE))
            .jsonPath("$.apellidos")
            .value(is(DEFAULT_APELLIDOS))
            .jsonPath("$.numeroDocumento")
            .value(is(DEFAULT_NUMERO_DOCUMENTO))
            .jsonPath("$.codigoEstudiantil")
            .value(is(DEFAULT_CODIGO_ESTUDIANTIL))
            .jsonPath("$.celular")
            .value(is(DEFAULT_CELULAR))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL));
    }

    @Test
    void getNonExistingEstudiante() {
        // Get the estudiante
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewEstudiante() throws Exception {
        // Initialize the database
        estudianteRepository.save(estudiante).block();

        int databaseSizeBeforeUpdate = estudianteRepository.findAll().collectList().block().size();

        // Update the estudiante
        Estudiante updatedEstudiante = estudianteRepository.findById(estudiante.getId()).block();
        updatedEstudiante
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .codigoEstudiantil(UPDATED_CODIGO_ESTUDIANTIL)
            .celular(UPDATED_CELULAR)
            .email(UPDATED_EMAIL);
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(updatedEstudiante);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, estudianteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
        Estudiante testEstudiante = estudianteList.get(estudianteList.size() - 1);
        assertThat(testEstudiante.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testEstudiante.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testEstudiante.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testEstudiante.getNumeroDocumento()).isEqualTo(UPDATED_NUMERO_DOCUMENTO);
        assertThat(testEstudiante.getCodigoEstudiantil()).isEqualTo(UPDATED_CODIGO_ESTUDIANTIL);
        assertThat(testEstudiante.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testEstudiante.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void putNonExistingEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().collectList().block().size();
        estudiante.setId(count.incrementAndGet());

        // Create the Estudiante
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, estudianteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().collectList().block().size();
        estudiante.setId(count.incrementAndGet());

        // Create the Estudiante
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().collectList().block().size();
        estudiante.setId(count.incrementAndGet());

        // Create the Estudiante
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEstudianteWithPatch() throws Exception {
        // Initialize the database
        estudianteRepository.save(estudiante).block();

        int databaseSizeBeforeUpdate = estudianteRepository.findAll().collectList().block().size();

        // Update the estudiante using partial update
        Estudiante partialUpdatedEstudiante = new Estudiante();
        partialUpdatedEstudiante.setId(estudiante.getId());

        partialUpdatedEstudiante
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .celular(UPDATED_CELULAR)
            .email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEstudiante.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEstudiante))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
        Estudiante testEstudiante = estudianteList.get(estudianteList.size() - 1);
        assertThat(testEstudiante.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testEstudiante.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testEstudiante.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testEstudiante.getNumeroDocumento()).isEqualTo(DEFAULT_NUMERO_DOCUMENTO);
        assertThat(testEstudiante.getCodigoEstudiantil()).isEqualTo(DEFAULT_CODIGO_ESTUDIANTIL);
        assertThat(testEstudiante.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testEstudiante.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void fullUpdateEstudianteWithPatch() throws Exception {
        // Initialize the database
        estudianteRepository.save(estudiante).block();

        int databaseSizeBeforeUpdate = estudianteRepository.findAll().collectList().block().size();

        // Update the estudiante using partial update
        Estudiante partialUpdatedEstudiante = new Estudiante();
        partialUpdatedEstudiante.setId(estudiante.getId());

        partialUpdatedEstudiante
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .codigoEstudiantil(UPDATED_CODIGO_ESTUDIANTIL)
            .celular(UPDATED_CELULAR)
            .email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEstudiante.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEstudiante))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
        Estudiante testEstudiante = estudianteList.get(estudianteList.size() - 1);
        assertThat(testEstudiante.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testEstudiante.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testEstudiante.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testEstudiante.getNumeroDocumento()).isEqualTo(UPDATED_NUMERO_DOCUMENTO);
        assertThat(testEstudiante.getCodigoEstudiantil()).isEqualTo(UPDATED_CODIGO_ESTUDIANTIL);
        assertThat(testEstudiante.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testEstudiante.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void patchNonExistingEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().collectList().block().size();
        estudiante.setId(count.incrementAndGet());

        // Create the Estudiante
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, estudianteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().collectList().block().size();
        estudiante.setId(count.incrementAndGet());

        // Create the Estudiante
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().collectList().block().size();
        estudiante.setId(count.incrementAndGet());

        // Create the Estudiante
        EstudianteDTO estudianteDTO = estudianteMapper.toDto(estudiante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(estudianteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEstudiante() {
        // Initialize the database
        estudianteRepository.save(estudiante).block();

        int databaseSizeBeforeDelete = estudianteRepository.findAll().collectList().block().size();

        // Delete the estudiante
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, estudiante.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Estudiante> estudianteList = estudianteRepository.findAll().collectList().block();
        assertThat(estudianteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
