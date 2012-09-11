
package com.qxjerry.sleeptest.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SqliteDataHelper {

    public final static String SLEEP_DB_NAME = "sleep_time_db";

    public final static int DB_VERSION = 1;

    public final static String SLEEP_TIME_TABLE = "sleep_time";

    public final static String SLEEP_MARK_TIME = "mark_time";

    public final static String SLEEP_CREATE_TIME = "create_time";

    public final static String SLEEP_TIME_TYPE = "time_type";

    public static final String _ID = "_id";

    public final static int SLEEP_TYPE_GOTO_BED = 1;

    public final static int SLEEP_TYPE_WAKE_UP = 2;

    public enum SleepType {
        GOTO_BED, WAKEUP
    }

    private static SqliteDataHelper dbHelper;

    private DBAdapter dbAdapter;

    private SQLiteDatabase mdb;

    private Context mContext;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private SqliteDataHelper(Context context) {
        mContext = context;
    }

    public static SqliteDataHelper getDBHelper(Context context) {
        if (null == dbHelper) {
            dbHelper = new SqliteDataHelper(context);
        }
        return dbHelper;
    }

    public void openDBHelper() {
        if (null == dbAdapter) {
            dbAdapter = new DBAdapter(mContext);
        }

        if (null == mdb) {
            mdb = dbAdapter.getWritableDatabase();
        } else {
            if (!mdb.isOpen()) {
                mdb = dbAdapter.getWritableDatabase();
            }
        }
    }

    public void closeDBHelper() {
        mdb.close();
        dbAdapter.close();
    }

    public Cursor getAllSleepTime() {
        Cursor cursor = null;
        if (mdb != null) {
            cursor = mdb.query(SLEEP_TIME_TABLE, null, null, null, null, null, null);
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getNightSignin() {
        return getSleepTime(SLEEP_TYPE_GOTO_BED);
    }

    public Cursor getMorningSignin() {
        return getSleepTime(SLEEP_TYPE_WAKE_UP);
    }


    public String  getMarkSleepTimeById(long id){
        Cursor c = null;
        String   marktimeString ="" ;
        if (mdb != null) {
            c = mdb.query(SLEEP_TIME_TABLE, null, _ID + " =  " + id, null,
                    null, null, null);
            c.moveToFirst();
            marktimeString = c.getString(c.getColumnIndexOrThrow(SLEEP_MARK_TIME));
        }

        return  marktimeString;
    }

    private Cursor getSleepTime(int sleey_type) {
        Cursor c = null;
        if (mdb != null) {
            c = mdb.query(SLEEP_TIME_TABLE, null, SLEEP_TIME_TYPE + " =  " + sleey_type, null,
                    null, null, _ID + "  desc");
        }

        return c;
    }

    public int updateOneTime(long id, Date remarkDate) {
        int flag = -1;
        if (mdb != null) {
            ContentValues cv = new ContentValues();
            Log.i("time" ,   " remarkDate.getYear()===,"+ remarkDate.getYear());
            Log.i("time" ,   "dateFormat.format(remarkDate)===,"+ dateFormat.format(remarkDate));
            cv.put(SLEEP_MARK_TIME, dateFormat.format(remarkDate));
            flag = mdb.update(SLEEP_TIME_TABLE, cv, _ID + " = " + id, null);
        }
        return flag;
    }

    public int deleteOneTime(long id) {
        int d = -1;
        if (mdb != null) {
            d = mdb.delete(SLEEP_TIME_TABLE, _ID + "= " + id, null);
        }
        return d;
    }

    public long addOneTime(SleepType sleep_time_type) {
        return addOneTime(new Date(), sleep_time_type);
    }

    public long addOneTime(Date marktime, SleepType sleep_time_type) {
        if (null == marktime || "".equals(marktime)) {
            return -1;
        }
        ContentValues initialValues = new ContentValues();
        initialValues.put(SLEEP_CREATE_TIME, "" + dateFormat.format(new Date()));
        initialValues.put(SLEEP_MARK_TIME, "" + dateFormat.format(marktime));
        switch (sleep_time_type) {
            case GOTO_BED:
                initialValues.put(SLEEP_TIME_TYPE, SLEEP_TYPE_GOTO_BED);
                break;
            case WAKEUP:
                initialValues.put(SLEEP_TIME_TYPE, SLEEP_TYPE_WAKE_UP);
                break;

            default:
                break;
        }

        return mdb.insert(SLEEP_TIME_TABLE, null, initialValues);
    }

    class DBAdapter extends SQLiteOpenHelper {

        DBAdapter(Context context) {
            super(context, SLEEP_DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + SLEEP_TIME_TABLE + " (" + _ID
                    + " integer primary key autoincrement," + SLEEP_MARK_TIME + " text not null,"
                    + SLEEP_TIME_TYPE + " INTEGER  ," + SLEEP_CREATE_TIME + " text not null    );");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SLEEP_TIME_TABLE);
            onCreate(db);
        }
    }

}
