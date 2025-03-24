package com.salary.plus.web.rest;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.User;
import com.salary.plus.guard.UseGuards;
import com.salary.plus.guard.UserGuard;
import com.salary.plus.security.SecurityUtils;
import com.salary.plus.service.ShopBaseSalaryService;
import com.salary.plus.service.ShopUserMappingService;
import com.salary.plus.service.dto.AdminUserSalaryMappingDTO;
import com.salary.plus.service.dto.ShopUserSalaryBaseMappingResponse;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequiredArgsConstructor
@UseGuards({ UserGuard.class })
@RequestMapping("/api")
public class ShopUserMappingResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "login", "firstName", "lastName", "price", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate")
    );

    private static final Logger LOG = LoggerFactory.getLogger(ShopUserMappingResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopUserMappingService shopUserMappingService;
    private final ShopBaseSalaryService shopBaseSalaryService;

    /**
     * {@code POST  /shops}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends a
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> getShops() {
        final Optional<String> login = SecurityUtils.getCurrentUserLogin();
        LOG.debug("getShops login : {}", login.get());
        final List<Shop> shops = shopUserMappingService.getMappingShops(login.get());
        return new ResponseEntity<>(shops, HttpStatus.OK);
    }

    @GetMapping("/shops/{shopId}/user-salary-mappings")
    public ResponseEntity<List<ShopUserSalaryBaseMappingResponse>> getAllUserSalaryMappingForPage(
        @PathVariable("shopId") Long shopId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST shopId: {}, pageable: {}", shopId, pageable);
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<ShopUserSalaryBaseMappingResponse> page = shopUserMappingService.getAllUserSalaryMapping(shopId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/shops/{shopId}/user-salary-mappings/all")
    public ResponseEntity<Void> createAllUserSalaryMappings(
        @PathVariable("shopId") Long shopId,
        @Valid @RequestBody AdminUserSalaryMappingDTO mappingDTO
    ) throws URISyntaxException {
        final String login = SecurityUtils.getLoginNoneNull();
        mappingDTO.setShopId(shopId);
        LOG.debug("REST login: {}, mappingDTO: {}", login, mappingDTO);
        shopBaseSalaryService.createUserBaseSalaryMappings(login, mappingDTO);
        return ResponseEntity.created(new URI("/api/shops/" + shopId + "/user-salary-mappings/all"))
            .headers(HeaderUtil.createAlert(applicationName, "userSalaryMapping.created", login))
            .build();
    }

    @PostMapping("/shops/{shopId}/user-salary-mappings/{userId}")
    public ResponseEntity<Void> createUserSalaryMappings(
        @PathVariable("shopId") Long shopId,
        @PathVariable("userId") Long userId,
        @Valid @RequestBody AdminUserSalaryMappingDTO mappingDTO
    ) throws URISyntaxException {
        final String login = SecurityUtils.getLoginNoneNull();
        mappingDTO.setShopId(shopId);
        mappingDTO.setUserId(userId);
        shopBaseSalaryService.createUserBaseSalaryMappings(login, mappingDTO);
        return ResponseEntity.created(new URI("/api/shops/" + shopId + "/user-salary-mappings/" + userId))
            .headers(HeaderUtil.createAlert(applicationName, "userSalaryMapping.created", login))
            .build();
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }
}
