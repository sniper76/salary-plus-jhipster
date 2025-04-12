package com.salary.plus.repository;

import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopSalaryDTO;
import com.salary.plus.service.dto.ShopSalesDTO;
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
public interface ShopUserDailySalaryRepository extends JpaRepository<ShopUserDailySalary, Long> {
    Optional<ShopUserDailySalary> findByShopIdAndDateAndUserIdAndActivated(Long shopId, String date, Long userId, boolean activated);

    List<ShopUserDailySalary> findAllByShopIdAndDate(Long shopId, String date);

    List<ShopUserDailySalary> findAllByShopIdAndDateAndUserId(Long shopId, String date, Long userId);

    @Query(
        value = """
        SELECT temp.id as user_id,
               temp.model_no, temp.first_name, temp.last_name,
               sum(jsuds.price) as salary_price,
               sum(case when jsuds.price is null then (
                   select price
                   from jhi_shop_penalty
                   where shop_id = 1
                     and type = 'DAY'
                     and activated = true
                   and type_value like '%'||temp.day_of_week||'%'
                   ) end) as day_penalty_price,
               sum(case when jsuds.price is not null then (
                    select case when type_value = '1M' then trunc(EXTRACT(EPOCH FROM (now() - jsuds.created_date)) / 60) * jsuds.price
                                when type_value = '1H' then trunc(EXTRACT(EPOCH FROM (now() - jsuds.created_date)) / 3600) * jsuds.price end
                   from jhi_shop_penalty
                   where shop_id = 1
                     and type = 'TIME'
                     and activated = true
               ) end) as time_penalty_price
        FROM (
             SELECT to_char(date, 'YYYY-MM-DD') AS date,
                    upper(to_char(date, 'Dy')) as day_of_week,
                    ju.id, ju.model_no, ju.first_name, ju.last_name
             FROM generate_series(date :startDate, date :endDate, interval '1 day') as temp(date)
             cross join jhi_user ju
             where ju.commission_target_yn = true
         ) AS temp
         LEFT OUTER JOIN jhi_shop_user_daily_salary jsuds
                         ON temp.date = jsuds.date and temp.id = jsuds.user_id
        WHERE temp.date BETWEEN :startDate AND :endDate
        and jsuds.shop_id = :shopId
        group by temp.id,
                 temp.model_no, temp.first_name, temp.last_name
        """,
        nativeQuery = true
    )
    List<ShopSalaryDTO> findAllByShopIdAndSearchDate(
        @Param("shopId") Long shopId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );
}
