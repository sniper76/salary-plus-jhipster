package com.salary.plus.service.handler;

import com.salary.plus.utils.SetUtils;
import java.util.Set;

public interface SalaryHandler {
    default Set<Long> getSupportVersions() {
        return SetUtils.immutableSet(1L);
    }

    boolean supports(Long shopId);

    String process();
}
