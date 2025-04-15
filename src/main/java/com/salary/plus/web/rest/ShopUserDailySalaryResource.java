package com.salary.plus.web.rest;

import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.User;
import com.salary.plus.guard.ShopGuard;
import com.salary.plus.guard.UseGuards;
import com.salary.plus.security.SecurityUtils;
import com.salary.plus.service.EntityManagerService;
import com.salary.plus.service.ShopUserDailySalaryService;
import com.salary.plus.service.dto.AdminUserDTO;
import com.salary.plus.service.dto.ShopDailySalaryDTO;
import com.salary.plus.service.dto.ShopSalaryDTO;
import com.salary.plus.service.dto.ShopSalaryDetailDTO;
import com.salary.plus.service.dto.ShopSalesDTO;
import com.salary.plus.service.dto.UserDTO;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
@UseGuards({ ShopGuard.class })
@RequestMapping("/api/shops")
public class ShopUserDailySalaryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShopUserDailySalaryResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopUserDailySalaryService shopDailySalaryService;
    private final EntityManagerService entityManagerService;

    /**
     * {@code POST  /api/shops}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends a
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param shopSalesItemDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/{shopId}/daily-salaries")
    public ResponseEntity<UserDTO> createDailySalaries(
        @PathVariable("shopId") Long shopId,
        @Valid @RequestBody ShopDailySalaryDTO shopSalesItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save User : {}", shopSalesItemDTO);
        final String login = SecurityUtils.getLoginNoneNull();
        List<ShopUserDailySalary> newUser = shopDailySalaryService.create(shopSalesItemDTO, login);
        final UserDTO userDTO = new UserDTO();
        userDTO.setLogin(login);
        userDTO.setSalaries(newUser);
        return ResponseEntity.created(new URI("/api/shops/" + shopId + "/daily-salaries"))
            .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", userDTO.getLogin()))
            .body(userDTO);
    }

    @GetMapping("/{shopId}/daily-salaries/{startDate}/{endDate}")
    public ResponseEntity<List<ShopSalaryDTO>> getAllDailySalaries(
        @PathVariable("shopId") Long shopId,
        @PathVariable("startDate") String startDate,
        @PathVariable("endDate") String endDate
    ) {
        final List<ShopSalaryDTO> items = entityManagerService.findAllByShopIdAndSearchDate(shopId, startDate, endDate);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{shopId}/daily-salaries/users/{userId}/{startDate}/{endDate}")
    public ResponseEntity<List<ShopSalaryDetailDTO>> getAllDailySalariesForPage(
        @PathVariable("shopId") Long shopId,
        @PathVariable("userId") Long userId,
        @PathVariable("startDate") String startDate,
        @PathVariable("endDate") String endDate
    ) {
        LOG.debug("REST getAllDailySalariesForPage : {}", shopId);
        final List<ShopSalaryDetailDTO> items = shopDailySalaryService.getAllDailySalariesForPage(shopId, userId, startDate, endDate);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
