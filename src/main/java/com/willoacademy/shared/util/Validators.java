package com.willoacademy.shared.util;

import java.util.regex.Pattern;

public final class Validators {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private Validators() {}

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.isBlank();
    }

    public static String sanitize(String value) {
        if (value == null) return null;
        return value.replaceAll("<[^>]*>", "").trim();
    }
}
