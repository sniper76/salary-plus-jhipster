package com.salary.plus.repository;

import com.salary.plus.domain.ShopPenalty;
import com.salary.plus.domain.ShopTable;
import com.salary.plus.domain.User;
import com.salary.plus.service.dto.ShopTableResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link ShopTable} entity.
 */
@Repository
public interface ShopTableRepository extends JpaRepository<ShopTable, Long> {
    @Query(
        """
            select new com.salary.plus.service.dto.ShopTableResponse(st)
            from ShopTable st
            where st.shopId = :shopId
            and st.activated = :activated
        """
    )
    List<ShopTableResponse> findAllByShopIdAndActivated(Long shopId, boolean activated);
}
