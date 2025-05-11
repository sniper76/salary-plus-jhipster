package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopSalaryDTO;
import com.salary.plus.service.dto.ShopSalaryDetailDTO;
import com.salary.plus.service.dto.ShopSalesDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
            select suds.date,
                   suds.price as salary_price,
                   spm.price as penalty_price
            from jhi_shop s
            inner join jhi_shop_user_mapping sump on s.id = sump.shop_id
            inner join jhi_user u on sump.user_id = u.id
            inner join jhi_shop_user_daily_salary suds on u.id = suds.user_id
            left outer join
            (
                select supm.user_id, sp.type, supm.price, supm.date
                from jhi_shop_penalty sp
                inner join jhi_shop_user_penalty_mapping supm on sp.id = supm.shop_penalty_id
                where sp.shop_id = :shopId
                  and supm.date between :startDate and :endDate
                order by supm.date, supm.user_id
            ) spm on u.id = spm.user_id and suds.date = spm.date
            where s.id = :shopId
            and u.id = :userId
            and suds.date between :startDate and :endDate
            order by suds.date
        """,
        nativeQuery = true
    )
    List<ShopSalaryDetailDTO> findAllByShopIdAndUserIdAndSearchDate(Long shopId, Long userId, String startDate, String endDate);
}
