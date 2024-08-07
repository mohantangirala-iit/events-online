package com.iit.mit.service;

import com.iit.mit.domain.ApplicationUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iit.mit.domain.ApplicationUser}.
 */
public interface ApplicationUserService {
    /**
     * Save a applicationUser.
     *
     * @param applicationUser the entity to save.
     * @return the persisted entity.
     */
    ApplicationUser save(ApplicationUser applicationUser);

    /**
     * Updates a applicationUser.
     *
     * @param applicationUser the entity to update.
     * @return the persisted entity.
     */
    ApplicationUser update(ApplicationUser applicationUser);

    /**
     * Partially updates a applicationUser.
     *
     * @param applicationUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ApplicationUser> partialUpdate(ApplicationUser applicationUser);

    /**
     * Get all the applicationUsers.
     *
     * @return the list of entities.
     */
    List<ApplicationUser> findAll();

    /**
     * Get all the applicationUsers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApplicationUser> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" applicationUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApplicationUser> findOne(Long id);

    /**
     * Delete the "id" applicationUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
