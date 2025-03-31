package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserToUserMapping;
import com.salary.plus.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopUserToUserMappingRepository extends JpaRepository<ShopUserToUserMapping, Long> {
    Optional<ShopUserToUserMapping> findByShopIdAndTargetUserId(Long shopId, Long targetUserId);

    @Query("SELECT s.targetUserId FROM ShopUserToUserMapping s WHERE s.shopId = :shopId AND s.userId = :userId")
    List<Long> findAllTargetUserIdByShopIdAndUserId(@Param("shopId") Long shopId, @Param("userId") Long userId);

    void deleteByShopIdAndTargetUserId(Long shopId, Long targetUserId);
}
