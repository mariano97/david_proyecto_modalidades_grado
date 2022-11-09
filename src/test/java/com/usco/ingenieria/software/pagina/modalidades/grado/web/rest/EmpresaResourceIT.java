package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.usco.ingenieria.software.pagina.modalidades.grado.IntegrationTest;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Empresa;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EmpresaRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EntityManager;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.EmpresaDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.EmpresaMapper;
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
 * Integration tests for the {@link EmpresaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmpresaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_NIT = "AAAAAAAAAA";
    private static final String UPDATED_NIT = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/empresas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaMapper empresaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Empresa empresa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createEntity(EntityManager em) {
        Empresa empresa = new Empresa().nombre(DEFAULT_NOMBRE).nit(DEFAULT_NIT).telefono(DEFAULT_TELEFONO).email(DEFAULT_EMAIL);
        return empresa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createUpdatedEntity(EntityManager em) {
        Empresa empresa = new Empresa().nombre(UPDATED_NOMBRE).nit(UPDATED_NIT).telefono(UPDATED_TELEFONO).email(UPDATED_EMAIL);
        return empresa;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Empresa.class).block();
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
        empresa = createEntity(em);
    }

    @Test
    void createEmpresa() throws Exception {
        int databaseSizeBeforeCreate = empresaRepository.findAll().collectList().block().size();
        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate + 1);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEmpresa.getNit()).isEqualTo(DEFAULT_NIT);
        assertThat(testEmpresa.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testEmpresa.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void createEmpresaWithExistingId() throws Exception {
        // Create the Empresa with an existing ID
        empresa.setId(1L);
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        int databaseSizeBeforeCreate = empresaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = empresaRepository.findAll().collectList().block().size();
        // set the field null
        empresa.setNombre(null);

        // Create the Empresa, which fails.
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNitIsRequired() throws Exception {
        int databaseSizeBeforeTest = empresaRepository.findAll().collectList().block().size();
        // set the field null
        empresa.setNit(null);

        // Create the Empresa, which fails.
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = empresaRepository.findAll().collectList().block().size();
        // set the field null
        empresa.setEmail(null);

        // Create the Empresa, which fails.
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEmpresas() {
        // Initialize the database
        empresaRepository.save(empresa).block();

        // Get all the empresaList
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
            .value(hasItem(empresa.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].nit")
            .value(hasItem(DEFAULT_NIT))
            .jsonPath("$.[*].telefono")
            .value(hasItem(DEFAULT_TELEFONO))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL));
    }

    @Test
    void getEmpresa() {
        // Initialize the database
        empresaRepository.save(empresa).block();

        // Get the empresa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, empresa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(empresa.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.nit")
            .value(is(DEFAULT_NIT))
            .jsonPath("$.telefono")
            .value(is(DEFAULT_TELEFONO))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL));
    }

    @Test
    void getNonExistingEmpresa() {
        // Get the empresa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.save(empresa).block();

        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();

        // Update the empresa
        Empresa updatedEmpresa = empresaRepository.findById(empresa.getId()).block();
        updatedEmpresa.nombre(UPDATED_NOMBRE).nit(UPDATED_NIT).telefono(UPDATED_TELEFONO).email(UPDATED_EMAIL);
        EmpresaDTO empresaDTO = empresaMapper.toDto(updatedEmpresa);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, empresaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpresa.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testEmpresa.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEmpresa.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void putNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, empresaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        empresaRepository.save(empresa).block();

        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa.nombre(UPDATED_NOMBRE).nit(UPDATED_NIT).email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpresa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpresa.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testEmpresa.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testEmpresa.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void fullUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        empresaRepository.save(empresa).block();

        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa.nombre(UPDATED_NOMBRE).nit(UPDATED_NIT).telefono(UPDATED_TELEFONO).email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpresa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpresa.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testEmpresa.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEmpresa.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void patchNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, empresaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmpresa() {
        // Initialize the database
        empresaRepository.save(empresa).block();

        int databaseSizeBeforeDelete = empresaRepository.findAll().collectList().block().size();

        // Delete the empresa
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, empresa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
