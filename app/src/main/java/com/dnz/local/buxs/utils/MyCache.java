package com.dnz.local.buxs.utils;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

public class MyCache {
    private static Map<String, Object> cache;

    // Used to create new data in cache or edit data in cache
    public synchronized static void writeToCache(String key, Object data){
        cache.put(key, data);
    }

    // Used to load data from cache
    public static Object getFromCache(String key){
        return cache.get(key);
    }

    // can be used by app to perform operations on cache data on a separate thread eg: saving to file
    public static void saveData(String key, AsyncTask task){
        Object data = cache.get(key);
        task.execute(data);
    }

    public static synchronized void destroyData(String key){
        cache.remove(key);
    }

    public static void resetCache(){
        cache = new HashMap<>();
    }

    public static void startCache(){
        cache = new HashMap<>();
    }
}
