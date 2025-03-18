package com.salary.plus.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String getFormatted(DateTimeFormatter formatter) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        return currentDateTime.format(formatter);
    }
}
