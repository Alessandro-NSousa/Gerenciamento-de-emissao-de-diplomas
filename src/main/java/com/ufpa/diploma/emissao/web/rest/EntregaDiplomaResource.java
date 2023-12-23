package com.ufpa.diploma.emissao.web.rest;

import com.ufpa.diploma.emissao.domain.EntregaDiploma;
import com.ufpa.diploma.emissao.repository.EntregaDiplomaRepository;
import com.ufpa.diploma.emissao.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ufpa.diploma.emissao.domain.EntregaDiploma}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EntregaDiplomaResource {

    private final Logger log = LoggerFactory.getLogger(EntregaDiplomaResource.class);

    private static final String ENTITY_NAME = "entregaDiploma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntregaDiplomaRepository entregaDiplomaRepository;

    public EntregaDiplomaResource(EntregaDiplomaRepository entregaDiplomaRepository) {
        this.entregaDiplomaRepository = entregaDiplomaRepository;
    }

    /**
     * {@code POST  /entrega-diplomas} : Create a new entregaDiploma.
     *
     * @param entregaDiploma the entregaDiploma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entregaDiploma, or with status {@code 400 (Bad Request)} if the entregaDiploma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entrega-diplomas")
    public ResponseEntity<EntregaDiploma> createEntregaDiploma(@RequestBody EntregaDiploma entregaDiploma) throws URISyntaxException {
        log.debug("REST request to save EntregaDiploma : {}", entregaDiploma);
        if (entregaDiploma.getId() != null) {
            throw new BadRequestAlertException("A new entregaDiploma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EntregaDiploma result = entregaDiplomaRepository.save(entregaDiploma);
        return ResponseEntity
            .created(new URI("/api/entrega-diplomas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entrega-diplomas/:id} : Updates an existing entregaDiploma.
     *
     * @param id the id of the entregaDiploma to save.
     * @param entregaDiploma the entregaDiploma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entregaDiploma,
     * or with status {@code 400 (Bad Request)} if the entregaDiploma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entregaDiploma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entrega-diplomas/{id}")
    public ResponseEntity<EntregaDiploma> updateEntregaDiploma(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntregaDiploma entregaDiploma
    ) throws URISyntaxException {
        log.debug("REST request to update EntregaDiploma : {}, {}", id, entregaDiploma);
        if (entregaDiploma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entregaDiploma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entregaDiplomaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EntregaDiploma result = entregaDiplomaRepository.save(entregaDiploma);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entregaDiploma.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /entrega-diplomas/:id} : Partial updates given fields of an existing entregaDiploma, field will ignore if it is null
     *
     * @param id the id of the entregaDiploma to save.
     * @param entregaDiploma the entregaDiploma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entregaDiploma,
     * or with status {@code 400 (Bad Request)} if the entregaDiploma is not valid,
     * or with status {@code 404 (Not Found)} if the entregaDiploma is not found,
     * or with status {@code 500 (Internal Server Error)} if the entregaDiploma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/entrega-diplomas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EntregaDiploma> partialUpdateEntregaDiploma(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntregaDiploma entregaDiploma
    ) throws URISyntaxException {
        log.debug("REST request to partial update EntregaDiploma partially : {}, {}", id, entregaDiploma);
        if (entregaDiploma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entregaDiploma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entregaDiplomaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EntregaDiploma> result = entregaDiplomaRepository
            .findById(entregaDiploma.getId())
            .map(existingEntregaDiploma -> {
                if (entregaDiploma.getDataDeEntrega() != null) {
                    existingEntregaDiploma.setDataDeEntrega(entregaDiploma.getDataDeEntrega());
                }
                if (entregaDiploma.getObservacoes() != null) {
                    existingEntregaDiploma.setObservacoes(entregaDiploma.getObservacoes());
                }

                return existingEntregaDiploma;
            })
            .map(entregaDiplomaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entregaDiploma.getId().toString())
        );
    }

    /**
     * {@code GET  /entrega-diplomas} : get all the entregaDiplomas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entregaDiplomas in body.
     */
    @GetMapping("/entrega-diplomas")
    public List<EntregaDiploma> getAllEntregaDiplomas(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all EntregaDiplomas");
        if (eagerload) {
            return entregaDiplomaRepository.findAllWithEagerRelationships();
        } else {
            return entregaDiplomaRepository.findAll();
        }
    }

    /**
     * {@code GET  /entrega-diplomas/:id} : get the "id" entregaDiploma.
     *
     * @param id the id of the entregaDiploma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entregaDiploma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entrega-diplomas/{id}")
    public ResponseEntity<EntregaDiploma> getEntregaDiploma(@PathVariable Long id) {
        log.debug("REST request to get EntregaDiploma : {}", id);
        Optional<EntregaDiploma> entregaDiploma = entregaDiplomaRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(entregaDiploma);
    }

    /**
     * {@code DELETE  /entrega-diplomas/:id} : delete the "id" entregaDiploma.
     *
     * @param id the id of the entregaDiploma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entrega-diplomas/{id}")
    public ResponseEntity<Void> deleteEntregaDiploma(@PathVariable Long id) {
        log.debug("REST request to delete EntregaDiploma : {}", id);
        entregaDiplomaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
