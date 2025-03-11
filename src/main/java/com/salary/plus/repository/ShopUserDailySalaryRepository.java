package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopUserDailySalaryRepository extends JpaRepository<ShopUserDailySalary, Long> {}
