package com.comp90024.proj2.util;

import java.util.regex.Pattern;

public class Consts {

    public class ResultCode {

        public static final String SUCCESS = "1";

        public static final String ERROR = "0";


    }

    public static final Pattern SUBURB_PATTERN = Pattern.compile("[a-zA-Z\\s]+(?=\\s\\([A-Z]+\\))");

}
