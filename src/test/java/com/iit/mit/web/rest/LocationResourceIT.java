package com.iit.mit.web.rest;

import static com.iit.mit.domain.LocationAsserts.*;
import static com.iit.mit.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.mit.IntegrationTest;
import com.iit.mit.domain.Location;
import com.iit.mit.domain.enumeration.Region;
import com.iit.mit.repository.LocationRepository;
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
 * Integration tests for the {@link LocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationResourceIT {

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    private static final Region DEFAULT_REGION = Region.NA;
    private static final Region UPDATED_REGION = Region.LATAM;

    private static final String ENTITY_API_URL = "/api/locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationMockMvc;

    private Location location;

    private Location insertedLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .stateProvince(DEFAULT_STATE_PROVINCE)
            .countryName(DEFAULT_COUNTRY_NAME)
            .region(DEFAULT_REGION);
        return location;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity(EntityManager em) {
        Location location = new Location()
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .countryName(UPDATED_COUNTRY_NAME)
            .region(UPDATED_REGION);
        return location;
    }

    @BeforeEach
    public void initTest() {
        location = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLocation != null) {
            locationRepository.delete(insertedLocation);
            insertedLocation = null;
        }
    }

    @Test
    @Transactional
    void createLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Location
        var returnedLocation = om.readValue(
            restLocationMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(location)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Location.class
        );

        // Validate the Location in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLocationUpdatableFieldsEquals(returnedLocation, getPersistedLocation(returnedLocation));

        insertedLocation = returnedLocation;
    }

    @Test
    @Transactional
    void createLocationWithExistingId() throws Exception {
        // Create the Location with an existing ID
        location.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(location)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocations() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())));
    }

    @Test
    @Transactional
    void getLocation() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()));
    }

    @Test
    @Transactional
    void getLocationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        Long id = location.getId();

        defaultLocationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLocationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLocationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddress equals to
        defaultLocationFiltering("streetAddress.equals=" + DEFAULT_STREET_ADDRESS, "streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddress in
        defaultLocationFiltering(
            "streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS,
            "streetAddress.in=" + UPDATED_STREET_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddress is not null
        defaultLocationFiltering("streetAddress.specified=true", "streetAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddress contains
        defaultLocationFiltering("streetAddress.contains=" + DEFAULT_STREET_ADDRESS, "streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where streetAddress does not contain
        defaultLocationFiltering(
            "streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS,
            "streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode equals to
        defaultLocationFiltering("postalCode.equals=" + DEFAULT_POSTAL_CODE, "postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode in
        defaultLocationFiltering(
            "postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE,
            "postalCode.in=" + UPDATED_POSTAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode is not null
        defaultLocationFiltering("postalCode.specified=true", "postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode contains
        defaultLocationFiltering("postalCode.contains=" + DEFAULT_POSTAL_CODE, "postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode does not contain
        defaultLocationFiltering("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE, "postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where city equals to
        defaultLocationFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where city in
        defaultLocationFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where city is not null
        defaultLocationFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where city contains
        defaultLocationFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where city does not contain
        defaultLocationFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByStateProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where stateProvince equals to
        defaultLocationFiltering("stateProvince.equals=" + DEFAULT_STATE_PROVINCE, "stateProvince.equals=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllLocationsByStateProvinceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where stateProvince in
        defaultLocationFiltering(
            "stateProvince.in=" + DEFAULT_STATE_PROVINCE + "," + UPDATED_STATE_PROVINCE,
            "stateProvince.in=" + UPDATED_STATE_PROVINCE
        );
    }

    @Test
    @Transactional
    void getAllLocationsByStateProvinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where stateProvince is not null
        defaultLocationFiltering("stateProvince.specified=true", "stateProvince.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByStateProvinceContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where stateProvince contains
        defaultLocationFiltering("stateProvince.contains=" + DEFAULT_STATE_PROVINCE, "stateProvince.contains=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllLocationsByStateProvinceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where stateProvince does not contain
        defaultLocationFiltering(
            "stateProvince.doesNotContain=" + UPDATED_STATE_PROVINCE,
            "stateProvince.doesNotContain=" + DEFAULT_STATE_PROVINCE
        );
    }

    @Test
    @Transactional
    void getAllLocationsByCountryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where countryName equals to
        defaultLocationFiltering("countryName.equals=" + DEFAULT_COUNTRY_NAME, "countryName.equals=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByCountryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where countryName in
        defaultLocationFiltering(
            "countryName.in=" + DEFAULT_COUNTRY_NAME + "," + UPDATED_COUNTRY_NAME,
            "countryName.in=" + UPDATED_COUNTRY_NAME
        );
    }

    @Test
    @Transactional
    void getAllLocationsByCountryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where countryName is not null
        defaultLocationFiltering("countryName.specified=true", "countryName.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByCountryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where countryName contains
        defaultLocationFiltering("countryName.contains=" + DEFAULT_COUNTRY_NAME, "countryName.contains=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByCountryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where countryName does not contain
        defaultLocationFiltering(
            "countryName.doesNotContain=" + UPDATED_COUNTRY_NAME,
            "countryName.doesNotContain=" + DEFAULT_COUNTRY_NAME
        );
    }

    @Test
    @Transactional
    void getAllLocationsByRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where region equals to
        defaultLocationFiltering("region.equals=" + DEFAULT_REGION, "region.equals=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllLocationsByRegionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where region in
        defaultLocationFiltering("region.in=" + DEFAULT_REGION + "," + UPDATED_REGION, "region.in=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllLocationsByRegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where region is not null
        defaultLocationFiltering("region.specified=true", "region.specified=false");
    }

    private void defaultLocationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLocationShouldBeFound(shouldBeFound);
        defaultLocationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationShouldBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())));

        // Check, that the count call also returns 1
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationShouldNotBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocation() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .countryName(UPDATED_COUNTRY_NAME)
            .region(UPDATED_REGION);

        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocation.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocationToMatchAllProperties(updatedLocation);
    }

    @Test
    @Transactional
    void putNonExistingLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, location.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(location)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation.streetAddress(UPDATED_STREET_ADDRESS).postalCode(UPDATED_POSTAL_CODE).countryName(UPDATED_COUNTRY_NAME);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLocation, location), getPersistedLocation(location));
    }

    @Test
    @Transactional
    void fullUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .countryName(UPDATED_COUNTRY_NAME)
            .region(UPDATED_REGION);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationUpdatableFieldsEquals(partialUpdatedLocation, getPersistedLocation(partialUpdatedLocation));
    }

    @Test
    @Transactional
    void patchNonExistingLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, location.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(location)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocation() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the location
        restLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, location.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return locationRepository.count();
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

    protected Location getPersistedLocation(Location location) {
        return locationRepository.findById(location.getId()).orElseThrow();
    }

    protected void assertPersistedLocationToMatchAllProperties(Location expectedLocation) {
        assertLocationAllPropertiesEquals(expectedLocation, getPersistedLocation(expectedLocation));
    }

    protected void assertPersistedLocationToMatchUpdatableProperties(Location expectedLocation) {
        assertLocationAllUpdatablePropertiesEquals(expectedLocation, getPersistedLocation(expectedLocation));
    }
}
