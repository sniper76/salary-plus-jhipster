package com.salary.plus.service.handler;

import com.salary.plus.domain.ShopPenalty;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PenaltyHandlerResolver {

    private final List<PenaltyHandler> penaltyHandlerList;
    private final DefaultShopPenaltyHandler defaultShopPenaltyHandler;

    /*
    private final MailDeliveryStatusHandlerResolver mailDeliveryStatusHandlerResolver;

        return mailDeliveryStatusHandlerResolver
            .resolve(MailDeliveryStatus.valueOf(updateMailDeliveryStatusRequest.getMailDeliveryStatus()))
            .handle(mailDeliveryId);
     */
    public Integer resolve(Long shopId, ShopPenalty shopPenalty) {
        return penaltyHandlerList
            .stream()
            .filter(handler -> handler.supports(shopId))
            .findFirst()
            .orElse(defaultShopPenaltyHandler)
            .process(shopId, shopPenalty);
    }
}
