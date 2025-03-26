package com.salary.plus.repository;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopUserSalaryBaseMappingResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<ShopBaseSalary> findAllByShopIdAndActivated(Long shopId, boolean activated);

    Page<ShopBaseSalary> findAllByShopId(Long shopId, Pageable pageable);

    Optional<ShopBaseSalary> findByIdAndShopIdAndActivated(long shopBaseSalaryId, Long shopId, boolean activated);

    @Query(
        """
            select new com.salary.plus.service.dto.ShopUserSalaryBaseMappingResponse(sbs, subsm)
            from ShopUserBaseSalaryMapping subsm
            inner join ShopBaseSalary sbs on subsm.shopBaseSalaryId = sbs.id
            where sbs.shopId = :shopId
            and subsm.userId = :userId
            and subsm.activated = :activated
            and sbs.activated = :activated
        """
    )
    Optional<ShopUserSalaryBaseMappingResponse> findBaseMappingResponseByShopIdAndUserId(Long shopId, Long userId, boolean activated);
}
