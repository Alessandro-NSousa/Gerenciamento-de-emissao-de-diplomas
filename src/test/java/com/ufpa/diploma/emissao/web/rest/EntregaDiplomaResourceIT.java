package com.ufpa.diploma.emissao.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ufpa.diploma.emissao.IntegrationTest;
import com.ufpa.diploma.emissao.domain.EntregaDiploma;
import com.ufpa.diploma.emissao.repository.EntregaDiplomaRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link EntregaDiplomaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EntregaDiplomaResourceIT {

    private static final LocalDate DEFAULT_DATA_DE_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DE_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entrega-diplomas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EntregaDiplomaRepository entregaDiplomaRepository;

    @Mock
    private EntregaDiplomaRepository entregaDiplomaRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntregaDiplomaMockMvc;

    private EntregaDiploma entregaDiploma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntregaDiploma createEntity(EntityManager em) {
        EntregaDiploma entregaDiploma = new EntregaDiploma().dataDeEntrega(DEFAULT_DATA_DE_ENTREGA).observacoes(DEFAULT_OBSERVACOES);
        return entregaDiploma;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntregaDiploma createUpdatedEntity(EntityManager em) {
        EntregaDiploma entregaDiploma = new EntregaDiploma().dataDeEntrega(UPDATED_DATA_DE_ENTREGA).observacoes(UPDATED_OBSERVACOES);
        return entregaDiploma;
    }

    @BeforeEach
    public void initTest() {
        entregaDiploma = createEntity(em);
    }

    @Test
    @Transactional
    void createEntregaDiploma() throws Exception {
        int databaseSizeBeforeCreate = entregaDiplomaRepository.findAll().size();
        // Create the EntregaDiploma
        restEntregaDiplomaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entregaDiploma))
            )
            .andExpect(status().isCreated());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeCreate + 1);
        EntregaDiploma testEntregaDiploma = entregaDiplomaList.get(entregaDiplomaList.size() - 1);
        assertThat(testEntregaDiploma.getDataDeEntrega()).isEqualTo(DEFAULT_DATA_DE_ENTREGA);
        assertThat(testEntregaDiploma.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);
    }

    @Test
    @Transactional
    void createEntregaDiplomaWithExistingId() throws Exception {
        // Create the EntregaDiploma with an existing ID
        entregaDiploma.setId(1L);

        int databaseSizeBeforeCreate = entregaDiplomaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntregaDiplomaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entregaDiploma))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEntregaDiplomas() throws Exception {
        // Initialize the database
        entregaDiplomaRepository.saveAndFlush(entregaDiploma);

        // Get all the entregaDiplomaList
        restEntregaDiplomaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entregaDiploma.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataDeEntrega").value(hasItem(DEFAULT_DATA_DE_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEntregaDiplomasWithEagerRelationshipsIsEnabled() throws Exception {
        when(entregaDiplomaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEntregaDiplomaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(entregaDiplomaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEntregaDiplomasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(entregaDiplomaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEntregaDiplomaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(entregaDiplomaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEntregaDiploma() throws Exception {
        // Initialize the database
        entregaDiplomaRepository.saveAndFlush(entregaDiploma);

        // Get the entregaDiploma
        restEntregaDiplomaMockMvc
            .perform(get(ENTITY_API_URL_ID, entregaDiploma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entregaDiploma.getId().intValue()))
            .andExpect(jsonPath("$.dataDeEntrega").value(DEFAULT_DATA_DE_ENTREGA.toString()))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEntregaDiploma() throws Exception {
        // Get the entregaDiploma
        restEntregaDiplomaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEntregaDiploma() throws Exception {
        // Initialize the database
        entregaDiplomaRepository.saveAndFlush(entregaDiploma);

        int databaseSizeBeforeUpdate = entregaDiplomaRepository.findAll().size();

        // Update the entregaDiploma
        EntregaDiploma updatedEntregaDiploma = entregaDiplomaRepository.findById(entregaDiploma.getId()).get();
        // Disconnect from session so that the updates on updatedEntregaDiploma are not directly saved in db
        em.detach(updatedEntregaDiploma);
        updatedEntregaDiploma.dataDeEntrega(UPDATED_DATA_DE_ENTREGA).observacoes(UPDATED_OBSERVACOES);

        restEntregaDiplomaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEntregaDiploma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEntregaDiploma))
            )
            .andExpect(status().isOk());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeUpdate);
        EntregaDiploma testEntregaDiploma = entregaDiplomaList.get(entregaDiplomaList.size() - 1);
        assertThat(testEntregaDiploma.getDataDeEntrega()).isEqualTo(UPDATED_DATA_DE_ENTREGA);
        assertThat(testEntregaDiploma.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void putNonExistingEntregaDiploma() throws Exception {
        int databaseSizeBeforeUpdate = entregaDiplomaRepository.findAll().size();
        entregaDiploma.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntregaDiplomaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entregaDiploma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entregaDiploma))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntregaDiploma() throws Exception {
        int databaseSizeBeforeUpdate = entregaDiplomaRepository.findAll().size();
        entregaDiploma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntregaDiplomaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entregaDiploma))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntregaDiploma() throws Exception {
        int databaseSizeBeforeUpdate = entregaDiplomaRepository.findAll().size();
        entregaDiploma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntregaDiplomaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entregaDiploma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntregaDiplomaWithPatch() throws Exception {
        // Initialize the database
        entregaDiplomaRepository.saveAndFlush(entregaDiploma);

        int databaseSizeBeforeUpdate = entregaDiplomaRepository.findAll().size();

        // Update the entregaDiploma using partial update
        EntregaDiploma partialUpdatedEntregaDiploma = new EntregaDiploma();
        partialUpdatedEntregaDiploma.setId(entregaDiploma.getId());

        partialUpdatedEntregaDiploma.dataDeEntrega(UPDATED_DATA_DE_ENTREGA);

        restEntregaDiplomaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntregaDiploma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntregaDiploma))
            )
            .andExpect(status().isOk());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeUpdate);
        EntregaDiploma testEntregaDiploma = entregaDiplomaList.get(entregaDiplomaList.size() - 1);
        assertThat(testEntregaDiploma.getDataDeEntrega()).isEqualTo(UPDATED_DATA_DE_ENTREGA);
        assertThat(testEntregaDiploma.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);
    }

    @Test
    @Transactional
    void fullUpdateEntregaDiplomaWithPatch() throws Exception {
        // Initialize the database
        entregaDiplomaRepository.saveAndFlush(entregaDiploma);

        int databaseSizeBeforeUpdate = entregaDiplomaRepository.findAll().size();

        // Update the entregaDiploma using partial update
        EntregaDiploma partialUpdatedEntregaDiploma = new EntregaDiploma();
        partialUpdatedEntregaDiploma.setId(entregaDiploma.getId());

        partialUpdatedEntregaDiploma.dataDeEntrega(UPDATED_DATA_DE_ENTREGA).observacoes(UPDATED_OBSERVACOES);

        restEntregaDiplomaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntregaDiploma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntregaDiploma))
            )
            .andExpect(status().isOk());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeUpdate);
        EntregaDiploma testEntregaDiploma = entregaDiplomaList.get(entregaDiplomaList.size() - 1);
        assertThat(testEntregaDiploma.getDataDeEntrega()).isEqualTo(UPDATED_DATA_DE_ENTREGA);
        assertThat(testEntregaDiploma.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void patchNonExistingEntregaDiploma() throws Exception {
        int databaseSizeBeforeUpdate = entregaDiplomaRepository.findAll().size();
        entregaDiploma.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntregaDiplomaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entregaDiploma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entregaDiploma))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntregaDiploma() throws Exception {
        int databaseSizeBeforeUpdate = entregaDiplomaRepository.findAll().size();
        entregaDiploma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntregaDiplomaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entregaDiploma))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntregaDiploma() throws Exception {
        int databaseSizeBeforeUpdate = entregaDiplomaRepository.findAll().size();
        entregaDiploma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntregaDiplomaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(entregaDiploma))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntregaDiploma in the database
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntregaDiploma() throws Exception {
        // Initialize the database
        entregaDiplomaRepository.saveAndFlush(entregaDiploma);

        int databaseSizeBeforeDelete = entregaDiplomaRepository.findAll().size();

        // Delete the entregaDiploma
        restEntregaDiplomaMockMvc
            .perform(delete(ENTITY_API_URL_ID, entregaDiploma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EntregaDiploma> entregaDiplomaList = entregaDiplomaRepository.findAll();
        assertThat(entregaDiplomaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
