package com.ginkgocap.parasol.file.web.jetty.web.utils;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {
    public static List<Long> convert(String demandIds) {
        List<Long> ids = new ArrayList<Long>();
        String[] r = demandIds.split(",");
        if (r != null && r.length > 0) {
            for (String id : r) {
                if(!id.equals("")){
                    ids.add(new Long(id));
                }
            }
        }
        return ids;
    }
}
