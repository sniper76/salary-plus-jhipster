package com.salary.plus.service.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultShopSalaryHandler implements SalaryHandler {

    @Override
    public boolean supports(Long shopId) {
        return getSupportVersions().contains(shopId);
    }

    @Override
    public String process() {
        return null;
    }
}
