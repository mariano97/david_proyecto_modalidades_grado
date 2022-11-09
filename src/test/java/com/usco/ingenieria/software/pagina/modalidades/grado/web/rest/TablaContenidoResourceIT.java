package com.usco.ingenieria.software.pagina.modalidades.grado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.usco.ingenieria.software.pagina.modalidades.grado.IntegrationTest;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaMaestra;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EntityManager;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.TablaContenidoRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.TablaContenidoService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaContenidoDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.TablaContenidoMapper;
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
 * Integration tests for the {@link TablaContenidoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TablaContenidoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tabla-contenidos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TablaContenidoRepository tablaContenidoRepository;

    @Mock
    private TablaContenidoRepository tablaContenidoRepositoryMock;

    @Autowired
    private TablaContenidoMapper tablaContenidoMapper;

    @Mock
    private TablaContenidoService tablaContenidoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TablaContenido tablaContenido;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TablaContenido createEntity(EntityManager em) {
        TablaContenido tablaContenido = new TablaContenido().nombre(DEFAULT_NOMBRE).codigo(DEFAULT_CODIGO);
        // Add required entity
        TablaMaestra tablaMaestra;
        tablaMaestra = em.insert(TablaMaestraResourceIT.createEntity(em)).block();
        tablaContenido.setTablaMaestra(tablaMaestra);
        return tablaContenido;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TablaContenido createUpdatedEntity(EntityManager em) {
        TablaContenido tablaContenido = new TablaContenido().nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);
        // Add required entity
        TablaMaestra tablaMaestra;
        tablaMaestra = em.insert(TablaMaestraResourceIT.createUpdatedEntity(em)).block();
        tablaContenido.setTablaMaestra(tablaMaestra);
        return tablaContenido;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TablaContenido.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        TablaMaestraResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        tablaContenido = createEntity(em);
    }

    @Test
    void createTablaContenido() throws Exception {
        int databaseSizeBeforeCreate = tablaContenidoRepository.findAll().collectList().block().size();
        // Create the TablaContenido
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(tablaContenido);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeCreate + 1);
        TablaContenido testTablaContenido = tablaContenidoList.get(tablaContenidoList.size() - 1);
        assertThat(testTablaContenido.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTablaContenido.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    void createTablaContenidoWithExistingId() throws Exception {
        // Create the TablaContenido with an existing ID
        tablaContenido.setId(1L);
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(tablaContenido);

        int databaseSizeBeforeCreate = tablaContenidoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tablaContenidoRepository.findAll().collectList().block().size();
        // set the field null
        tablaContenido.setNombre(null);

        // Create the TablaContenido, which fails.
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(tablaContenido);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tablaContenidoRepository.findAll().collectList().block().size();
        // set the field null
        tablaContenido.setCodigo(null);

        // Create the TablaContenido, which fails.
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(tablaContenido);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTablaContenidos() {
        // Initialize the database
        tablaContenidoRepository.save(tablaContenido).block();

        // Get all the tablaContenidoList
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
            .value(hasItem(tablaContenido.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTablaContenidosWithEagerRelationshipsIsEnabled() {
        when(tablaContenidoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(tablaContenidoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTablaContenidosWithEagerRelationshipsIsNotEnabled() {
        when(tablaContenidoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(tablaContenidoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getTablaContenido() {
        // Initialize the database
        tablaContenidoRepository.save(tablaContenido).block();

        // Get the tablaContenido
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tablaContenido.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tablaContenido.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO));
    }

    @Test
    void getNonExistingTablaContenido() {
        // Get the tablaContenido
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTablaContenido() throws Exception {
        // Initialize the database
        tablaContenidoRepository.save(tablaContenido).block();

        int databaseSizeBeforeUpdate = tablaContenidoRepository.findAll().collectList().block().size();

        // Update the tablaContenido
        TablaContenido updatedTablaContenido = tablaContenidoRepository.findById(tablaContenido.getId()).block();
        updatedTablaContenido.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(updatedTablaContenido);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tablaContenidoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeUpdate);
        TablaContenido testTablaContenido = tablaContenidoList.get(tablaContenidoList.size() - 1);
        assertThat(testTablaContenido.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTablaContenido.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void putNonExistingTablaContenido() throws Exception {
        int databaseSizeBeforeUpdate = tablaContenidoRepository.findAll().collectList().block().size();
        tablaContenido.setId(count.incrementAndGet());

        // Create the TablaContenido
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(tablaContenido);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tablaContenidoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTablaContenido() throws Exception {
        int databaseSizeBeforeUpdate = tablaContenidoRepository.findAll().collectList().block().size();
        tablaContenido.setId(count.incrementAndGet());

        // Create the TablaContenido
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(tablaContenido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTablaContenido() throws Exception {
        int databaseSizeBeforeUpdate = tablaContenidoRepository.findAll().collectList().block().size();
        tablaContenido.setId(count.incrementAndGet());

        // Create the TablaContenido
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(tablaContenido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTablaContenidoWithPatch() throws Exception {
        // Initialize the database
        tablaContenidoRepository.save(tablaContenido).block();

        int databaseSizeBeforeUpdate = tablaContenidoRepository.findAll().collectList().block().size();

        // Update the tablaContenido using partial update
        TablaContenido partialUpdatedTablaContenido = new TablaContenido();
        partialUpdatedTablaContenido.setId(tablaContenido.getId());

        partialUpdatedTablaContenido.codigo(UPDATED_CODIGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTablaContenido.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTablaContenido))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeUpdate);
        TablaContenido testTablaContenido = tablaContenidoList.get(tablaContenidoList.size() - 1);
        assertThat(testTablaContenido.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTablaContenido.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void fullUpdateTablaContenidoWithPatch() throws Exception {
        // Initialize the database
        tablaContenidoRepository.save(tablaContenido).block();

        int databaseSizeBeforeUpdate = tablaContenidoRepository.findAll().collectList().block().size();

        // Update the tablaContenido using partial update
        TablaContenido partialUpdatedTablaContenido = new TablaContenido();
        partialUpdatedTablaContenido.setId(tablaContenido.getId());

        partialUpdatedTablaContenido.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTablaContenido.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTablaContenido))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeUpdate);
        TablaContenido testTablaContenido = tablaContenidoList.get(tablaContenidoList.size() - 1);
        assertThat(testTablaContenido.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTablaContenido.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void patchNonExistingTablaContenido() throws Exception {
        int databaseSizeBeforeUpdate = tablaContenidoRepository.findAll().collectList().block().size();
        tablaContenido.setId(count.incrementAndGet());

        // Create the TablaContenido
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(tablaContenido);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tablaContenidoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTablaContenido() throws Exception {
        int databaseSizeBeforeUpdate = tablaContenidoRepository.findAll().collectList().block().size();
        tablaContenido.setId(count.incrementAndGet());

        // Create the TablaContenido
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(tablaContenido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTablaContenido() throws Exception {
        int databaseSizeBeforeUpdate = tablaContenidoRepository.findAll().collectList().block().size();
        tablaContenido.setId(count.incrementAndGet());

        // Create the TablaContenido
        TablaContenidoDTO tablaContenidoDTO = tablaContenidoMapper.toDto(tablaContenido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaContenidoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TablaContenido in the database
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTablaContenido() {
        // Initialize the database
        tablaContenidoRepository.save(tablaContenido).block();

        int databaseSizeBeforeDelete = tablaContenidoRepository.findAll().collectList().block().size();

        // Delete the tablaContenido
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tablaContenido.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TablaContenido> tablaContenidoList = tablaContenidoRepository.findAll().collectList().block();
        assertThat(tablaContenidoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
