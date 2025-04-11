package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopSalaryDTO;
import com.salary.plus.service.dto.ShopSalesDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopUserDailySalaryRepository extends JpaRepository<ShopUserDailySalary, Long> {
    Optional<ShopUserDailySalary> findByShopIdAndDateAndUserIdAndActivated(Long shopId, String date, Long userId, boolean activated);

    List<ShopUserDailySalary> findAllByShopIdAndDate(Long shopId, String date);

    List<ShopUserDailySalary> findAllByShopIdAndDateAndUserId(Long shopId, String date, Long userId);

    @Query(
        value = """
            select jsuds.date,
                   sum(jsuds.price),
                   sum(jsuds.price)
            from jhi_user ju
                inner join jhi_shop_user_mapping jsum on ju.id = jsum.user_id
                inner join jhi_shop js on jsum.shop_id = js.id
                inner join jhi_shop_user_daily_salary jsuds on ju.id = jsuds.user_id
                inner join jhi_shop_user_base_salary_mapping jsubsm on jsuds.user_id = jsubsm.user_id
            where jsuds.date between '2025-03-01' and '2025-04-30'
              and js.id = 1
            group by jsuds.date
            order by jsuds.date
        """,
        nativeQuery = true
    )
    List<ShopSalaryDTO> findAllByShopIdAndSearchDate(Long shopId, String startDate, String endDate);
}
