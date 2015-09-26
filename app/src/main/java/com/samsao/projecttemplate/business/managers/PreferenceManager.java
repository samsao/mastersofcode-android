package com.samsao.projecttemplate.business.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author jliang
 * @since 2015-08-07
 */
public class PreferenceManager {
    /**
     * Constants
     */
    private final String PREFERENCES_FILE_KEY = "com.samsao.projecttemplate.PREFERENCE_FILE_KEY";//TODO change name

    /**
     * Shared preferences
     */
    private SharedPreferences mSharedPreferences;

    /**
     * Constructor
     *
     * @param context
     */
    public PreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    /**
     * Get the editor
     *
     * @return
     */
    private SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    /**
     * Helper method to get a string
     *
     * @param key
     * @param value
     * @return
     */
    private SharedPreferences.Editor putString(String key, String value) {
        return getEditor().putString(key, value);
    }

    /**
     * Helper method to put a string
     *
     * @param key
     * @param defValue
     * @return
     */
    private String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    /**
     * Helper method to put an integer
     *
     * @param key
     * @param value
     * @return
     */
    private SharedPreferences.Editor putInteger(String key, int value) {
        return getEditor().putInt(key, value);
    }

    /**
     * Helper method to get int
     *
     * @param key
     * @param defValue
     * @return
     */
    private int getInteger(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    /**
     * Helper method to put an float
     *
     * @param key
     * @param value
     * @return
     */
    private SharedPreferences.Editor putFloat(String key, float value) {
        return getEditor().putFloat(key, value);
    }

    /**
     * Helper method to get float
     *
     * @param key
     * @param defValue
     * @return
     */
    private float getFloat(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

    /**
     * Helper method to remove a value with a given key
     *
     * @param key
     */
    private void remove(String key) {
        getEditor().remove(key).apply();
    }
}
