package com.salary.plus.web.rest;

import com.salary.plus.domain.User;
import com.salary.plus.guard.ShopGuard;
import com.salary.plus.guard.UseGuards;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.security.SecurityUtils;
import com.salary.plus.service.ShopModelService;
import com.salary.plus.service.UserService;
import com.salary.plus.service.dto.AdminModelDTO;
import com.salary.plus.service.dto.ModelMappingDTO;
import com.salary.plus.service.dto.ShopModelResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class ShopModelResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShopModelResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;
    private final ShopModelService shopModelService;

    @GetMapping("/{shopId}/models")
    public ResponseEntity<List<ShopModelResponse>> getAllModels(@PathVariable("shopId") Long shopId) {
        final List<ShopModelResponse> responses = userService.getAllModels(shopId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{shopId}/only-models/{userId}")
    public ResponseEntity<List<ShopModelResponse>> getAllOnlyModels(
        @PathVariable("shopId") Long shopId,
        @PathVariable("userId") Long userId
    ) {
        final List<ShopModelResponse> responses = userService.getAllOnlyModels(shopId, userId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/{shopId}/models")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> createModels(@PathVariable("shopId") Long shopId, @Valid @RequestBody AdminModelDTO modelDTO)
        throws URISyntaxException {
        final String login = SecurityUtils.getLoginNoneNull();
        modelDTO.setShopId(shopId);
        userService.createModels(login, modelDTO);
        return ResponseEntity.created(new URI("/api/shops/" + shopId + "/models"))
            .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", login))
            .build();
    }

    @PutMapping("/{shopId}/models/{userId}/mama-mappings")
    public ResponseEntity<Void> updateMamaMappings(
        @PathVariable("shopId") Long shopId,
        @PathVariable("userId") Long userId,
        @Valid @RequestBody ModelMappingDTO mappingDTO
    ) throws URISyntaxException {
        final String login = SecurityUtils.getLoginNoneNull();
        mappingDTO.setShopId(shopId);
        mappingDTO.setUserId(userId);
        LOG.debug("REST request to updateMamaMappings mappingDTO : {}", mappingDTO);
        shopModelService.updateMamaMappings(login, mappingDTO);
        return ResponseEntity.created(new URI("/api/shops/" + shopId + "/only-models/" + userId))
            .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", login))
            .build();
    }
}
