package com.salary.plus.repository;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopUserBaseSalaryMapping;
import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopBaseSalaryRepository extends JpaRepository<ShopBaseSalary, Long> {
    @Query(
        """
            select sbs
            from ShopUserBaseSalaryMapping subsm
            inner join ShopBaseSalary sbs on subsm.shopBaseSalaryId = sbs.id
            where sbs.shopId = :shopId
            and subsm.userId = :userId
            and subsm.activated = :activated
            and sbs.activated = :activated
        """
    )
    Optional<ShopBaseSalary> findByShopIdAndUserId(Long shopId, Long userId, boolean activated);
}
