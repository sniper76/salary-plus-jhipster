package com.salary.plus.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    public static final String WORK_START_TIME_REGEX = "^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "ko";

    private Constants() {}
}
