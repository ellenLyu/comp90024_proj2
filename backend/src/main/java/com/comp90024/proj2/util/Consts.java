/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

package com.comp90024.proj2.util;

import java.util.regex.Pattern;

public class Consts {

    public class ResultCode {

        public static final String SUCCESS = "1";

        public static final String ERROR = "0";


    }

    public static final Pattern SUBURB_PATTERN = Pattern.compile("[a-zA-Z\\s]+(?=\\s\\([A-Z]+\\))");

}
