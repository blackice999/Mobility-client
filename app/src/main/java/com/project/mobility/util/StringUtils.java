package com.project.mobility.util;

import android.util.Patterns;

import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean containsIgnoreCase(String haystack, String needle) {
        if (needle.equals("")) {
            return true;
        }

        if (haystack == null || haystack.equals("")) {
            return false;
        }

        return Pattern.compile(needle, Pattern.CASE_INSENSITIVE + Pattern.LITERAL).matcher(haystack).find();
    }
}
