package com.iit.mit.service.impl;

import com.iit.mit.domain.Event;
import com.iit.mit.repository.EventRepository;
import com.iit.mit.service.EventService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iit.mit.domain.Event}.
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event save(Event event) {
        log.debug("Request to save Event : {}", event);
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event) {
        log.debug("Request to update Event : {}", event);
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> partialUpdate(Event event) {
        log.debug("Request to partially update Event : {}", event);

        return eventRepository
            .findById(event.getId())
            .map(existingEvent -> {
                if (event.getTitle() != null) {
                    existingEvent.setTitle(event.getTitle());
                }
                if (event.getDescription() != null) {
                    existingEvent.setDescription(event.getDescription());
                }
                if (event.getAudience() != null) {
                    existingEvent.setAudience(event.getAudience());
                }
                if (event.getLevel() != null) {
                    existingEvent.setLevel(event.getLevel());
                }
                if (event.getLanguage() != null) {
                    existingEvent.setLanguage(event.getLanguage());
                }
                if (event.getDate() != null) {
                    existingEvent.setDate(event.getDate());
                }
                if (event.getStartTime() != null) {
                    existingEvent.setStartTime(event.getStartTime());
                }
                if (event.getEndTime() != null) {
                    existingEvent.setEndTime(event.getEndTime());
                }
                if (event.getEventType() != null) {
                    existingEvent.setEventType(event.getEventType());
                }

                return existingEvent;
            })
            .map(eventRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Event> findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        return eventRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.deleteById(id);
    }
}
