package com.iit.mit.web.rest;

import com.iit.mit.domain.PersonType;
import com.iit.mit.repository.PersonTypeRepository;
import com.iit.mit.service.PersonTypeService;
import com.iit.mit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.iit.mit.domain.PersonType}.
 */
@RestController
@RequestMapping("/api/person-types")
public class PersonTypeResource {

    private static final Logger log = LoggerFactory.getLogger(PersonTypeResource.class);

    private static final String ENTITY_NAME = "personType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonTypeService personTypeService;

    private final PersonTypeRepository personTypeRepository;

    public PersonTypeResource(PersonTypeService personTypeService, PersonTypeRepository personTypeRepository) {
        this.personTypeService = personTypeService;
        this.personTypeRepository = personTypeRepository;
    }

    /**
     * {@code POST  /person-types} : Create a new personType.
     *
     * @param personType the personType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personType, or with status {@code 400 (Bad Request)} if the personType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PersonType> createPersonType(@RequestBody PersonType personType) throws URISyntaxException {
        log.debug("REST request to save PersonType : {}", personType);
        if (personType.getId() != null) {
            throw new BadRequestAlertException("A new personType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        personType = personTypeService.save(personType);
        return ResponseEntity.created(new URI("/api/person-types/" + personType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, personType.getId().toString()))
            .body(personType);
    }

    /**
     * {@code PUT  /person-types/:id} : Updates an existing personType.
     *
     * @param id the id of the personType to save.
     * @param personType the personType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personType,
     * or with status {@code 400 (Bad Request)} if the personType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonType> updatePersonType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonType personType
    ) throws URISyntaxException {
        log.debug("REST request to update PersonType : {}, {}", id, personType);
        if (personType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        personType = personTypeService.update(personType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personType.getId().toString()))
            .body(personType);
    }

    /**
     * {@code PATCH  /person-types/:id} : Partial updates given fields of an existing personType, field will ignore if it is null
     *
     * @param id the id of the personType to save.
     * @param personType the personType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personType,
     * or with status {@code 400 (Bad Request)} if the personType is not valid,
     * or with status {@code 404 (Not Found)} if the personType is not found,
     * or with status {@code 500 (Internal Server Error)} if the personType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonType> partialUpdatePersonType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonType personType
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonType partially : {}, {}", id, personType);
        if (personType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonType> result = personTypeService.partialUpdate(personType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personType.getId().toString())
        );
    }

    /**
     * {@code GET  /person-types} : get all the personTypes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personTypes in body.
     */
    @GetMapping("")
    public List<PersonType> getAllPersonTypes(@RequestParam(name = "filter", required = false) String filter) {
        if ("applicationuser-is-null".equals(filter)) {
            log.debug("REST request to get all PersonTypes where applicationUser is null");
            return personTypeService.findAllWhereApplicationUserIsNull();
        }
        log.debug("REST request to get all PersonTypes");
        return personTypeService.findAll();
    }

    /**
     * {@code GET  /person-types/:id} : get the "id" personType.
     *
     * @param id the id of the personType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonType> getPersonType(@PathVariable("id") Long id) {
        log.debug("REST request to get PersonType : {}", id);
        Optional<PersonType> personType = personTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personType);
    }

    /**
     * {@code DELETE  /person-types/:id} : delete the "id" personType.
     *
     * @param id the id of the personType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonType(@PathVariable("id") Long id) {
        log.debug("REST request to delete PersonType : {}", id);
        personTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
