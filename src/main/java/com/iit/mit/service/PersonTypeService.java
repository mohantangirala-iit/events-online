package com.iit.mit.service;

import com.iit.mit.domain.PersonType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.iit.mit.domain.PersonType}.
 */
public interface PersonTypeService {
    /**
     * Save a personType.
     *
     * @param personType the entity to save.
     * @return the persisted entity.
     */
    PersonType save(PersonType personType);

    /**
     * Updates a personType.
     *
     * @param personType the entity to update.
     * @return the persisted entity.
     */
    PersonType update(PersonType personType);

    /**
     * Partially updates a personType.
     *
     * @param personType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PersonType> partialUpdate(PersonType personType);

    /**
     * Get all the personTypes.
     *
     * @return the list of entities.
     */
    List<PersonType> findAll();

    /**
     * Get all the PersonType where ApplicationUser is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PersonType> findAllWhereApplicationUserIsNull();

    /**
     * Get the "id" personType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonType> findOne(Long id);

    /**
     * Delete the "id" personType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
