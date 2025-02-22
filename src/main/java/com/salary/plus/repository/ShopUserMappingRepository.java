package com.salary.plus.repository;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.User;
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
        select sum
            from User u
            inner join ShopUserMapping sum on u.id = sum.userId
            where sum.shopId = :shopId
            and u.login = :login
        """
    )
    Optional<ShopUserMapping> findByShopIdAndLogin(Long shopId, String login);
}
