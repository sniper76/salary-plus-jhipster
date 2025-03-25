package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopUserDailySalaryRepository extends JpaRepository<ShopUserDailySalary, Long> {
    Optional<ShopUserDailySalary> findByShopIdAndDateAndUserIdAndActivated(Long shopId, String date, Long userId, boolean activated);

    List<ShopUserDailySalary> findAllByShopIdAndDate(Long shopId, String date);

    List<ShopUserDailySalary> findAllByShopIdAndDateAndUserId(Long shopId, String date, Long userId);
}
