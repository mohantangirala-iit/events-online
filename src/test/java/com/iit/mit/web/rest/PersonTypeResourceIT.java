package com.iit.mit.web.rest;

import static com.iit.mit.domain.PersonTypeAsserts.*;
import static com.iit.mit.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.mit.IntegrationTest;
import com.iit.mit.domain.PersonType;
import com.iit.mit.domain.enumeration.Level;
import com.iit.mit.domain.enumeration.Role;
import com.iit.mit.repository.PersonTypeRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonTypeResourceIT {

    private static final Long DEFAULT_JOB_TITLE = 1L;
    private static final Long UPDATED_JOB_TITLE = 2L;

    private static final Role DEFAULT_ROLE = Role.SPEAKER;
    private static final Role UPDATED_ROLE = Role.GUEST;

    private static final Level DEFAULT_LEVEL = Level.BASIC;
    private static final Level UPDATED_LEVEL = Level.INTERMEDIATE;

    private static final String ENTITY_API_URL = "/api/person-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonTypeRepository personTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonTypeMockMvc;

    private PersonType personType;

    private PersonType insertedPersonType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonType createEntity(EntityManager em) {
        PersonType personType = new PersonType().jobTitle(DEFAULT_JOB_TITLE).role(DEFAULT_ROLE).level(DEFAULT_LEVEL);
        return personType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonType createUpdatedEntity(EntityManager em) {
        PersonType personType = new PersonType().jobTitle(UPDATED_JOB_TITLE).role(UPDATED_ROLE).level(UPDATED_LEVEL);
        return personType;
    }

    @BeforeEach
    public void initTest() {
        personType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPersonType != null) {
            personTypeRepository.delete(insertedPersonType);
            insertedPersonType = null;
        }
    }

    @Test
    @Transactional
    void createPersonType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PersonType
        var returnedPersonType = om.readValue(
            restPersonTypeMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PersonType.class
        );

        // Validate the PersonType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPersonTypeUpdatableFieldsEquals(returnedPersonType, getPersistedPersonType(returnedPersonType));

        insertedPersonType = returnedPersonType;
    }

    @Test
    @Transactional
    void createPersonTypeWithExistingId() throws Exception {
        // Create the PersonType with an existing ID
        personType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonTypeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personType)))
            .andExpect(status().isBadRequest());

        // Validate the PersonType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonTypes() throws Exception {
        // Initialize the database
        insertedPersonType = personTypeRepository.saveAndFlush(personType);

        // Get all the personTypeList
        restPersonTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personType.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())));
    }

    @Test
    @Transactional
    void getPersonType() throws Exception {
        // Initialize the database
        insertedPersonType = personTypeRepository.saveAndFlush(personType);

        // Get the personType
        restPersonTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, personType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personType.getId().intValue()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE.intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPersonType() throws Exception {
        // Get the personType
        restPersonTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonType() throws Exception {
        // Initialize the database
        insertedPersonType = personTypeRepository.saveAndFlush(personType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personType
        PersonType updatedPersonType = personTypeRepository.findById(personType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonType are not directly saved in db
        em.detach(updatedPersonType);
        updatedPersonType.jobTitle(UPDATED_JOB_TITLE).role(UPDATED_ROLE).level(UPDATED_LEVEL);

        restPersonTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPersonType))
            )
            .andExpect(status().isOk());

        // Validate the PersonType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonTypeToMatchAllProperties(updatedPersonType);
    }

    @Test
    @Transactional
    void putNonExistingPersonType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonTypeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPersonType = personTypeRepository.saveAndFlush(personType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personType using partial update
        PersonType partialUpdatedPersonType = new PersonType();
        partialUpdatedPersonType.setId(personType.getId());

        restPersonTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonType))
            )
            .andExpect(status().isOk());

        // Validate the PersonType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPersonType, personType),
            getPersistedPersonType(personType)
        );
    }

    @Test
    @Transactional
    void fullUpdatePersonTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPersonType = personTypeRepository.saveAndFlush(personType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personType using partial update
        PersonType partialUpdatedPersonType = new PersonType();
        partialUpdatedPersonType.setId(personType.getId());

        partialUpdatedPersonType.jobTitle(UPDATED_JOB_TITLE).role(UPDATED_ROLE).level(UPDATED_LEVEL);

        restPersonTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonType))
            )
            .andExpect(status().isOk());

        // Validate the PersonType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonTypeUpdatableFieldsEquals(partialUpdatedPersonType, getPersistedPersonType(partialUpdatedPersonType));
    }

    @Test
    @Transactional
    void patchNonExistingPersonType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(personType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonType() throws Exception {
        // Initialize the database
        insertedPersonType = personTypeRepository.saveAndFlush(personType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the personType
        restPersonTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, personType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return personTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PersonType getPersistedPersonType(PersonType personType) {
        return personTypeRepository.findById(personType.getId()).orElseThrow();
    }

    protected void assertPersistedPersonTypeToMatchAllProperties(PersonType expectedPersonType) {
        assertPersonTypeAllPropertiesEquals(expectedPersonType, getPersistedPersonType(expectedPersonType));
    }

    protected void assertPersistedPersonTypeToMatchUpdatableProperties(PersonType expectedPersonType) {
        assertPersonTypeAllUpdatablePropertiesEquals(expectedPersonType, getPersistedPersonType(expectedPersonType));
    }
}
