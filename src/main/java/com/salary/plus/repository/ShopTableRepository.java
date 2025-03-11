package com.salary.plus.repository;

import com.salary.plus.domain.ShopPenalty;
import com.salary.plus.domain.ShopTable;
import com.salary.plus.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link ShopTable} entity.
 */
@Repository
public interface ShopTableRepository extends JpaRepository<ShopTable, Long> {}
