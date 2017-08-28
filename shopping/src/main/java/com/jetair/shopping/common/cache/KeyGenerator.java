package com.jetair.shopping.common.cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class KeyGenerator {
    public KeyGenerator() {
    }
    public static String toString(Object key){
        return key==null ? "NULL":(key instanceof Date ?(new SimpleDateFormat("yyyyMMddHHmmssSSS")).format(key) :key.toString());
    }
}
