package com.salary.plus.service.handler;

import com.salary.plus.domain.ShopPenalty;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultShopPenaltyHandler implements PenaltyHandler {

    @Override
    public boolean supports(Long shopId) {
        return !getSupportShopIds().contains(shopId);
    }

    @Override
    public Integer process(Long shopId, ShopPenalty shopPenalty) {
        return null;
    }
}
