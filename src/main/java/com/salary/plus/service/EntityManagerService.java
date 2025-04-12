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
            "       sum(case when jsuds.price is null then (" +
            "           select price" +
            "           from jhi_shop_penalty" +
            "           where shop_id = :shopId" +
            "             and type = 'DAY'" +
            "             and activated = true" +
            "             and type_value like '%'||temp.day_of_week||'%'" +
            "       ) end) as day_penalty_price," +
            "       sum(case when jsuds.price is not null then (" +
            "           select case when type_value = '1M' then trunc(EXTRACT(EPOCH FROM (now() - jsuds.created_date)) / 60) * jsuds.price" +
            "                        when type_value = '1H' then trunc(EXTRACT(EPOCH FROM (now() - jsuds.created_date)) / 3600) * jsuds.price end" +
            "           from jhi_shop_penalty" +
            "           where shop_id = :shopId" +
            "             and type = 'TIME'" +
            "             and activated = true" +
            "       ) end) as time_penalty_price " +
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
