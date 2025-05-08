package com.salary.plus.repository;

import com.salary.plus.domain.Authority;
import com.salary.plus.domain.User;
import com.salary.plus.enums.PenaltyType;
import com.salary.plus.service.dto.ShopModelResponse;
import com.salary.plus.service.dto.ShopUserResponse;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    Optional<User> findOneByActivationKey(String activationKey);
    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Optional<User> findOneByResetKey(String resetKey);
    Optional<User> findOneByEmailIgnoreCase(String email);
    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE, unless = "#result == null")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE, unless = "#result == null")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    @Query(
        """
            select a.name
            from User u
            inner join u.authorities a
            where u.login = :login
        """
    )
    List<String> findAuthoritiesByLogin(String login);

    @Query(
        """
            select new com.salary.plus.service.dto.ShopModelResponse(u)
            from Shop s
            inner join ShopUserMapping sump on s.id = sump.shopId
            inner join User u on sump.userId = u.id
            inner join u.authorities ua
            where s.id = :shopId
            and u.activated = :activated
            and u.commissionTargetYn = :commissionTargetYn
            and ua.name in :authorities
        """
    )
    List<ShopModelResponse> findAllByShopId(Long shopId, boolean activated, boolean commissionTargetYn, List<String> authorities);

    @Query(
        """
            select new com.salary.plus.service.dto.ShopModelResponse(u, sutum)
            from User u
            inner join ShopUserMapping sump on u.id = sump.userId
            inner join Shop s on s.id = sump.shopId
            inner join u.authorities ua
            left outer join ShopUserToUserMapping sutum on s.id = sutum.shopId and u.id = sutum.targetUserId and sutum.userId = :userId
            where s.id = :shopId
            and u.activated = :activated
            and u.commissionTargetYn = :commissionTargetYn
            and ua.name in :authorities
        """
    )
    List<ShopModelResponse> findAllModelsByShopIdAndMamaId(
        Long shopId,
        Long userId,
        boolean activated,
        boolean commissionTargetYn,
        List<String> authorities
    );

    @Query(
        """
        select u
        from User u
        where exists (
            select 1
            from Shop s
            inner join ShopUserMapping sump on s.id = sump.shopId
            where s.id = :shopId
            and sump.userId = u.id
        )
        and not exists (
            select 1
            from u.authorities a
            where a.name = :roleName
        )
        """
    )
    Page<User> findAllByShopIdExcludingRole(Long shopId, String roleName, Pageable pageable);

    @Query(
        value = """
        select u.id as user_id, u.first_name, u.last_name, u.model_no,
        case when suds.id is null then false else true end as is_check_in,
        case when spm.user_id is null then false else true end as is_absence
        from jhi_shop s
        inner join jhi_shop_user_mapping sump on s.id = sump.shop_id
        inner join jhi_user u on sump.user_id = u.id
        left outer join jhi_shop_user_daily_salary suds on u.id = suds.user_id and suds.date = :date
        left outer join
        (
            select distinct supm.user_id
                from jhi_shop_penalty sp
            inner join jhi_shop_user_penalty_mapping supm on sp.id = supm.shop_penalty_id
            where sp.shop_id = :shopId
                and sp.type in :types
                and supm.date = :date
        ) spm on u.id = spm.user_id
        where s.id = :shopId
            and u.activated = :activated
            and u.commission_target_yn = :commissionTargetYn
        order by u.model_no
        """,
        nativeQuery = true
    )
    List<ShopUserResponse> findAllByShopIdAndDate(
        Long shopId,
        String date,
        boolean activated,
        boolean commissionTargetYn,
        List<String> types
    );
}
