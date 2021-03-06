package com.ginkgocap.parasol.file.web.jetty.web.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 功能描述：去掉特殊字符
     * @param source
     * @return dest
     */
    public static String replaceSpecial(String source) {
        String dest = "";
        if (source != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(source);
            dest = m.replaceAll("");
        }
        return dest;
    }
    
    public static void main(String[] args){
    	System.out.println();
    }
}
