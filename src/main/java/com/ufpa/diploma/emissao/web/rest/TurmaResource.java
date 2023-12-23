package com.ufpa.diploma.emissao.web.rest;

import com.ufpa.diploma.emissao.domain.Turma;
import com.ufpa.diploma.emissao.repository.TurmaRepository;
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
 * REST controller for managing {@link com.ufpa.diploma.emissao.domain.Turma}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TurmaResource {

    private final Logger log = LoggerFactory.getLogger(TurmaResource.class);

    private static final String ENTITY_NAME = "turma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TurmaRepository turmaRepository;

    public TurmaResource(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    /**
     * {@code POST  /turmas} : Create a new turma.
     *
     * @param turma the turma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new turma, or with status {@code 400 (Bad Request)} if the turma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/turmas")
    public ResponseEntity<Turma> createTurma(@RequestBody Turma turma) throws URISyntaxException {
        log.debug("REST request to save Turma : {}", turma);
        if (turma.getId() != null) {
            throw new BadRequestAlertException("A new turma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Turma result = turmaRepository.save(turma);
        return ResponseEntity
            .created(new URI("/api/turmas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /turmas/:id} : Updates an existing turma.
     *
     * @param id the id of the turma to save.
     * @param turma the turma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turma,
     * or with status {@code 400 (Bad Request)} if the turma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the turma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/turmas/{id}")
    public ResponseEntity<Turma> updateTurma(@PathVariable(value = "id", required = false) final Long id, @RequestBody Turma turma)
        throws URISyntaxException {
        log.debug("REST request to update Turma : {}, {}", id, turma);
        if (turma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Turma result = turmaRepository.save(turma);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, turma.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /turmas/:id} : Partial updates given fields of an existing turma, field will ignore if it is null
     *
     * @param id the id of the turma to save.
     * @param turma the turma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turma,
     * or with status {@code 400 (Bad Request)} if the turma is not valid,
     * or with status {@code 404 (Not Found)} if the turma is not found,
     * or with status {@code 500 (Internal Server Error)} if the turma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/turmas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Turma> partialUpdateTurma(@PathVariable(value = "id", required = false) final Long id, @RequestBody Turma turma)
        throws URISyntaxException {
        log.debug("REST request to partial update Turma partially : {}, {}", id, turma);
        if (turma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Turma> result = turmaRepository
            .findById(turma.getId())
            .map(existingTurma -> {
                if (turma.getCurso() != null) {
                    existingTurma.setCurso(turma.getCurso());
                }
                if (turma.getSigla() != null) {
                    existingTurma.setSigla(turma.getSigla());
                }
                if (turma.getAno() != null) {
                    existingTurma.setAno(turma.getAno());
                }

                return existingTurma;
            })
            .map(turmaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, turma.getId().toString())
        );
    }

    /**
     * {@code GET  /turmas} : get all the turmas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of turmas in body.
     */
    @GetMapping("/turmas")
    public List<Turma> getAllTurmas() {
        log.debug("REST request to get all Turmas");
        return turmaRepository.findAll();
    }

    /**
     * {@code GET  /turmas/:id} : get the "id" turma.
     *
     * @param id the id of the turma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the turma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/turmas/{id}")
    public ResponseEntity<Turma> getTurma(@PathVariable Long id) {
        log.debug("REST request to get Turma : {}", id);
        Optional<Turma> turma = turmaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(turma);
    }

    /**
     * {@code DELETE  /turmas/:id} : delete the "id" turma.
     *
     * @param id the id of the turma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/turmas/{id}")
    public ResponseEntity<Void> deleteTurma(@PathVariable Long id) {
        log.debug("REST request to delete Turma : {}", id);
        turmaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
