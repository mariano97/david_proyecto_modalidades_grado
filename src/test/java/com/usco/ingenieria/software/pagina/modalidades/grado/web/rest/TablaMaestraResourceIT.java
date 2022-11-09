package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.usco.ingenieria.software.pagina.modalidades.grado.IntegrationTest;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaMaestra;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EntityManager;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.TablaMaestraRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaMaestraDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.TablaMaestraMapper;
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
 * Integration tests for the {@link TablaMaestraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TablaMaestraResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tabla-maestras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TablaMaestraRepository tablaMaestraRepository;

    @Autowired
    private TablaMaestraMapper tablaMaestraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TablaMaestra tablaMaestra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TablaMaestra createEntity(EntityManager em) {
        TablaMaestra tablaMaestra = new TablaMaestra().nombre(DEFAULT_NOMBRE).codigo(DEFAULT_CODIGO);
        return tablaMaestra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TablaMaestra createUpdatedEntity(EntityManager em) {
        TablaMaestra tablaMaestra = new TablaMaestra().nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);
        return tablaMaestra;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TablaMaestra.class).block();
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
        tablaMaestra = createEntity(em);
    }

    @Test
    void createTablaMaestra() throws Exception {
        int databaseSizeBeforeCreate = tablaMaestraRepository.findAll().collectList().block().size();
        // Create the TablaMaestra
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(tablaMaestra);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeCreate + 1);
        TablaMaestra testTablaMaestra = tablaMaestraList.get(tablaMaestraList.size() - 1);
        assertThat(testTablaMaestra.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTablaMaestra.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    void createTablaMaestraWithExistingId() throws Exception {
        // Create the TablaMaestra with an existing ID
        tablaMaestra.setId(1L);
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(tablaMaestra);

        int databaseSizeBeforeCreate = tablaMaestraRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tablaMaestraRepository.findAll().collectList().block().size();
        // set the field null
        tablaMaestra.setNombre(null);

        // Create the TablaMaestra, which fails.
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(tablaMaestra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tablaMaestraRepository.findAll().collectList().block().size();
        // set the field null
        tablaMaestra.setCodigo(null);

        // Create the TablaMaestra, which fails.
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(tablaMaestra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTablaMaestras() {
        // Initialize the database
        tablaMaestraRepository.save(tablaMaestra).block();

        // Get all the tablaMaestraList
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
            .value(hasItem(tablaMaestra.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO));
    }

    @Test
    void getTablaMaestra() {
        // Initialize the database
        tablaMaestraRepository.save(tablaMaestra).block();

        // Get the tablaMaestra
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tablaMaestra.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tablaMaestra.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO));
    }

    @Test
    void getNonExistingTablaMaestra() {
        // Get the tablaMaestra
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTablaMaestra() throws Exception {
        // Initialize the database
        tablaMaestraRepository.save(tablaMaestra).block();

        int databaseSizeBeforeUpdate = tablaMaestraRepository.findAll().collectList().block().size();

        // Update the tablaMaestra
        TablaMaestra updatedTablaMaestra = tablaMaestraRepository.findById(tablaMaestra.getId()).block();
        updatedTablaMaestra.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(updatedTablaMaestra);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tablaMaestraDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeUpdate);
        TablaMaestra testTablaMaestra = tablaMaestraList.get(tablaMaestraList.size() - 1);
        assertThat(testTablaMaestra.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTablaMaestra.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void putNonExistingTablaMaestra() throws Exception {
        int databaseSizeBeforeUpdate = tablaMaestraRepository.findAll().collectList().block().size();
        tablaMaestra.setId(count.incrementAndGet());

        // Create the TablaMaestra
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(tablaMaestra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tablaMaestraDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTablaMaestra() throws Exception {
        int databaseSizeBeforeUpdate = tablaMaestraRepository.findAll().collectList().block().size();
        tablaMaestra.setId(count.incrementAndGet());

        // Create the TablaMaestra
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(tablaMaestra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTablaMaestra() throws Exception {
        int databaseSizeBeforeUpdate = tablaMaestraRepository.findAll().collectList().block().size();
        tablaMaestra.setId(count.incrementAndGet());

        // Create the TablaMaestra
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(tablaMaestra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTablaMaestraWithPatch() throws Exception {
        // Initialize the database
        tablaMaestraRepository.save(tablaMaestra).block();

        int databaseSizeBeforeUpdate = tablaMaestraRepository.findAll().collectList().block().size();

        // Update the tablaMaestra using partial update
        TablaMaestra partialUpdatedTablaMaestra = new TablaMaestra();
        partialUpdatedTablaMaestra.setId(tablaMaestra.getId());

        partialUpdatedTablaMaestra.nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTablaMaestra.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTablaMaestra))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeUpdate);
        TablaMaestra testTablaMaestra = tablaMaestraList.get(tablaMaestraList.size() - 1);
        assertThat(testTablaMaestra.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTablaMaestra.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    void fullUpdateTablaMaestraWithPatch() throws Exception {
        // Initialize the database
        tablaMaestraRepository.save(tablaMaestra).block();

        int databaseSizeBeforeUpdate = tablaMaestraRepository.findAll().collectList().block().size();

        // Update the tablaMaestra using partial update
        TablaMaestra partialUpdatedTablaMaestra = new TablaMaestra();
        partialUpdatedTablaMaestra.setId(tablaMaestra.getId());

        partialUpdatedTablaMaestra.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTablaMaestra.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTablaMaestra))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeUpdate);
        TablaMaestra testTablaMaestra = tablaMaestraList.get(tablaMaestraList.size() - 1);
        assertThat(testTablaMaestra.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTablaMaestra.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void patchNonExistingTablaMaestra() throws Exception {
        int databaseSizeBeforeUpdate = tablaMaestraRepository.findAll().collectList().block().size();
        tablaMaestra.setId(count.incrementAndGet());

        // Create the TablaMaestra
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(tablaMaestra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tablaMaestraDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTablaMaestra() throws Exception {
        int databaseSizeBeforeUpdate = tablaMaestraRepository.findAll().collectList().block().size();
        tablaMaestra.setId(count.incrementAndGet());

        // Create the TablaMaestra
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(tablaMaestra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTablaMaestra() throws Exception {
        int databaseSizeBeforeUpdate = tablaMaestraRepository.findAll().collectList().block().size();
        tablaMaestra.setId(count.incrementAndGet());

        // Create the TablaMaestra
        TablaMaestraDTO tablaMaestraDTO = tablaMaestraMapper.toDto(tablaMaestra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaMaestraDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TablaMaestra in the database
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTablaMaestra() {
        // Initialize the database
        tablaMaestraRepository.save(tablaMaestra).block();

        int databaseSizeBeforeDelete = tablaMaestraRepository.findAll().collectList().block().size();

        // Delete the tablaMaestra
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tablaMaestra.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TablaMaestra> tablaMaestraList = tablaMaestraRepository.findAll().collectList().block();
        assertThat(tablaMaestraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
