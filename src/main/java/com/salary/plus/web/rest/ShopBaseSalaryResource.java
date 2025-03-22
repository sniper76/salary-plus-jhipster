package com.salary.plus.web.rest;

import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.User;
import com.salary.plus.guard.ShopGuard;
import com.salary.plus.guard.UseGuards;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.security.SecurityUtils;
import com.salary.plus.service.ShopBaseSalaryService;
import com.salary.plus.service.dto.ShopBaseSalaryDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

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
public class ShopBaseSalaryResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "nameKo", "nameEn", "price", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate")
    );

    private static final Logger LOG = LoggerFactory.getLogger(ShopBaseSalaryResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopBaseSalaryService shopBaseSalaryService;

    @PostMapping("/{shopId}/base-salaries")
    public ResponseEntity<ShopBaseSalary> create(
        @PathVariable("shopId") Long shopId,
        @Valid @RequestBody ShopBaseSalaryDTO shopBaseSalaryDTO
    ) throws URISyntaxException {
        final String login = SecurityUtils.getLoginNoneNull();
        shopBaseSalaryDTO.setLogin(login);
        LOG.debug("REST request to create shopBaseSalaryDTO : {}", shopBaseSalaryDTO);
        ShopBaseSalary newUser = shopBaseSalaryService.create(shopId, shopBaseSalaryDTO);
        return ResponseEntity.created(new URI("/api/shops/" + shopId + "/base-salaries"))
            .headers(HeaderUtil.createAlert(applicationName, "baseSalary.created", login))
            .body(newUser);
    }

    @PutMapping("/{shopId}/base-salaries")
    public ResponseEntity<ShopBaseSalary> update(
        @PathVariable("shopId") Long shopId,
        @Valid @RequestBody ShopBaseSalaryDTO shopBaseSalaryDTO
    ) {
        final String login = SecurityUtils.getLoginNoneNull();
        shopBaseSalaryDTO.setLogin(login);
        LOG.debug("REST request to update shopBaseSalaryDTO : {}", shopBaseSalaryDTO);
        Optional<ShopBaseSalary> newUser = shopBaseSalaryService.update(shopId, shopBaseSalaryDTO);
        return ResponseUtil.wrapOrNotFound(newUser, HeaderUtil.createAlert(applicationName, "baseSalary.updated", login));
    }

    @DeleteMapping("/{shopId}/base-salaries/{shopBaseSalaryId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> delete(@PathVariable("shopId") Long shopId, @PathVariable("shopBaseSalaryId") long shopBaseSalaryId) {
        LOG.debug("REST request to get ShopId : {}, ShopBaseSalaryId : {}", shopId, shopBaseSalaryId);
        final String login = SecurityUtils.getLoginNoneNull();
        shopBaseSalaryService.delete(shopId, shopBaseSalaryId);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "baseSalary.deleted", login)).build();
    }

    @GetMapping("/{shopId}/base-salaries/all")
    public ResponseEntity<List<ShopBaseSalary>> getAllSalesItems(@PathVariable("shopId") Long shopId) {
        LOG.debug("REST request to get all sales item for an admin");
        final List<ShopBaseSalary> items = shopBaseSalaryService.getAllBaseSalaries(shopId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{shopId}/base-salaries/{shopBaseSalaryId}")
    public ResponseEntity<ShopBaseSalary> getShopShopBaseSalary(
        @PathVariable("shopId") Long shopId,
        @PathVariable("shopBaseSalaryId") long shopBaseSalaryId
    ) {
        LOG.debug("REST request to get ShopId : {}, ShopBaseSalaryId : {}", shopId, shopBaseSalaryId);
        return ResponseUtil.wrapOrNotFound(shopBaseSalaryService.get(shopId, shopBaseSalaryId));
    }

    @GetMapping("/{shopId}/base-salaries")
    public ResponseEntity<List<ShopBaseSalary>> getAllPenaltiesForPage(
        @PathVariable("shopId") Long shopId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST shopId: {}, pageable: {}", shopId, pageable);
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<ShopBaseSalary> page = shopBaseSalaryService.getAllBaseSalaries(shopId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }
}
