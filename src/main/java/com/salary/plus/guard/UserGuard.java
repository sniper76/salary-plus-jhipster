package com.salary.plus.guard;

import com.salary.plus.security.SecurityUtils;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserGuard implements UseGuard {

    public void canActivate(Map<String, Object> parameterMap) throws RuntimeException {
        final Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new BadRequestAlertException("Not found user", "userManagement", "idexists");
        }
    }
}
