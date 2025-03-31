package com.salary.plus.service.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalaryHandlerResolver {

    private final List<SalaryHandler> salaryHandlerList;
    private final DefaultShopSalaryHandler defaultShopSalaryHandler;

    /*
    private final MailDeliveryStatusHandlerResolver mailDeliveryStatusHandlerResolver;

        return mailDeliveryStatusHandlerResolver
            .resolve(MailDeliveryStatus.valueOf(updateMailDeliveryStatusRequest.getMailDeliveryStatus()))
            .handle(mailDeliveryId);
     */
    public String resolve(Long shopId) {
        return salaryHandlerList
            .stream()
            .filter(handler -> handler.supports(shopId))
            .findFirst()
            .orElse(defaultShopSalaryHandler)
            .process();
    }
}
