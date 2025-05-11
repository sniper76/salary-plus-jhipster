package com.salary.plus.service;

import com.salary.plus.service.dto.ShopSalaryDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class EntityManagerService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ShopSalaryDTO> findAllByShopIdAndSearchDate(Long shopId, String startDate, String endDate) {
        String sql =
            "SELECT temp.id as user_id," +
            "       temp.model_no, temp.first_name, temp.last_name," +
            "       sum(jsuds.price) as salary_price," +
            "       sum(spm_day.price) as day_penalty_price," +
            "       sum(spm_time.price) as time_penalty_price " +
            "FROM (" +
            "     SELECT to_char(date, 'YYYY-MM-DD') AS date," +
            "            upper(to_char(date, 'Dy')) as day_of_week," +
            "            ju.id, ju.model_no, ju.first_name, ju.last_name " +
            "     FROM generate_series(date '" +
            startDate +
            "', date '" +
            endDate +
            "', interval '1 day') as temp(date) " +
            "     cross join jhi_user ju " +
            "     where ju.commission_target_yn = true" +
            " ) AS temp " +
            " LEFT OUTER JOIN jhi_shop_user_daily_salary jsuds" +
            "                 ON temp.date = jsuds.date and temp.id = jsuds.user_id " +
            "            left outer join " +
            "    ( " +
            "        select supm.user_id, sp.type, supm.price, supm.date " +
            "    from jhi_shop_penalty sp " +
            "    inner join jhi_shop_user_penalty_mapping supm on sp.id = supm.shop_penalty_id " +
            "    where sp.shop_id = :shopId " +
            "   and supm.date between :startDate AND :endDate " +
            "   and sp.type = 'TIME' " +
            "    order by supm.date, supm.user_id " +
            " ) spm_time on temp.id = spm_time.user_id and temp.date = spm_time.date " +
            "    left outer join " +
            "        ( " +
            "            select supm.user_id, sp.type, supm.price, supm.date " +
            "            from jhi_shop_penalty sp " +
            "            inner join jhi_shop_user_penalty_mapping supm on sp.id = supm.shop_penalty_id " +
            "            where sp.shop_id = :shopId " +
            "           and supm.date between :startDate AND :endDate " +
            "    and sp.type in ('MANDATORY', 'DAY') " +
            "    order by supm.date, supm.user_id " +
            " ) spm_day on temp.id = spm_day.user_id and temp.date = spm_day.date " +
            "WHERE temp.date BETWEEN :startDate AND :endDate " +
            "  AND jsuds.shop_id = :shopId " +
            "GROUP BY temp.id, temp.model_no, temp.first_name, temp.last_name";

        List<Object[]> resultList = entityManager
            .createNativeQuery(sql)
            .setParameter("shopId", shopId)
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .getResultList();

        return resultList
            .stream()
            .map(row ->
                new ShopSalaryDTO(
                    toLong(row[0]), // userId
                    (String) row[1], // modelNo
                    (String) row[2], // firstName
                    (String) row[3], // lastName
                    toLong(row[4]), // salaryPrice
                    toLong(row[5]), // dayPenaltyPrice
                    toLong(row[6]) // timePenaltyPrice
                )
            )
            .toList();
    }

    private Long toLong(Object obj) {
        if (obj == null) return 0L;
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).longValue();
        } else if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).longValue();
        } else if (obj instanceof Number) {
            return ((Number) obj).longValue(); // covers Long, Integer, Double 등
        } else {
            throw new IllegalArgumentException("Unexpected type: " + obj.getClass());
        }
    }
}
