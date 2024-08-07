package com.iit.mit.web.rest;

import static com.iit.mit.domain.ConferenceAsserts.*;
import static com.iit.mit.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.mit.IntegrationTest;
import com.iit.mit.domain.Conference;
import com.iit.mit.domain.Location;
import com.iit.mit.repository.ConferenceRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ConferenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConferenceResourceIT {

    private static final String DEFAULT_CONFERENCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONFERENCE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/conferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConferenceMockMvc;

    private Conference conference;

    private Conference insertedConference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conference createEntity(EntityManager em) {
        Conference conference = new Conference()
            .conferenceName(DEFAULT_CONFERENCE_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return conference;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conference createUpdatedEntity(EntityManager em) {
        Conference conference = new Conference()
            .conferenceName(UPDATED_CONFERENCE_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return conference;
    }

    @BeforeEach
    public void initTest() {
        conference = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedConference != null) {
            conferenceRepository.delete(insertedConference);
            insertedConference = null;
        }
    }

    @Test
    @Transactional
    void createConference() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Conference
        var returnedConference = om.readValue(
            restConferenceMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conference))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Conference.class
        );

        // Validate the Conference in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertConferenceUpdatableFieldsEquals(returnedConference, getPersistedConference(returnedConference));

        insertedConference = returnedConference;
    }

    @Test
    @Transactional
    void createConferenceWithExistingId() throws Exception {
        // Create the Conference with an existing ID
        conference.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConferenceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conference)))
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConferenceNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conference.setConferenceName(null);

        // Create the Conference, which fails.

        restConferenceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conference)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConferences() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conference.getId().intValue())))
            .andExpect(jsonPath("$.[*].conferenceName").value(hasItem(DEFAULT_CONFERENCE_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getConference() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get the conference
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, conference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conference.getId().intValue()))
            .andExpect(jsonPath("$.conferenceName").value(DEFAULT_CONFERENCE_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getConferencesByIdFiltering() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        Long id = conference.getId();

        defaultConferenceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultConferenceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultConferenceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConferencesByConferenceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where conferenceName equals to
        defaultConferenceFiltering("conferenceName.equals=" + DEFAULT_CONFERENCE_NAME, "conferenceName.equals=" + UPDATED_CONFERENCE_NAME);
    }

    @Test
    @Transactional
    void getAllConferencesByConferenceNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where conferenceName in
        defaultConferenceFiltering(
            "conferenceName.in=" + DEFAULT_CONFERENCE_NAME + "," + UPDATED_CONFERENCE_NAME,
            "conferenceName.in=" + UPDATED_CONFERENCE_NAME
        );
    }

    @Test
    @Transactional
    void getAllConferencesByConferenceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where conferenceName is not null
        defaultConferenceFiltering("conferenceName.specified=true", "conferenceName.specified=false");
    }

    @Test
    @Transactional
    void getAllConferencesByConferenceNameContainsSomething() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where conferenceName contains
        defaultConferenceFiltering(
            "conferenceName.contains=" + DEFAULT_CONFERENCE_NAME,
            "conferenceName.contains=" + UPDATED_CONFERENCE_NAME
        );
    }

    @Test
    @Transactional
    void getAllConferencesByConferenceNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where conferenceName does not contain
        defaultConferenceFiltering(
            "conferenceName.doesNotContain=" + UPDATED_CONFERENCE_NAME,
            "conferenceName.doesNotContain=" + DEFAULT_CONFERENCE_NAME
        );
    }

    @Test
    @Transactional
    void getAllConferencesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where startDate equals to
        defaultConferenceFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllConferencesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where startDate in
        defaultConferenceFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllConferencesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where startDate is not null
        defaultConferenceFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllConferencesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where endDate equals to
        defaultConferenceFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllConferencesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where endDate in
        defaultConferenceFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllConferencesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where endDate is not null
        defaultConferenceFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllConferencesByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            conferenceRepository.saveAndFlush(conference);
            location = LocationResourceIT.createEntity(em);
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        conference.setLocation(location);
        conferenceRepository.saveAndFlush(conference);
        Long locationId = location.getId();
        // Get all the conferenceList where location equals to locationId
        defaultConferenceShouldBeFound("locationId.equals=" + locationId);

        // Get all the conferenceList where location equals to (locationId + 1)
        defaultConferenceShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    private void defaultConferenceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultConferenceShouldBeFound(shouldBeFound);
        defaultConferenceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConferenceShouldBeFound(String filter) throws Exception {
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conference.getId().intValue())))
            .andExpect(jsonPath("$.[*].conferenceName").value(hasItem(DEFAULT_CONFERENCE_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConferenceShouldNotBeFound(String filter) throws Exception {
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConference() throws Exception {
        // Get the conference
        restConferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConference() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the conference
        Conference updatedConference = conferenceRepository.findById(conference.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConference are not directly saved in db
        em.detach(updatedConference);
        updatedConference.conferenceName(UPDATED_CONFERENCE_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restConferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConference.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedConference))
            )
            .andExpect(status().isOk());

        // Validate the Conference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConferenceToMatchAllProperties(updatedConference);
    }

    @Test
    @Transactional
    void putNonExistingConference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conference.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conference.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conference))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conference))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conference)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConferenceWithPatch() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the conference using partial update
        Conference partialUpdatedConference = new Conference();
        partialUpdatedConference.setId(conference.getId());

        restConferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConference.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConference))
            )
            .andExpect(status().isOk());

        // Validate the Conference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConferenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConference, conference),
            getPersistedConference(conference)
        );
    }

    @Test
    @Transactional
    void fullUpdateConferenceWithPatch() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the conference using partial update
        Conference partialUpdatedConference = new Conference();
        partialUpdatedConference.setId(conference.getId());

        partialUpdatedConference.conferenceName(UPDATED_CONFERENCE_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restConferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConference.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConference))
            )
            .andExpect(status().isOk());

        // Validate the Conference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConferenceUpdatableFieldsEquals(partialUpdatedConference, getPersistedConference(partialUpdatedConference));
    }

    @Test
    @Transactional
    void patchNonExistingConference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conference.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conference.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(conference))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(conference))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(conference))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConference() throws Exception {
        // Initialize the database
        insertedConference = conferenceRepository.saveAndFlush(conference);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the conference
        restConferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, conference.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return conferenceRepository.count();
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

    protected Conference getPersistedConference(Conference conference) {
        return conferenceRepository.findById(conference.getId()).orElseThrow();
    }

    protected void assertPersistedConferenceToMatchAllProperties(Conference expectedConference) {
        assertConferenceAllPropertiesEquals(expectedConference, getPersistedConference(expectedConference));
    }

    protected void assertPersistedConferenceToMatchUpdatableProperties(Conference expectedConference) {
        assertConferenceAllUpdatablePropertiesEquals(expectedConference, getPersistedConference(expectedConference));
    }
}
