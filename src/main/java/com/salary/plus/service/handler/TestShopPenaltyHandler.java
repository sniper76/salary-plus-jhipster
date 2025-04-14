package com.salary.plus.service.handler;

import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopPenalty;
import com.salary.plus.service.ShopService;
import com.salary.plus.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TestShopPenaltyHandler implements PenaltyHandler {

    private final ShopService shopService;

    @Override
    public boolean supports(Long shopId) {
        return getSupportShopIds().contains(shopId);
    }

    @Override
    public Integer process(Long shopId, ShopPenalty shopPenalty) {
        final Shop shop = shopService.get(shopId).orElseThrow();
        final String workStartTime = shop.getWorkStartTime();
        final long duration = DateUtils.getDuration(workStartTime, shopPenalty.getTypeValue());
        return (int) duration * shopPenalty.getPrice();
    }
}
