package com.salary.plus.service.handler;

public interface SalaryHandler {
    boolean supports(Long shopId);

    String process();
}
