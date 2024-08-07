package com.iit.mit.service.impl;

import com.iit.mit.domain.Conference;
import com.iit.mit.repository.ConferenceRepository;
import com.iit.mit.service.ConferenceService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iit.mit.domain.Conference}.
 */
@Service
@Transactional
public class ConferenceServiceImpl implements ConferenceService {

    private static final Logger log = LoggerFactory.getLogger(ConferenceServiceImpl.class);

    private final ConferenceRepository conferenceRepository;

    public ConferenceServiceImpl(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    @Override
    public Conference save(Conference conference) {
        log.debug("Request to save Conference : {}", conference);
        return conferenceRepository.save(conference);
    }

    @Override
    public Conference update(Conference conference) {
        log.debug("Request to update Conference : {}", conference);
        return conferenceRepository.save(conference);
    }

    @Override
    public Optional<Conference> partialUpdate(Conference conference) {
        log.debug("Request to partially update Conference : {}", conference);

        return conferenceRepository
            .findById(conference.getId())
            .map(existingConference -> {
                if (conference.getConferenceName() != null) {
                    existingConference.setConferenceName(conference.getConferenceName());
                }
                if (conference.getStartDate() != null) {
                    existingConference.setStartDate(conference.getStartDate());
                }
                if (conference.getEndDate() != null) {
                    existingConference.setEndDate(conference.getEndDate());
                }

                return existingConference;
            })
            .map(conferenceRepository::save);
    }

    /**
     *  Get all the conferences where ApplicationUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Conference> findAllWhereApplicationUserIsNull() {
        log.debug("Request to get all conferences where ApplicationUser is null");
        return StreamSupport.stream(conferenceRepository.findAll().spliterator(), false)
            .filter(conference -> conference.getApplicationUser() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Conference> findOne(Long id) {
        log.debug("Request to get Conference : {}", id);
        return conferenceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Conference : {}", id);
        conferenceRepository.deleteById(id);
    }
}
