package com.oyeoye.merchant.business;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    /**
     * Constants
     */
    private final static String PREFERENCES_FILE_KEY = "com.oyeoye.merchant.business.PREFERENCE_FILE_KEY";
    public final static String GCM_TOKEN_SENT_TO_SERVER_KEY = "com.oyeoye.merchant.business.GCM_TOKEN_SENT_TO_SERVER_KEY";
    public final static String GCM_TOKEN = "com.oyeoye.merchant.business.GCM_TOKEN";

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
     * Helper method to put an boolean
     *
     * @param key
     * @param value
     * @return
     */
    private SharedPreferences.Editor putBoolean(String key, boolean value) {
        return getEditor().putBoolean(key, value);
    }

    /**
     * Helper method to get boolean
     *
     * @param key
     * @param defValue
     * @return
     */
    private boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }


    /**
     * Helper method to remove a value with a given key
     *
     * @param key
     */
    private void remove(String key) {
        getEditor().remove(key).apply();
    }

    public void setGcmTokenSentToServer(boolean value) {
        putBoolean(GCM_TOKEN_SENT_TO_SERVER_KEY, value).apply();
    }

    public boolean setGcmTokenSentToServer() {
        return getBoolean(GCM_TOKEN_SENT_TO_SERVER_KEY, false);
    }

    public String getGcmToken() {
        return getString(GCM_TOKEN, null);
    }
}
