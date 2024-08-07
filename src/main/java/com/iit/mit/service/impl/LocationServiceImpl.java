package com.iit.mit.service.impl;

import com.iit.mit.domain.Location;
import com.iit.mit.repository.LocationRepository;
import com.iit.mit.service.LocationService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iit.mit.domain.Location}.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location save(Location location) {
        log.debug("Request to save Location : {}", location);
        return locationRepository.save(location);
    }

    @Override
    public Location update(Location location) {
        log.debug("Request to update Location : {}", location);
        return locationRepository.save(location);
    }

    @Override
    public Optional<Location> partialUpdate(Location location) {
        log.debug("Request to partially update Location : {}", location);

        return locationRepository
            .findById(location.getId())
            .map(existingLocation -> {
                if (location.getStreetAddress() != null) {
                    existingLocation.setStreetAddress(location.getStreetAddress());
                }
                if (location.getPostalCode() != null) {
                    existingLocation.setPostalCode(location.getPostalCode());
                }
                if (location.getCity() != null) {
                    existingLocation.setCity(location.getCity());
                }
                if (location.getStateProvince() != null) {
                    existingLocation.setStateProvince(location.getStateProvince());
                }
                if (location.getCountryName() != null) {
                    existingLocation.setCountryName(location.getCountryName());
                }
                if (location.getRegion() != null) {
                    existingLocation.setRegion(location.getRegion());
                }

                return existingLocation;
            })
            .map(locationRepository::save);
    }

    /**
     *  Get all the locations where Conference is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Location> findAllWhereConferenceIsNull() {
        log.debug("Request to get all locations where Conference is null");
        return StreamSupport.stream(locationRepository.findAll().spliterator(), false)
            .filter(location -> location.getConference() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Location> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }
}
