package com.iit.mit.service.impl;

import com.iit.mit.domain.PersonType;
import com.iit.mit.repository.PersonTypeRepository;
import com.iit.mit.service.PersonTypeService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iit.mit.domain.PersonType}.
 */
@Service
@Transactional
public class PersonTypeServiceImpl implements PersonTypeService {

    private static final Logger log = LoggerFactory.getLogger(PersonTypeServiceImpl.class);

    private final PersonTypeRepository personTypeRepository;

    public PersonTypeServiceImpl(PersonTypeRepository personTypeRepository) {
        this.personTypeRepository = personTypeRepository;
    }

    @Override
    public PersonType save(PersonType personType) {
        log.debug("Request to save PersonType : {}", personType);
        return personTypeRepository.save(personType);
    }

    @Override
    public PersonType update(PersonType personType) {
        log.debug("Request to update PersonType : {}", personType);
        return personTypeRepository.save(personType);
    }

    @Override
    public Optional<PersonType> partialUpdate(PersonType personType) {
        log.debug("Request to partially update PersonType : {}", personType);

        return personTypeRepository
            .findById(personType.getId())
            .map(existingPersonType -> {
                if (personType.getJobTitle() != null) {
                    existingPersonType.setJobTitle(personType.getJobTitle());
                }
                if (personType.getRole() != null) {
                    existingPersonType.setRole(personType.getRole());
                }
                if (personType.getLevel() != null) {
                    existingPersonType.setLevel(personType.getLevel());
                }

                return existingPersonType;
            })
            .map(personTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonType> findAll() {
        log.debug("Request to get all PersonTypes");
        return personTypeRepository.findAll();
    }

    /**
     *  Get all the personTypes where ApplicationUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PersonType> findAllWhereApplicationUserIsNull() {
        log.debug("Request to get all personTypes where ApplicationUser is null");
        return StreamSupport.stream(personTypeRepository.findAll().spliterator(), false)
            .filter(personType -> personType.getApplicationUser() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonType> findOne(Long id) {
        log.debug("Request to get PersonType : {}", id);
        return personTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PersonType : {}", id);
        personTypeRepository.deleteById(id);
    }
}
