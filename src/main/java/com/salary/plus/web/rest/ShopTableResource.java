package com.salary.plus.web.rest;

import com.salary.plus.domain.User;
import com.salary.plus.guard.ShopGuard;
import com.salary.plus.guard.UseGuards;
import com.salary.plus.service.ShopTableService;
import com.salary.plus.service.UserService;
import com.salary.plus.service.dto.ShopModelResponse;
import com.salary.plus.service.dto.ShopTableResponse;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class ShopTableResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShopTableResource.class);

    private final ShopTableService shopTableService;

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
    @GetMapping("/{shopId}/tables")
    public ResponseEntity<List<ShopTableResponse>> getAllTables(@PathVariable("shopId") Long shopId) {
        final List<ShopTableResponse> responses = shopTableService.getAllTables(shopId);
        LOG.debug("REST response : {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
