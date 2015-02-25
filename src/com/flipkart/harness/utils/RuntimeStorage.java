package com.flipkart.harness.utils;

import java.util.HashMap;
import java.util.Map;

public class RuntimeStorage {
    private static Map<String, String> runtimeStorageMap = new HashMap<String, String>();

    public static String get(String key)
    {
        return runtimeStorageMap.get(key);
    }

    public static synchronized void put(String key, String value)
    {
        runtimeStorageMap.put(key, value);
    }

}
