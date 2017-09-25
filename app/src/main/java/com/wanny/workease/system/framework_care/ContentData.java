package com.wanny.workease.system.framework_care;

import com.google.gson.Gson;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 文件名： ContentData
 * 功能：
 * 作者： wanny
 * 时间： 16:32 2017/6/22
 */
public class ContentData {

    public static String token = "";
    public static String yiping = "";


//    //
//    public static String setPostYiping(Object object) {
//        Gson gson = new Gson();
//        String noSing = gson.toJson(object);
//        return MD5Utils.MD5Encode(noSing + ApiStores.ASID, "utf-8", false);
//    }
//
//    //
//    public static String setGetyiping(Map<String, String> object) {
//        StringBuffer noSign = new StringBuffer();
//        //排序
////        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>();
////        list.addAll(object.entrySet());
////        KeyComparator kc = new KeyComparator();
////        Collections.sort(list, kc);
//        //按照值
////        for (Iterator<Map.Entry<String, String>> it = list.iterator(); it .hasNext(); ) {
////            Map.Entry<String, String> entry = it.next();
////            noSign.append(entry.getKey()).append(entry.getValue());
////        }
////
//        Iterator<Map.Entry<String, String>> it = object.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, String> entry = it.next();
//            noSign.append(entry.getKey()).append(entry.getValue());
//
//        }
//        LogUtil.log("nosign== ", noSign + "");
//        return MD5Utils.MD5Encode(noSign.toString() + ApiStores.ASID, "utf-8", false);
//    }
//
//    //按照值排序
//    private static class ValueComparator implements
//            Comparator<Map.Entry<String, String>> {
//        public int compare(Map.Entry<String, String> m,
//                           Map.Entry<String, String> n) {
//            return m.getValue().compareTo(n.getValue());
//        }
//    }
//
//    private static class KeyComparator implements Comparator<Map.Entry<String, String>> {
//        public int compare(Map.Entry<String, String> m,
//                           Map.Entry<String, String> n) {
//            return m.getValue().compareTo(n.getValue());
//        }
//    }
}
