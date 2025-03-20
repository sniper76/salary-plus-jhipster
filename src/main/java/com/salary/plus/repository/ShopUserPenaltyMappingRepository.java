package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserPenaltyMapping;
import com.salary.plus.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopUserPenaltyMappingRepository extends JpaRepository<ShopUserPenaltyMapping, Long> {}
