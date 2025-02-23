package com.salary.plus.guard;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.User;
import com.salary.plus.security.SecurityUtils;
import com.salary.plus.service.ShopUserMappingService;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@RequiredArgsConstructor
public class ShopGuard implements UseGuard {

    private final ShopUserMappingService shopUserMappingService;

    public void canActivate(Map<String, Object> parameterMap) throws RuntimeException {
        final Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new BadRequestAlertException("Not found user", "userManagement", "idexists");
        }
        final Long shopId = (Long) parameterMap.get("shopId");
        final Optional<ShopUserMapping> mappingByLogin = shopUserMappingService.getShopUserMappingByLogin(shopId, login.get());
        if (mappingByLogin.isEmpty()) {
            throw new BadRequestAlertException("UnMatch shop and user", "userManagement", "idexists");
        }
    }
}
