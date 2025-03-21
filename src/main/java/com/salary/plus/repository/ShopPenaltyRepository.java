package com.salary.plus.repository;

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
public interface ShopPenaltyRepository extends JpaRepository<ShopPenalty, Long> {
    Optional<ShopPenalty> findByIdAndShopIdAndActivated(long salesItemId, Long shopId, boolean activated);

    Page<ShopPenalty> findAllByShopId(Long shopId, Pageable pageable);

    List<ShopPenalty> findAllByShopIdAndActivated(Long shopId, boolean activated);
}
