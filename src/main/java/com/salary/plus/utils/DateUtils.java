package com.salary.plus.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtils {

    public static String getFormatted(DateTimeFormatter formatter) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        return currentDateTime.format(formatter);
    }

    public static long getDuration(String timeStr, String type) {
        //        String timeStr = "09:00:00"; // 기준 시간

        // 1. 시간 포맷터로 LocalTime 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime targetTime = LocalTime.parse(timeStr, formatter);

        // 2. 현재 시각 (시스템 기본 시간대 기준)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayWithTargetTime = LocalDateTime.of(now.toLocalDate(), targetTime);

        // 3. 현재보다 과거일 경우, 차이 계산
        Duration duration = Duration.between(todayWithTargetTime, now);

        // 4. 결과 출력
        long diffMinutes = duration.toMinutes();
        long diffHours = duration.toHours();

        //        System.out.println("차이 (분): " + diffMinutes);
        //        System.out.println("차이 (시간): " + diffHours);
        return switch (type) {
            case "1H" -> diffHours;
            case "1M" -> diffMinutes;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public static String getMinusDay(String dateStr, long minusDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate targetDate = LocalDate.parse(dateStr, formatter);
        return targetDate.minusDays(minusDay).format(formatter);
    }

    /**
     *
     * @return MON, TUE, WED, THU, FRI, SAT, SUN
     */
    public static String getWeekdayFormat() {
        return LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
    }
}
