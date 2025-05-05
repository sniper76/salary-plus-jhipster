package com.salary.plus.service.provider;

import com.salary.plus.utils.DateUtils;
import org.springframework.stereotype.Component;

@Component
public class DateProvider {

    public String getWeekday() {
        return DateUtils.getWeekdayFormat();
    }
}
