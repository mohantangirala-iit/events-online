package com.iit.mit.service;

import com.iit.mit.domain.Conference;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.iit.mit.domain.Conference}.
 */
public interface ConferenceService {
    /**
     * Save a conference.
     *
     * @param conference the entity to save.
     * @return the persisted entity.
     */
    Conference save(Conference conference);

    /**
     * Updates a conference.
     *
     * @param conference the entity to update.
     * @return the persisted entity.
     */
    Conference update(Conference conference);

    /**
     * Partially updates a conference.
     *
     * @param conference the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Conference> partialUpdate(Conference conference);

    /**
     * Get all the Conference where ApplicationUser is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Conference> findAllWhereApplicationUserIsNull();

    /**
     * Get the "id" conference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Conference> findOne(Long id);

    /**
     * Delete the "id" conference.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
