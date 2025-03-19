package com.salary.plus.web.rest;

import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.User;
import com.salary.plus.guard.ShopGuard;
import com.salary.plus.guard.UseGuards;
import com.salary.plus.security.SecurityUtils;
import com.salary.plus.service.ShopOrderService;
import com.salary.plus.service.dto.ShopOrderCreateDTO;
import com.salary.plus.service.dto.ShopOrderDetailResponse;
import com.salary.plus.service.dto.ShopOrderResponse;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

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
public class ShopOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShopOrderResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopOrderService shopOrderService;

    /**
     * {@code POST  /api/shops/shopId/orders/orderId}  : Get a shop orders.
     * <p>
     * Creates a new user if the login and email are not already used, and sends a
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @GetMapping("/{shopId}/dates/{date}/orders")
    public ResponseEntity<List<ShopOrderResponse>> getAllOrders(@PathVariable("shopId") Long shopId, @PathVariable("date") String date) {
        final List<ShopOrderResponse> responses = shopOrderService.getAllOrdersByDate(shopId, date);
        LOG.debug("REST response : {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{shopId}/dates/{date}/orders/{orderId}")
    public ResponseEntity<List<ShopOrderDetailResponse>> getOrderDetails(
        @PathVariable("shopId") Long shopId,
        @PathVariable("date") String date,
        @PathVariable("orderId") Long orderId
    ) {
        final List<ShopOrderDetailResponse> responses = shopOrderService.getOrderDetails(shopId, date, orderId);
        LOG.debug("REST response : {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/{shopId}/dates/{date}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(
        @PathVariable("shopId") Long shopId,
        @PathVariable("date") String date,
        @Valid @RequestBody ShopOrderCreateDTO shopOrderCreateDTO
    ) {
        LOG.debug("REST shopId : {}, date : {}, shopOrderCreateDTO : {}", shopId, date, shopOrderCreateDTO);
        shopOrderService.createOrder(shopId, date, shopOrderCreateDTO);
    }

    @PatchMapping("/{shopId}/dates/{date}")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateOrder(
        @PathVariable("shopId") Long shopId,
        @PathVariable("date") String date,
        @Valid @RequestBody ShopOrderCreateDTO shopOrderCreateDTO
    ) {
        LOG.debug("REST shopId : {}, date : {}, shopOrderCreateDTO : {}", shopId, date, shopOrderCreateDTO);
        shopOrderService.updateOrder(shopId, date, shopOrderCreateDTO);
    }

    @PutMapping("/{shopId}/dates/{date}/orders/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateOrderPaid(
        @PathVariable("shopId") Long shopId,
        @PathVariable("date") String date,
        @PathVariable("orderId") Long orderId
    ) {
        LOG.debug("REST shopId : {}, date : {}, orderId : {}", shopId, date, orderId);
        shopOrderService.updateOrderPaid(shopId, date, orderId);
    }

    @DeleteMapping("/orders/{orderId}/details/{orderDetailId}")
    public ResponseEntity<Void> deleteOrderDetail(
        @PathVariable("orderId") Long orderId,
        @PathVariable("orderDetailId") Long orderDetailId
    ) {
        final Optional<ShopOrderDetail> detail = shopOrderService.getOrderDetail(orderId, orderDetailId);
        if (detail.isEmpty()) {
            throw new BadRequestAlertException("Not found target", "orders", "order.not.found.target");
        }
        final ShopOrderDetail shopOrderDetail = detail.get();
        final String loginUser = SecurityUtils.getLoginNoneNull();
        shopOrderDetail.delete(loginUser);
        shopOrderService.deleteOrderDetail(shopOrderDetail);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createAlert(applicationName, "order.deleted", shopOrderDetail.getLastModifiedBy()))
            .build();
    }
}
