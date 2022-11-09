package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.usco.ingenieria.software.pagina.modalidades.grado.IntegrationTest;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Arl;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ArlRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EntityManager;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ArlDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.ArlMapper;
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
 * Integration tests for the {@link ArlResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ArlResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/arls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArlRepository arlRepository;

    @Autowired
    private ArlMapper arlMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Arl arl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arl createEntity(EntityManager em) {
        Arl arl = new Arl().nombre(DEFAULT_NOMBRE);
        return arl;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arl createUpdatedEntity(EntityManager em) {
        Arl arl = new Arl().nombre(UPDATED_NOMBRE);
        return arl;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Arl.class).block();
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
        arl = createEntity(em);
    }

    @Test
    void createArl() throws Exception {
        int databaseSizeBeforeCreate = arlRepository.findAll().collectList().block().size();
        // Create the Arl
        ArlDTO arlDTO = arlMapper.toDto(arl);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arlDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeCreate + 1);
        Arl testArl = arlList.get(arlList.size() - 1);
        assertThat(testArl.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    void createArlWithExistingId() throws Exception {
        // Create the Arl with an existing ID
        arl.setId(1L);
        ArlDTO arlDTO = arlMapper.toDto(arl);

        int databaseSizeBeforeCreate = arlRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arlDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = arlRepository.findAll().collectList().block().size();
        // set the field null
        arl.setNombre(null);

        // Create the Arl, which fails.
        ArlDTO arlDTO = arlMapper.toDto(arl);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arlDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllArls() {
        // Initialize the database
        arlRepository.save(arl).block();

        // Get all the arlList
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
            .value(hasItem(arl.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE));
    }

    @Test
    void getArl() {
        // Initialize the database
        arlRepository.save(arl).block();

        // Get the arl
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, arl.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(arl.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE));
    }

    @Test
    void getNonExistingArl() {
        // Get the arl
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewArl() throws Exception {
        // Initialize the database
        arlRepository.save(arl).block();

        int databaseSizeBeforeUpdate = arlRepository.findAll().collectList().block().size();

        // Update the arl
        Arl updatedArl = arlRepository.findById(arl.getId()).block();
        updatedArl.nombre(UPDATED_NOMBRE);
        ArlDTO arlDTO = arlMapper.toDto(updatedArl);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, arlDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arlDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeUpdate);
        Arl testArl = arlList.get(arlList.size() - 1);
        assertThat(testArl.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    void putNonExistingArl() throws Exception {
        int databaseSizeBeforeUpdate = arlRepository.findAll().collectList().block().size();
        arl.setId(count.incrementAndGet());

        // Create the Arl
        ArlDTO arlDTO = arlMapper.toDto(arl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, arlDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arlDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchArl() throws Exception {
        int databaseSizeBeforeUpdate = arlRepository.findAll().collectList().block().size();
        arl.setId(count.incrementAndGet());

        // Create the Arl
        ArlDTO arlDTO = arlMapper.toDto(arl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arlDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamArl() throws Exception {
        int databaseSizeBeforeUpdate = arlRepository.findAll().collectList().block().size();
        arl.setId(count.incrementAndGet());

        // Create the Arl
        ArlDTO arlDTO = arlMapper.toDto(arl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(arlDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateArlWithPatch() throws Exception {
        // Initialize the database
        arlRepository.save(arl).block();

        int databaseSizeBeforeUpdate = arlRepository.findAll().collectList().block().size();

        // Update the arl using partial update
        Arl partialUpdatedArl = new Arl();
        partialUpdatedArl.setId(arl.getId());

        partialUpdatedArl.nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedArl.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedArl))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeUpdate);
        Arl testArl = arlList.get(arlList.size() - 1);
        assertThat(testArl.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    void fullUpdateArlWithPatch() throws Exception {
        // Initialize the database
        arlRepository.save(arl).block();

        int databaseSizeBeforeUpdate = arlRepository.findAll().collectList().block().size();

        // Update the arl using partial update
        Arl partialUpdatedArl = new Arl();
        partialUpdatedArl.setId(arl.getId());

        partialUpdatedArl.nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedArl.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedArl))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeUpdate);
        Arl testArl = arlList.get(arlList.size() - 1);
        assertThat(testArl.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    void patchNonExistingArl() throws Exception {
        int databaseSizeBeforeUpdate = arlRepository.findAll().collectList().block().size();
        arl.setId(count.incrementAndGet());

        // Create the Arl
        ArlDTO arlDTO = arlMapper.toDto(arl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, arlDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(arlDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchArl() throws Exception {
        int databaseSizeBeforeUpdate = arlRepository.findAll().collectList().block().size();
        arl.setId(count.incrementAndGet());

        // Create the Arl
        ArlDTO arlDTO = arlMapper.toDto(arl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(arlDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamArl() throws Exception {
        int databaseSizeBeforeUpdate = arlRepository.findAll().collectList().block().size();
        arl.setId(count.incrementAndGet());

        // Create the Arl
        ArlDTO arlDTO = arlMapper.toDto(arl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(arlDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Arl in the database
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteArl() {
        // Initialize the database
        arlRepository.save(arl).block();

        int databaseSizeBeforeDelete = arlRepository.findAll().collectList().block().size();

        // Delete the arl
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, arl.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Arl> arlList = arlRepository.findAll().collectList().block();
        assertThat(arlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
