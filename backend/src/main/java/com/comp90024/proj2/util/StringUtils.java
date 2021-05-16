package com.comp90024.proj2.util;

public class StringUtils {

    public static boolean isNotEmpty(String s) {
        int strLen;
        if (s == null || (strLen = s.length()) == 0) {
            return false;
        }

        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(s.charAt(i)))) {
                return true;
            }
        }
        return false;
    }
}
