package com.example.notehub.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheHelper {

    private static final String CACHE_NAME = "notehub";
    private static final String DEFAULT_STRING_VALUE = "";
    private static final int DEFAULT_INT_VALUE = 0;
    private static final long DEFAULT_LONG_VALUE = 0L;
    private static final float DEFAULT_FLOAT_VALUE = 0.0f;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;

    private SharedPreferences sharedPreferences;

    public CacheHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
    }

    // Save data to cache
    public void save(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.apply();
    }

    // Retrieve data from cache
    public Object get(String key, Object defaultValue) {
        if (defaultValue instanceof String) {
            return sharedPreferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultValue);
        }
        return null;
    }

    // Clear data for a specific key from cache
    public void clearKey(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    // Clear all data from cache
    public void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Check if a key exists in the cache
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }
}
