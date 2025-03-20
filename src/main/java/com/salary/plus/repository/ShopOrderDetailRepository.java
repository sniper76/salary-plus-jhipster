package com.salary.plus.repository;

import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopOrderDetailRepository extends JpaRepository<ShopOrderDetail, Long> {
    Optional<ShopOrderDetail> findByIdAndShopOrderId(Long orderDetailId, Long orderId);
}
