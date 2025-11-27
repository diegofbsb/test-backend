package com.test.backend.util;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class Util {

    public static String toLower(String value) {
        if (value == null) {
            return null;
        }
        return value.toLowerCase(Locale.ROOT);
    }
}
