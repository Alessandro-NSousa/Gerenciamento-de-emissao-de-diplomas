package com.ufpa.diploma.emissao.web.rest;

import com.ufpa.diploma.emissao.domain.Processo;
import com.ufpa.diploma.emissao.repository.ProcessoRepository;
import com.ufpa.diploma.emissao.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ufpa.diploma.emissao.domain.Processo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProcessoResource {

    private final Logger log = LoggerFactory.getLogger(ProcessoResource.class);

    private static final String ENTITY_NAME = "processo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessoRepository processoRepository;

    public ProcessoResource(ProcessoRepository processoRepository) {
        this.processoRepository = processoRepository;
    }

    /**
     * {@code POST  /processos} : Create a new processo.
     *
     * @param processo the processo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processo, or with status {@code 400 (Bad Request)} if the processo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/processos")
    public ResponseEntity<Processo> createProcesso(@Valid @RequestBody Processo processo) throws URISyntaxException {
        log.debug("REST request to save Processo : {}", processo);
        if (processo.getId() != null) {
            throw new BadRequestAlertException("A new processo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Processo result = processoRepository.save(processo);
        return ResponseEntity
            .created(new URI("/api/processos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /processos/:id} : Updates an existing processo.
     *
     * @param id the id of the processo to save.
     * @param processo the processo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processo,
     * or with status {@code 400 (Bad Request)} if the processo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/processos/{id}")
    public ResponseEntity<Processo> updateProcesso(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Processo processo
    ) throws URISyntaxException {
        log.debug("REST request to update Processo : {}, {}", id, processo);
        if (processo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Processo result = processoRepository.save(processo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /processos/:id} : Partial updates given fields of an existing processo, field will ignore if it is null
     *
     * @param id the id of the processo to save.
     * @param processo the processo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processo,
     * or with status {@code 400 (Bad Request)} if the processo is not valid,
     * or with status {@code 404 (Not Found)} if the processo is not found,
     * or with status {@code 500 (Internal Server Error)} if the processo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/processos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Processo> partialUpdateProcesso(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Processo processo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Processo partially : {}, {}", id, processo);
        if (processo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Processo> result = processoRepository
            .findById(processo.getId())
            .map(existingProcesso -> {
                if (processo.getStatusProcesso() != null) {
                    existingProcesso.setStatusProcesso(processo.getStatusProcesso());
                }
                if (processo.getMatricula() != null) {
                    existingProcesso.setMatricula(processo.getMatricula());
                }
                if (processo.getNome() != null) {
                    existingProcesso.setNome(processo.getNome());
                }
                if (processo.getData() != null) {
                    existingProcesso.setData(processo.getData());
                }
                if (processo.getNumeroDaDefesa() != null) {
                    existingProcesso.setNumeroDaDefesa(processo.getNumeroDaDefesa());
                }
                if (processo.getStatusSigaa() != null) {
                    existingProcesso.setStatusSigaa(processo.getStatusSigaa());
                }
                if (processo.getNumeroSipac() != null) {
                    existingProcesso.setNumeroSipac(processo.getNumeroSipac());
                }
                if (processo.getEnviadoBiblioteca() != null) {
                    existingProcesso.setEnviadoBiblioteca(processo.getEnviadoBiblioteca());
                }

                return existingProcesso;
            })
            .map(processoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processo.getId().toString())
        );
    }

    /**
     * {@code GET  /processos} : get all the processos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processos in body.
     */
    @GetMapping("/processos")
    public List<Processo> getAllProcessos(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Processos");
        if (eagerload) {
            return processoRepository.findAllWithEagerRelationships();
        } else {
            return processoRepository.findAll();
        }
    }

    /**
     * {@code GET  /processos/:id} : get the "id" processo.
     *
     * @param id the id of the processo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/processos/{id}")
    public ResponseEntity<Processo> getProcesso(@PathVariable Long id) {
        log.debug("REST request to get Processo : {}", id);
        Optional<Processo> processo = processoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(processo);
    }

    /**
     * {@code DELETE  /processos/:id} : delete the "id" processo.
     *
     * @param id the id of the processo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/processos/{id}")
    public ResponseEntity<Void> deleteProcesso(@PathVariable Long id) {
        log.debug("REST request to delete Processo : {}", id);
        processoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
