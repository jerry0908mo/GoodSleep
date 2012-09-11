
package com.qxjerry.sleeptest.data;

import com.qxjerry.sleeptest.data.SqliteDataHelper.SleepType;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalDataHelper {

    private static final String SLEEP_LOCAL_DATA = "sleep_local_data";

    private static final String SLEEP_TYPE = "current_sleep_status";

    public static boolean setSleepStatus(Context context, SleepType sleepType) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SLEEP_LOCAL_DATA,
                Context.MODE_PRIVATE);
        Editor editor = mSharedPreferences.edit();
        switch (sleepType) {
            case GOTO_BED:
                editor.putInt(SLEEP_TYPE, SqliteDataHelper.SLEEP_TYPE_GOTO_BED);
                break;

            case WAKEUP:
                editor.putInt(SLEEP_TYPE, SqliteDataHelper.SLEEP_TYPE_WAKE_UP);
                break;

            default:
                editor.putInt(SLEEP_TYPE, SqliteDataHelper.SLEEP_TYPE_GOTO_BED);
                break;
        }
        return editor.commit();
    }

    public static SleepType getSleepStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SLEEP_LOCAL_DATA,
                Context.MODE_PRIVATE);

        int getSleepStatus = sharedPreferences.getInt(SLEEP_TYPE, -1);

        switch (getSleepStatus) {
            case SqliteDataHelper.SLEEP_TYPE_GOTO_BED:
                return SleepType.GOTO_BED;

            case SqliteDataHelper.SLEEP_TYPE_WAKE_UP:
                return SleepType.WAKEUP;

            default:
                return SleepType.GOTO_BED;
        }
    }
}
