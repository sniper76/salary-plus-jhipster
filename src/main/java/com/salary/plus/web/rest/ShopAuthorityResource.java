package com.salary.plus.web.rest;

import com.salary.plus.domain.Authority;
import com.salary.plus.guard.UseGuards;
import com.salary.plus.guard.UserGuard;
import com.salary.plus.repository.AuthorityRepository;
import com.salary.plus.service.ShopAuthorityService;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Authority}.
 */
@RestController
@RequestMapping("/api/shops")
@UseGuards({ UserGuard.class })
@RequiredArgsConstructor
public class ShopAuthorityResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShopAuthorityResource.class);

    private final ShopAuthorityService shopAuthorityService;

    /**
     * {@code GET  /authorities} : get all the authorities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authorities in body.
     */
    @GetMapping("/authorities")
    public List<Authority> getAllAuthorities() {
        LOG.debug("REST request to get all Authorities");
        return shopAuthorityService.findAllExceptAdmin();
    }
}
