package com.project.mobility.util;

import android.util.Patterns;

public class StringUtils {
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPhoneValid(String number) {
        return Patterns.PHONE.matcher(number).matches();
    }
}
