package com.salary.plus.web.rest;

import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopUserPenaltyMapping;
import com.salary.plus.domain.User;
import com.salary.plus.guard.ShopGuard;
import com.salary.plus.guard.UseGuards;
import com.salary.plus.repository.ShopUserPenaltyMappingRepository;
import com.salary.plus.service.ShopSalesService;
import com.salary.plus.service.dto.ShopOrderDetailDTO;
import com.salary.plus.service.dto.ShopOrderDetailResponse;
import com.salary.plus.service.dto.ShopSalesDTO;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
public class ShopSalesResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "shopTableId", "totalPrice", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate")
    );

    private static final Logger LOG = LoggerFactory.getLogger(ShopSalesResource.class);

    private final ShopSalesService shopSalesService;

    /**
     * {@code GET /shops/{shopId}/sales-items} : get all users with all the details - calling this are only allowed for the administrators.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/{shopId}/sales/{startDate}/{endDate}")
    public ResponseEntity<List<ShopSalesDTO>> getAllSales(
        @PathVariable("shopId") Long shopId,
        @PathVariable("startDate") String startDate,
        @PathVariable("endDate") String endDate
    ) {
        LOG.debug("REST request to get all sales item for an admin");
        final List<ShopSalesDTO> items = shopSalesService.getAllSales(shopId, startDate, endDate);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{shopId}/sales/{startDate}/{endDate}/users/{userId}")
    public ResponseEntity<List<ShopSalesDTO>> getAllSalesByUserId(
        @PathVariable("shopId") Long shopId,
        @PathVariable("startDate") String startDate,
        @PathVariable("endDate") String endDate,
        @PathVariable("userId") Long userId
    ) {
        LOG.debug("REST request to get all sales item for an admin");
        final List<ShopSalesDTO> items = shopSalesService.getAllSalesByUserId(shopId, startDate, endDate, userId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{shopId}/sales/dates/{date}")
    public ResponseEntity<List<ShopOrder>> getSalesForPage(
        @PathVariable("shopId") Long shopId,
        @PathVariable("date") String date,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST shopId: {}, pageable: {}", shopId, pageable);
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<ShopOrder> page = shopSalesService.getSalesForPage(shopId, date, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    @GetMapping("/{shopId}/sales/orders/{orderId}")
    public ResponseEntity<List<ShopOrderDetailDTO>> getOrderDetails(
        @PathVariable("shopId") Long shopId,
        @PathVariable("orderId") Long orderId
    ) {
        final List<ShopOrderDetailDTO> responses = shopSalesService.getSalesDetails(shopId, orderId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
