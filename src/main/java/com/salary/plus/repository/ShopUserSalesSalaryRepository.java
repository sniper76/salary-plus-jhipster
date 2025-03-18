package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserSalesSalary;
import com.salary.plus.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopUserSalesSalaryRepository extends JpaRepository<ShopUserSalesSalary, Long> {
    Optional<ShopUserSalesSalary> findByShopOrderDetailId(Long shopOrderDetailId);
}
