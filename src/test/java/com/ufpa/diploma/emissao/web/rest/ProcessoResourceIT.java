package com.ufpa.diploma.emissao.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ufpa.diploma.emissao.IntegrationTest;
import com.ufpa.diploma.emissao.domain.Processo;
import com.ufpa.diploma.emissao.domain.enumeration.Enviado;
import com.ufpa.diploma.emissao.domain.enumeration.Status;
import com.ufpa.diploma.emissao.domain.enumeration.StatusProcesso;
import com.ufpa.diploma.emissao.repository.ProcessoRepository;
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

/**
 * Integration tests for the {@link ProcessoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProcessoResourceIT {

    private static final StatusProcesso DEFAULT_STATUS_PROCESSO = StatusProcesso.PENDENTE;
    private static final StatusProcesso UPDATED_STATUS_PROCESSO = StatusProcesso.CONCLUIDO;

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NUMERO_DA_DEFESA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_DA_DEFESA = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS_SIGAA = Status.ATIVO;
    private static final Status UPDATED_STATUS_SIGAA = Status.FORMANDO;

    private static final String DEFAULT_NUMERO_SIPAC = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_SIPAC = "BBBBBBBBBB";

    private static final Enviado DEFAULT_ENVIADO_BIBLIOTECA = Enviado.SIM;
    private static final Enviado UPDATED_ENVIADO_BIBLIOTECA = Enviado.NAO;

    private static final String ENTITY_API_URL = "/api/processos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessoRepository processoRepository;

    @Mock
    private ProcessoRepository processoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessoMockMvc;

    private Processo processo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processo createEntity(EntityManager em) {
        Processo processo = new Processo()
            .statusProcesso(DEFAULT_STATUS_PROCESSO)
            .matricula(DEFAULT_MATRICULA)
            .nome(DEFAULT_NOME)
            .data(DEFAULT_DATA)
            .numeroDaDefesa(DEFAULT_NUMERO_DA_DEFESA)
            .statusSigaa(DEFAULT_STATUS_SIGAA)
            .numeroSipac(DEFAULT_NUMERO_SIPAC)
            .enviadoBiblioteca(DEFAULT_ENVIADO_BIBLIOTECA);
        return processo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processo createUpdatedEntity(EntityManager em) {
        Processo processo = new Processo()
            .statusProcesso(UPDATED_STATUS_PROCESSO)
            .matricula(UPDATED_MATRICULA)
            .nome(UPDATED_NOME)
            .data(UPDATED_DATA)
            .numeroDaDefesa(UPDATED_NUMERO_DA_DEFESA)
            .statusSigaa(UPDATED_STATUS_SIGAA)
            .numeroSipac(UPDATED_NUMERO_SIPAC)
            .enviadoBiblioteca(UPDATED_ENVIADO_BIBLIOTECA);
        return processo;
    }

    @BeforeEach
    public void initTest() {
        processo = createEntity(em);
    }

    @Test
    @Transactional
    void createProcesso() throws Exception {
        int databaseSizeBeforeCreate = processoRepository.findAll().size();
        // Create the Processo
        restProcessoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processo)))
            .andExpect(status().isCreated());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeCreate + 1);
        Processo testProcesso = processoList.get(processoList.size() - 1);
        assertThat(testProcesso.getStatusProcesso()).isEqualTo(DEFAULT_STATUS_PROCESSO);
        assertThat(testProcesso.getMatricula()).isEqualTo(DEFAULT_MATRICULA);
        assertThat(testProcesso.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testProcesso.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testProcesso.getNumeroDaDefesa()).isEqualTo(DEFAULT_NUMERO_DA_DEFESA);
        assertThat(testProcesso.getStatusSigaa()).isEqualTo(DEFAULT_STATUS_SIGAA);
        assertThat(testProcesso.getNumeroSipac()).isEqualTo(DEFAULT_NUMERO_SIPAC);
        assertThat(testProcesso.getEnviadoBiblioteca()).isEqualTo(DEFAULT_ENVIADO_BIBLIOTECA);
    }

    @Test
    @Transactional
    void createProcessoWithExistingId() throws Exception {
        // Create the Processo with an existing ID
        processo.setId(1L);

        int databaseSizeBeforeCreate = processoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processo)))
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculaIsRequired() throws Exception {
        int databaseSizeBeforeTest = processoRepository.findAll().size();
        // set the field null
        processo.setMatricula(null);

        // Create the Processo, which fails.

        restProcessoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processo)))
            .andExpect(status().isBadRequest());

        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProcessos() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList
        restProcessoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processo.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusProcesso").value(hasItem(DEFAULT_STATUS_PROCESSO.toString())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].numeroDaDefesa").value(hasItem(DEFAULT_NUMERO_DA_DEFESA)))
            .andExpect(jsonPath("$.[*].statusSigaa").value(hasItem(DEFAULT_STATUS_SIGAA.toString())))
            .andExpect(jsonPath("$.[*].numeroSipac").value(hasItem(DEFAULT_NUMERO_SIPAC)))
            .andExpect(jsonPath("$.[*].enviadoBiblioteca").value(hasItem(DEFAULT_ENVIADO_BIBLIOTECA.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProcessosWithEagerRelationshipsIsEnabled() throws Exception {
        when(processoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProcessoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(processoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProcessosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(processoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProcessoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(processoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProcesso() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get the processo
        restProcessoMockMvc
            .perform(get(ENTITY_API_URL_ID, processo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processo.getId().intValue()))
            .andExpect(jsonPath("$.statusProcesso").value(DEFAULT_STATUS_PROCESSO.toString()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.numeroDaDefesa").value(DEFAULT_NUMERO_DA_DEFESA))
            .andExpect(jsonPath("$.statusSigaa").value(DEFAULT_STATUS_SIGAA.toString()))
            .andExpect(jsonPath("$.numeroSipac").value(DEFAULT_NUMERO_SIPAC))
            .andExpect(jsonPath("$.enviadoBiblioteca").value(DEFAULT_ENVIADO_BIBLIOTECA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProcesso() throws Exception {
        // Get the processo
        restProcessoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcesso() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        int databaseSizeBeforeUpdate = processoRepository.findAll().size();

        // Update the processo
        Processo updatedProcesso = processoRepository.findById(processo.getId()).get();
        // Disconnect from session so that the updates on updatedProcesso are not directly saved in db
        em.detach(updatedProcesso);
        updatedProcesso
            .statusProcesso(UPDATED_STATUS_PROCESSO)
            .matricula(UPDATED_MATRICULA)
            .nome(UPDATED_NOME)
            .data(UPDATED_DATA)
            .numeroDaDefesa(UPDATED_NUMERO_DA_DEFESA)
            .statusSigaa(UPDATED_STATUS_SIGAA)
            .numeroSipac(UPDATED_NUMERO_SIPAC)
            .enviadoBiblioteca(UPDATED_ENVIADO_BIBLIOTECA);

        restProcessoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProcesso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProcesso))
            )
            .andExpect(status().isOk());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
        Processo testProcesso = processoList.get(processoList.size() - 1);
        assertThat(testProcesso.getStatusProcesso()).isEqualTo(UPDATED_STATUS_PROCESSO);
        assertThat(testProcesso.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testProcesso.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProcesso.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testProcesso.getNumeroDaDefesa()).isEqualTo(UPDATED_NUMERO_DA_DEFESA);
        assertThat(testProcesso.getStatusSigaa()).isEqualTo(UPDATED_STATUS_SIGAA);
        assertThat(testProcesso.getNumeroSipac()).isEqualTo(UPDATED_NUMERO_SIPAC);
        assertThat(testProcesso.getEnviadoBiblioteca()).isEqualTo(UPDATED_ENVIADO_BIBLIOTECA);
    }

    @Test
    @Transactional
    void putNonExistingProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessoWithPatch() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        int databaseSizeBeforeUpdate = processoRepository.findAll().size();

        // Update the processo using partial update
        Processo partialUpdatedProcesso = new Processo();
        partialUpdatedProcesso.setId(processo.getId());

        partialUpdatedProcesso.data(UPDATED_DATA).numeroDaDefesa(UPDATED_NUMERO_DA_DEFESA).statusSigaa(UPDATED_STATUS_SIGAA);

        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcesso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcesso))
            )
            .andExpect(status().isOk());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
        Processo testProcesso = processoList.get(processoList.size() - 1);
        assertThat(testProcesso.getStatusProcesso()).isEqualTo(DEFAULT_STATUS_PROCESSO);
        assertThat(testProcesso.getMatricula()).isEqualTo(DEFAULT_MATRICULA);
        assertThat(testProcesso.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testProcesso.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testProcesso.getNumeroDaDefesa()).isEqualTo(UPDATED_NUMERO_DA_DEFESA);
        assertThat(testProcesso.getStatusSigaa()).isEqualTo(UPDATED_STATUS_SIGAA);
        assertThat(testProcesso.getNumeroSipac()).isEqualTo(DEFAULT_NUMERO_SIPAC);
        assertThat(testProcesso.getEnviadoBiblioteca()).isEqualTo(DEFAULT_ENVIADO_BIBLIOTECA);
    }

    @Test
    @Transactional
    void fullUpdateProcessoWithPatch() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        int databaseSizeBeforeUpdate = processoRepository.findAll().size();

        // Update the processo using partial update
        Processo partialUpdatedProcesso = new Processo();
        partialUpdatedProcesso.setId(processo.getId());

        partialUpdatedProcesso
            .statusProcesso(UPDATED_STATUS_PROCESSO)
            .matricula(UPDATED_MATRICULA)
            .nome(UPDATED_NOME)
            .data(UPDATED_DATA)
            .numeroDaDefesa(UPDATED_NUMERO_DA_DEFESA)
            .statusSigaa(UPDATED_STATUS_SIGAA)
            .numeroSipac(UPDATED_NUMERO_SIPAC)
            .enviadoBiblioteca(UPDATED_ENVIADO_BIBLIOTECA);

        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcesso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcesso))
            )
            .andExpect(status().isOk());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
        Processo testProcesso = processoList.get(processoList.size() - 1);
        assertThat(testProcesso.getStatusProcesso()).isEqualTo(UPDATED_STATUS_PROCESSO);
        assertThat(testProcesso.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testProcesso.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProcesso.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testProcesso.getNumeroDaDefesa()).isEqualTo(UPDATED_NUMERO_DA_DEFESA);
        assertThat(testProcesso.getStatusSigaa()).isEqualTo(UPDATED_STATUS_SIGAA);
        assertThat(testProcesso.getNumeroSipac()).isEqualTo(UPDATED_NUMERO_SIPAC);
        assertThat(testProcesso.getEnviadoBiblioteca()).isEqualTo(UPDATED_ENVIADO_BIBLIOTECA);
    }

    @Test
    @Transactional
    void patchNonExistingProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(processo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcesso() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        int databaseSizeBeforeDelete = processoRepository.findAll().size();

        // Delete the processo
        restProcessoMockMvc
            .perform(delete(ENTITY_API_URL_ID, processo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
