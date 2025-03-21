package com.salary.plus.web.rest;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.User;
import com.salary.plus.guard.UseGuards;
import com.salary.plus.guard.UserGuard;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.security.SecurityUtils;
import com.salary.plus.service.ShopService;
import com.salary.plus.service.dto.AdminShopDTO;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import com.salary.plus.web.rest.errors.EmailAlreadyUsedException;
import com.salary.plus.web.rest.errors.LoginAlreadyUsedException;
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
@UseGuards({ UserGuard.class })
@RequestMapping("/api/admin")
public class ShopResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "nameKo", "nameEn", "type", "activated", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate")
    );

    private static final Logger LOG = LoggerFactory.getLogger(ShopResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopService shopService;

    /**
     * {@code POST  /admin/shops}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends a
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/shops")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Shop> create(@Valid @RequestBody AdminShopDTO userDTO) throws URISyntaxException {
        LOG.debug("REST request to save User : {}", userDTO);
        final String loginUser = SecurityUtils.getLoginNoneNull();
        Shop newUser = shopService.create(userDTO, loginUser);
        return ResponseEntity.created(new URI("/api/admin/shops/" + newUser.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "shopManagement.created", loginUser))
            .body(newUser);
    }

    /**
     * {@code PUT /admin/shops} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping({ "/shops" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Shop> update(@Valid @RequestBody AdminShopDTO userDTO) {
        LOG.debug("REST request to update Shop : {}", userDTO);
        final String loginUser = SecurityUtils.getLoginNoneNull();
        Optional<Shop> existingShop = shopService.get(userDTO.getId());
        if (existingShop.isPresent() && (!existingShop.orElseThrow().getId().equals(userDTO.getId()))) {
            throw new BadRequestAlertException("Id already exists", "shopManagement", "idexists");
        }
        Optional<Shop> updatedUser = shopService.update(userDTO, loginUser);

        return ResponseUtil.wrapOrNotFound(updatedUser, HeaderUtil.createAlert(applicationName, "shopManagement.updated", loginUser));
    }

    /**
     * {@code DELETE /admin/shops/:id} : delete the "id" Shop.
     *
     * @param id the is of the shop to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shops/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteShop(@PathVariable("id") long id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "shopManagement.deleted", "" + id)).build();
    }

    /**
     * {@code GET /admin/shops} : get all users with all the details - calling this are only allowed for the administrators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/shops")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<AdminShopDTO>> getAllShops(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get all User for an admin");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<AdminShopDTO> page = shopService.getAllManagedShops(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /admin/activated/shops} : get all the shops.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shops in body.
     */
    @GetMapping("/activated/shops")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<Shop> getAllActivatedShops() {
        LOG.debug("REST request to get all Shops");
        return shopService.getAllActivated();
    }

    /**
     * {@code GET /admin/shops/:id} : get the "id" shop.
     *
     * @param id the login of the shop to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shops/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Shop> getShop(@PathVariable("id") long id) {
        LOG.debug("REST request to get Shop : {}", id);
        return ResponseUtil.wrapOrNotFound(shopService.get(id));
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }
}
