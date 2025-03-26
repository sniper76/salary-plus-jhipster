package com.salary.plus.repository;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopUserMapping;
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
public interface ShopUserMappingRepository extends JpaRepository<ShopUserMapping, Long> {
    @Query(
        """
        select suma
            from User u
            inner join ShopUserMapping suma on u.id = suma.userId
            inner join Shop s on suma.shopId = s.id
            where s.id = :shopId
            and u.login = :login
        """
    )
    Optional<ShopUserMapping> findByShopIdAndLogin(Long shopId, String login);

    @Query(
        """
        select u
            from User u
            inner join ShopUserMapping suma on u.id = suma.userId
            inner join Shop s on suma.shopId = s.id
            where s.id = :shopId
            and u.activated = :activated
            and s.activated = :activated
        """
    )
    List<User> findAllUserByShopIdAndActivated(Long shopId, boolean activated);

    @Query(
        """
        select u
            from User u
            inner join ShopUserMapping suma on u.id = suma.userId
            inner join Shop s on suma.shopId = s.id
            where s.id = :shopId
            and u.activated = :activated
            and s.activated = :activated
            and u.commissionTargetYn = :commissionTargetYn
        """
    )
    List<User> findAllUserByShopIdAndActivatedAndCommissionTarget(Long shopId, boolean activated, boolean commissionTargetYn);

    List<ShopUserMapping> findAllByUserId(Long userId);

    @Query(
        """
        select s
            from Shop s
            inner join ShopUserMapping suma on s.id = suma.shopId
            inner join User u on suma.userId = u.id
            where u.login = :login
            and u.activated = :activated
            and s.activated = :activated
        """
    )
    List<Shop> findAllShopByLoginAndActivated(String login, boolean activated);

    void deleteByUserIdAndShopId(Long userId, Long shopId);

    @Query(
        """
            select new com.salary.plus.service.dto.ShopUserSalaryBaseMappingResponse(u)
            from User u
            inner join ShopUserMapping suma on u.id = suma.userId
            inner join Shop s on suma.shopId = s.id
            where s.id = :shopId
            and u.commissionTargetYn = :commissionTargetYn
            and u.activated = :activated
            and suma.activated = :activated
            and s.activated = :activated
        """
    )
    Page<ShopUserSalaryBaseMappingResponse> findAllByShopIdAndCommissionTargetAndActivated(
        Long shopId,
        boolean commissionTargetYn,
        boolean activated,
        Pageable pageable
    );
}
