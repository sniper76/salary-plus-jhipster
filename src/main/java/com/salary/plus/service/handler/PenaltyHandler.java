package com.salary.plus.service.handler;

import com.salary.plus.domain.ShopPenalty;
import com.salary.plus.utils.SetUtils;
import java.util.Set;

public interface PenaltyHandler {
    default Set<Long> getSupportShopIds() {
        return SetUtils.immutableSet(1L);
    }

    boolean supports(Long shopId);

    Integer process(Long shopId, ShopPenalty shopPenalty);
}
