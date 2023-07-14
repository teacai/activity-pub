package com.teacai.activitypub.util;

import java.time.Instant;

public class ParseUtils {

    public static String strValue(Object obj) {
        return strValue(obj, null);
    }

    public static String strValue(Object obj, String defaultValue) {
        return obj == null ? defaultValue : obj.toString();
    }

    public static Integer intValue(Object obj) {
        return obj == null ? null : Integer.parseInt(obj.toString());
    }

    public static Instant instantValue(Object obj) {
        return obj == null ? null : Instant.parse(obj.toString());
    }
}
