package com.salary.plus.guard;

import java.util.Map;

public interface UseGuard {
    void canActivate(Map<String, Object> parameterMap) throws RuntimeException;
}
