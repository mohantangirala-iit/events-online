package com.iit.mit.service.impl;

import com.iit.mit.domain.ApplicationUser;
import com.iit.mit.repository.ApplicationUserRepository;
import com.iit.mit.repository.UserRepository;
import com.iit.mit.service.ApplicationUserService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iit.mit.domain.ApplicationUser}.
 */
@Service
@Transactional
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationUserServiceImpl.class);

    private final ApplicationUserRepository applicationUserRepository;

    private final UserRepository userRepository;

    public ApplicationUserServiceImpl(ApplicationUserRepository applicationUserRepository, UserRepository userRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ApplicationUser save(ApplicationUser applicationUser) {
        log.debug("Request to save ApplicationUser : {}", applicationUser);
        Long userId = applicationUser.getInternalUser().getId();
        userRepository.findById(userId).ifPresent(applicationUser::internalUser);
        return applicationUserRepository.save(applicationUser);
    }

    @Override
    public ApplicationUser update(ApplicationUser applicationUser) {
        log.debug("Request to update ApplicationUser : {}", applicationUser);
        return applicationUserRepository.save(applicationUser);
    }

    @Override
    public Optional<ApplicationUser> partialUpdate(ApplicationUser applicationUser) {
        log.debug("Request to partially update ApplicationUser : {}", applicationUser);

        return applicationUserRepository
            .findById(applicationUser.getId())
            .map(existingApplicationUser -> {
                if (applicationUser.getPhoneNumber() != null) {
                    existingApplicationUser.setPhoneNumber(applicationUser.getPhoneNumber());
                }

                return existingApplicationUser;
            })
            .map(applicationUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationUser> findAll() {
        log.debug("Request to get all ApplicationUsers");
        return applicationUserRepository.findAll();
    }

    public Page<ApplicationUser> findAllWithEagerRelationships(Pageable pageable) {
        return applicationUserRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationUser> findOne(Long id) {
        log.debug("Request to get ApplicationUser : {}", id);
        return applicationUserRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
    }
}
