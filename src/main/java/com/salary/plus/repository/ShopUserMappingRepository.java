package com.salary.plus.repository;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.User;
import java.util.List;
import java.util.Optional;
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
            where suma.shopId = :shopId
            and u.login = :login
        """
    )
    Optional<ShopUserMapping> findByShopIdAndLogin(Long shopId, String login);

    @Query(
        """
        select u
            from User u
            inner join ShopUserMapping suma on u.id = suma.userId
            where suma.shopId = :shopId
            and u.activated = true
        """
    )
    List<User> findAllUserByShopId(Long shopId);

    @Query(
        """
        select u
            from User u
            inner join ShopUserMapping suma on u.id = suma.userId
            where suma.shopId = :shopId
            and u.activated = true
            and u.isCommissionTarget = :isCommissionTarget
        """
    )
    List<User> findAllUserByShopIdAndCommissionTargetUser(Long shopId, boolean isCommissionTarget);

    List<ShopUserMapping> findAllByUserId(Long userId);

    @Query(
        """
        select s
            from Shop s
            inner join ShopUserMapping suma on s.id = suma.shopId
            inner join User u on suma.userId = u.id
            where u.login = :login
            and u.activated = true
        """
    )
    List<Shop> findAllShopByLogin(String login);

    void deleteByUserIdAndShopId(Long userId, Long shopId);
}
