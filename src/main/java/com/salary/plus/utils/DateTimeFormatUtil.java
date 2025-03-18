package com.salary.plus.utils;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class DateTimeFormatUtil {

    private DateTimeFormatUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<String, DateTimeFormatter> FORMATTER_MAP = Map.ofEntries(
        createPatternEntry("yyMMdd"),
        createPatternEntry("yyyy"),
        createPatternEntry("yyyyMMdd"),
        createPatternEntry("yyyy-MM-dd"),
        createPatternEntry("yyyy-MM"),
        createPatternEntry("yyyy년 MM월 dd일", Locale.KOREAN),
        createPatternEntry("yyyy년 MM월 dd일 HH시", Locale.KOREAN),
        createPatternEntry("yyyy-MM-dd hh:mm:ss a", getAmPmSupportLocale()),
        createPatternEntry("yyyy-MM-dd HH:mm:ss"),
        createPatternEntry("yyyy-MM-dd HH:mm"),
        createPatternEntry("yyyy.MM.dd"),
        createPatternEntry("yy.MM.dd")
    );

    public static DateTimeFormatter ofPattern(String pattern) {
        return ofPattern(pattern, Locale.getDefault());
    }

    public static DateTimeFormatter ofPattern(String pattern, Locale locale) {
        return DateTimeFormatter.ofPattern(pattern, locale);
    }

    public static DateTimeFormatter yyMMdd() {
        return FORMATTER_MAP.get("yyMMdd");
    }

    public static DateTimeFormatter yyyy() {
        return FORMATTER_MAP.get("yyyy");
    }

    public static DateTimeFormatter yyyyMMdd() {
        return FORMATTER_MAP.get("yyyyMMdd");
    }

    public static DateTimeFormatter yyyy_MM_dd() {
        return FORMATTER_MAP.get("yyyy-MM-dd");
    }

    public static DateTimeFormatter yyyy_MM() {
        return FORMATTER_MAP.get("yyyy-MM");
    }

    public static DateTimeFormatter yyyy_MM_dd_korean() {
        return FORMATTER_MAP.get("yyyy년 MM월 dd일");
    }

    public static DateTimeFormatter yyyy_MM_dd_HH_korean() {
        return FORMATTER_MAP.get("yyyy년 MM월 dd일 HH시");
    }

    public static DateTimeFormatter yyyy_MM_dd_hh_mm_ss_a() {
        return FORMATTER_MAP.get("yyyy-MM-dd hh:mm:ss a");
    }

    public static DateTimeFormatter yyyy_MM_dd_HH_mm_ss() {
        return FORMATTER_MAP.get("yyyy-MM-dd HH:mm:ss");
    }

    public static DateTimeFormatter yyyy_MM_dd_HH_mm() {
        return FORMATTER_MAP.get("yyyy-MM-dd HH:mm");
    }

    public static DateTimeFormatter yyyyMMdd_withDots() {
        return FORMATTER_MAP.get("yyyy.MM.dd");
    }

    public static DateTimeFormatter yyMMdd_withDots() {
        return FORMATTER_MAP.get("yy.MM.dd");
    }

    private static Locale getAmPmSupportLocale() {
        // Use Locale.US to format the time with "AM/PM" instead of "오전/오후"
        return Locale.US;
    }

    private static Map.Entry<String, DateTimeFormatter> createPatternEntry(String pattern) {
        return createPatternEntry(pattern, Locale.getDefault());
    }

    private static Map.Entry<String, DateTimeFormatter> createPatternEntry(String pattern, Locale locale) {
        return Map.entry(pattern, DateTimeFormatter.ofPattern(pattern, locale));
    }
}
