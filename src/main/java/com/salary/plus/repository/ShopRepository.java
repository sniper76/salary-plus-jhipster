package com.salary.plus.repository;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findAllByActivated(boolean activated);
}
