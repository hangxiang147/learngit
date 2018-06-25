package com.zhizaolian.staff.utils;

public class EscapeUtil {
	
    public static String decodeSpecialChars(String content)
    {
        String afterDecode = content.replaceAll("'", "''");
        afterDecode = afterDecode.replaceAll("\\\\", "\\\\\\\\");
        afterDecode = afterDecode.replaceAll("%", "\\%");
        afterDecode = afterDecode.replaceAll("_", "\\_");
        return afterDecode;
    } 
}
