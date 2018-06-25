package com.zhizaolian.staff.utils;

public class FormatJSEltarg {
	public static String format(String s) {
        if (s != null && s.length() > 0) {
            s = s.replaceAll("(\r\n|\n\r)", "<br>");
            s = s.replaceAll("(\n)", "<br>");
            s = s.replaceAll("\"", "\\\\" + "\"");
            s = s.replaceAll("\'", "\\\\" + "\'");
            return s;
        } else { 
            return "";
        }
    }
}
