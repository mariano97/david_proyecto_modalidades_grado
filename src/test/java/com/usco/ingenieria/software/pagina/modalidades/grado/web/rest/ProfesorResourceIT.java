package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.usco.ingenieria.software.pagina.modalidades.grado.IntegrationTest;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Profesor;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EntityManager;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ProfesorRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ProfesorDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.ProfesorMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ProfesorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProfesorResourceIT {

    private static final String DEFAULT_PRIMER_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/profesors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private ProfesorMapper profesorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Profesor profesor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profesor createEntity(EntityManager em) {
        Profesor profesor = new Profesor()
            .primerNombre(DEFAULT_PRIMER_NOMBRE)
            .segundoNombre(DEFAULT_SEGUNDO_NOMBRE)
            .apellidos(DEFAULT_APELLIDOS)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO);
        return profesor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profesor createUpdatedEntity(EntityManager em) {
        Profesor profesor = new Profesor()
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO);
        return profesor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Profesor.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        profesor = createEntity(em);
    }

    @Test
    void createProfesor() throws Exception {
        int databaseSizeBeforeCreate = profesorRepository.findAll().collectList().block().size();
        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeCreate + 1);
        Profesor testProfesor = profesorList.get(profesorList.size() - 1);
        assertThat(testProfesor.getPrimerNombre()).isEqualTo(DEFAULT_PRIMER_NOMBRE);
        assertThat(testProfesor.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testProfesor.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testProfesor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfesor.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
    }

    @Test
    void createProfesorWithExistingId() throws Exception {
        // Create the Profesor with an existing ID
        profesor.setId(1L);
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        int databaseSizeBeforeCreate = profesorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPrimerNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = profesorRepository.findAll().collectList().block().size();
        // set the field null
        profesor.setPrimerNombre(null);

        // Create the Profesor, which fails.
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkApellidosIsRequired() throws Exception {
        int databaseSizeBeforeTest = profesorRepository.findAll().collectList().block().size();
        // set the field null
        profesor.setApellidos(null);

        // Create the Profesor, which fails.
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = profesorRepository.findAll().collectList().block().size();
        // set the field null
        profesor.setEmail(null);

        // Create the Profesor, which fails.
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProfesors() {
        // Initialize the database
        profesorRepository.save(profesor).block();

        // Get all the profesorList
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
            .value(hasItem(profesor.getId().intValue()))
            .jsonPath("$.[*].primerNombre")
            .value(hasItem(DEFAULT_PRIMER_NOMBRE))
            .jsonPath("$.[*].segundoNombre")
            .value(hasItem(DEFAULT_SEGUNDO_NOMBRE))
            .jsonPath("$.[*].apellidos")
            .value(hasItem(DEFAULT_APELLIDOS))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].telefono")
            .value(hasItem(DEFAULT_TELEFONO));
    }

    @Test
    void getProfesor() {
        // Initialize the database
        profesorRepository.save(profesor).block();

        // Get the profesor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, profesor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(profesor.getId().intValue()))
            .jsonPath("$.primerNombre")
            .value(is(DEFAULT_PRIMER_NOMBRE))
            .jsonPath("$.segundoNombre")
            .value(is(DEFAULT_SEGUNDO_NOMBRE))
            .jsonPath("$.apellidos")
            .value(is(DEFAULT_APELLIDOS))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.telefono")
            .value(is(DEFAULT_TELEFONO));
    }

    @Test
    void getNonExistingProfesor() {
        // Get the profesor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProfesor() throws Exception {
        // Initialize the database
        profesorRepository.save(profesor).block();

        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();

        // Update the profesor
        Profesor updatedProfesor = profesorRepository.findById(profesor.getId()).block();
        updatedProfesor
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO);
        ProfesorDTO profesorDTO = profesorMapper.toDto(updatedProfesor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, profesorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
        Profesor testProfesor = profesorList.get(profesorList.size() - 1);
        assertThat(testProfesor.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testProfesor.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testProfesor.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testProfesor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfesor.getTelefono()).isEqualTo(UPDATED_TELEFONO);
    }

    @Test
    void putNonExistingProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, profesorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProfesorWithPatch() throws Exception {
        // Initialize the database
        profesorRepository.save(profesor).block();

        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();

        // Update the profesor using partial update
        Profesor partialUpdatedProfesor = new Profesor();
        partialUpdatedProfesor.setId(profesor.getId());

        partialUpdatedProfesor
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProfesor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProfesor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
        Profesor testProfesor = profesorList.get(profesorList.size() - 1);
        assertThat(testProfesor.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testProfesor.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testProfesor.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testProfesor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfesor.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
    }

    @Test
    void fullUpdateProfesorWithPatch() throws Exception {
        // Initialize the database
        profesorRepository.save(profesor).block();

        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();

        // Update the profesor using partial update
        Profesor partialUpdatedProfesor = new Profesor();
        partialUpdatedProfesor.setId(profesor.getId());

        partialUpdatedProfesor
            .primerNombre(UPDATED_PRIMER_NOMBRE)
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProfesor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProfesor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
        Profesor testProfesor = profesorList.get(profesorList.size() - 1);
        assertThat(testProfesor.getPrimerNombre()).isEqualTo(UPDATED_PRIMER_NOMBRE);
        assertThat(testProfesor.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testProfesor.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testProfesor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfesor.getTelefono()).isEqualTo(UPDATED_TELEFONO);
    }

    @Test
    void patchNonExistingProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, profesorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProfesor() {
        // Initialize the database
        profesorRepository.save(profesor).block();

        int databaseSizeBeforeDelete = profesorRepository.findAll().collectList().block().size();

        // Delete the profesor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, profesor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
