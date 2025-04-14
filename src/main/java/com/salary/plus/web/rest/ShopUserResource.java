package com.salary.plus.web.rest;

import com.salary.plus.domain.User;
import com.salary.plus.guard.ShopGuard;
import com.salary.plus.guard.UseGuards;
import com.salary.plus.security.SecurityUtils;
import com.salary.plus.service.ShopUserDailySalaryService;
import com.salary.plus.service.UserService;
import com.salary.plus.service.dto.ShopUserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
public class ShopUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShopUserResource.class);

    private final UserService userService;
    private final ShopUserDailySalaryService shopUserDailySalaryService;

    @GetMapping("/{shopId}/dates/{date}")
    public ResponseEntity<List<ShopUserResponse>> getAllUsers(@PathVariable("shopId") Long shopId, @PathVariable("date") String date) {
        final List<ShopUserResponse> responses = userService.getAllUsersByDate(shopId, date);
        LOG.debug("REST response : {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/{shopId}/dates/{date}/users/{userId}/absence")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAbsence(@PathVariable("shopId") Long shopId, @PathVariable("date") String date, @PathVariable("userId") Long userId) {
        LOG.debug("REST shopId : {}, date : {}, userId : {}", shopId, date, userId);
        final String login = SecurityUtils.getLoginNoneNull();
        shopUserDailySalaryService.createAbsence(shopId, date, userId, login);
    }

    @PostMapping("/{shopId}/dates/{date}/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCheckIn(@PathVariable("shopId") Long shopId, @PathVariable("date") String date, @PathVariable("userId") Long userId) {
        LOG.debug("REST shopId : {}, date : {}, userId : {}", shopId, date, userId);
        final String login = SecurityUtils.getLoginNoneNull();
        shopUserDailySalaryService.createCheckIn(shopId, date, userId, login);
    }

    @PostMapping("/{shopId}/dates/{date}/users/all")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAllCheckIn(@PathVariable("shopId") Long shopId, @PathVariable("date") String date) {
        LOG.debug("REST shopId : {}, date : {}", shopId, date);
        final String login = SecurityUtils.getLoginNoneNull();
        shopUserDailySalaryService.createAllCheckIn(shopId, date, login);
    }

    @PostMapping("/{shopId}/dates/{date}/users/half-salaries/all")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateAllHalfSalary(@PathVariable("shopId") Long shopId, @PathVariable("date") String date) {
        LOG.debug("REST shopId : {}, date : {}", shopId, date);
        shopUserDailySalaryService.updateAllHalfSalary(shopId, date);
    }

    @PostMapping("/{shopId}/dates/{date}/users/half-salaries/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateHalfSalary(
        @PathVariable("shopId") Long shopId,
        @PathVariable("date") String date,
        @PathVariable("userId") Long userId
    ) {
        LOG.debug("REST shopId : {}, date : {}, userId : {}", shopId, date, userId);
        shopUserDailySalaryService.updateHalfSalary(shopId, date, userId);
    }
}
