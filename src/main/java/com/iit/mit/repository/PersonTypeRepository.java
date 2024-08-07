package com.iit.mit.repository;

import com.iit.mit.domain.PersonType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PersonType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonTypeRepository extends JpaRepository<PersonType, Long> {}
