package com.iit.mit.service;

import com.iit.mit.domain.*; // for static metamodels
import com.iit.mit.domain.Conference;
import com.iit.mit.repository.ConferenceRepository;
import com.iit.mit.service.criteria.ConferenceCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Conference} entities in the database.
 * The main input is a {@link ConferenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Conference} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConferenceQueryService extends QueryService<Conference> {

    private static final Logger log = LoggerFactory.getLogger(ConferenceQueryService.class);

    private final ConferenceRepository conferenceRepository;

    public ConferenceQueryService(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    /**
     * Return a {@link Page} of {@link Conference} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Conference> findByCriteria(ConferenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Conference> specification = createSpecification(criteria);
        return conferenceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConferenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Conference> specification = createSpecification(criteria);
        return conferenceRepository.count(specification);
    }

    /**
     * Function to convert {@link ConferenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Conference> createSpecification(ConferenceCriteria criteria) {
        Specification<Conference> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Conference_.id));
            }
            if (criteria.getConferenceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConferenceName(), Conference_.conferenceName));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Conference_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Conference_.endDate));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLocationId(), root -> root.join(Conference_.location, JoinType.LEFT).get(Location_.id))
                );
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEventId(), root -> root.join(Conference_.events, JoinType.LEFT).get(Event_.id))
                );
            }
            if (criteria.getApplicationUserId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getApplicationUserId(),
                        root -> root.join(Conference_.applicationUser, JoinType.LEFT).get(ApplicationUser_.id)
                    )
                );
            }
        }
        return specification;
    }
}
