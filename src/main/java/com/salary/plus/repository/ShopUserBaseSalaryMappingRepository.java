package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserBaseSalaryMapping;
import com.salary.plus.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopUserBaseSalaryMappingRepository extends JpaRepository<ShopUserBaseSalaryMapping, Long> {
    Optional<ShopUserBaseSalaryMapping> findByShopBaseSalaryIdAndUserId(Long shopBaseSalaryId, Long userId);

    @Query(
        """
            select subsm
            from User u
            inner join ShopUserMapping sums on u.id = sums.userId
            inner join Shop s on sums.shopId = s.id
            inner join ShopBaseSalary sbs on s.id = sbs.shopId
            inner join ShopUserBaseSalaryMapping subsm on sbs.id = subsm.shopBaseSalaryId and u.id = subsm.userId
            where s.id = :shopId
            and u.id = :userId
        """
    )
    Optional<ShopUserBaseSalaryMapping> findByShopIdAndUserId(Long shopId, Long userId);
}
