package com.salary.plus.repository;

import com.salary.plus.domain.ShopBonusSalary;
import com.salary.plus.domain.ShopPenalty;
import com.salary.plus.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopBonusSalaryRepository extends JpaRepository<ShopBonusSalary, Long> {
    List<ShopBonusSalary> findAllByShopIdAndActivated(Long shopId, boolean activated);

    Page<ShopBonusSalary> findAllByShopId(Long shopId, Pageable pageable);

    Optional<ShopBonusSalary> findByIdAndShopIdAndActivated(long shopBonusSalaryId, Long shopId, boolean activated);
}
