package com.github.tianma8023.smscode.utils;

import android.content.Context;

import com.crossbowffs.remotepreferences.RemotePreferenceAccessException;
import com.crossbowffs.remotepreferences.RemotePreferences;
import com.github.tianma8023.smscode.constant.IPrefConstants;

public class RemotePreferencesUtils {

    private static final String FIRST_RUN_SINCE_V1 = "first_run_v1";
    // 是否已经对MIUI的"通知类短信"权限进行提示了
    private static final String SERVICE_SMS_PROMPT_SHOWN = "service_sms_prompt_shown";
    private static final String LAST_SMS_DATE = "last_sms_date";
    private static final String LAST_SMS_SENDER = "last_sms_sender";

    private RemotePreferencesUtils() {
    }

    public static RemotePreferences getDefaultRemotePreferences(Context context) {
        return new RemotePreferences(context,
                IPrefConstants.REMOTE_PREF_AUTHORITY,
                IPrefConstants.REMOTE_PREF_NAME,
                true);
    }

    public static boolean getBooleanPref(RemotePreferences mPreferences, String key, boolean defaultValue) {
        try {
            return mPreferences.getBoolean(key, defaultValue);
        } catch (RemotePreferenceAccessException e) {
            XLog.e("Failed to read preference: %s", key, e);
            return defaultValue;
        }
    }

    public static void setBooleanPref(RemotePreferences preferences, String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public static String getStringPref(RemotePreferences preferences, String key, String defaultValue) {
        try {
            return preferences.getString(key, defaultValue);
        } catch (RemotePreferenceAccessException e) {
            XLog.e("Failed to read preference: %s", key, e);
            return defaultValue;
        }
    }

    public static void setStringPref(RemotePreferences preferences, String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public static long getLongPref(RemotePreferences preferences, String key, long defaultValue) {
        try {
            return preferences.getLong(key, defaultValue);
        } catch (RemotePreferenceAccessException e) {
            XLog.d("Failed to read preference: %s", key, e);
            return defaultValue;
        }
    }

    public static void setLongPref(RemotePreferences preferences, String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public static boolean isFirstRunSinceV1(RemotePreferences preferences) {
        return getBooleanPref(preferences, FIRST_RUN_SINCE_V1, true);
    }

    public static void setFirstRunSinceV1(RemotePreferences preferences, boolean value) {
        setBooleanPref(preferences, FIRST_RUN_SINCE_V1, value);
    }

    /**
     * MIUI的"通知类短信"权限申请是否已经提示过
     * @param preferences
     * @return
     */
    public static boolean isServiceSmsPromptShown(RemotePreferences preferences) {
        return getBooleanPref(preferences, SERVICE_SMS_PROMPT_SHOWN, false);
    }

    public static void setServiceSmsPromptShown(RemotePreferences preferences, boolean shown) {
        setBooleanPref(preferences, SERVICE_SMS_PROMPT_SHOWN, shown);
    }

    public static void setLastSmsSender(RemotePreferences preferences, String lastSender) {
        setStringPref(preferences, LAST_SMS_SENDER, lastSender);
    }

    public static String getLastSmsSender(RemotePreferences preferences) {
        return getStringPref(preferences, LAST_SMS_SENDER, "");
    }

    public static void setLastSmsDate(RemotePreferences preferences, long lastSendDate) {
        setLongPref(preferences, LAST_SMS_DATE, lastSendDate);
    }

    public static long getLastSmsDate(RemotePreferences preferences) {
        return getLongPref(preferences, LAST_SMS_DATE, -1L);
    }
}
