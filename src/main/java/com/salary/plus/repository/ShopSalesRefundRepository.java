package com.salary.plus.repository;

import com.salary.plus.domain.ShopBonusSalary;
import com.salary.plus.domain.ShopSalesRefund;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopOrderRefundResponse;
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
public interface ShopSalesRefundRepository extends JpaRepository<ShopSalesRefund, Long> {
    Optional<ShopSalesRefund> findByShopIdAndDateAndOrderId(Long shopId, String date, Long orderId);
}
