/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

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
