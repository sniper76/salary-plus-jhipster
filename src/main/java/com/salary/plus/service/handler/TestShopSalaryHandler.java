package com.salary.plus.service.handler;

import com.salary.plus.utils.SetUtils;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TestShopSalaryHandler implements SalaryHandler {

    @Override
    public boolean supports(Long shopId) {
        return !getSupportVersions().contains(shopId);
    }

    @Override
    public String process() {
        return null;
    }
}
