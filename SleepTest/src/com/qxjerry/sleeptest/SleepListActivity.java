
package com.qxjerry.sleeptest;

import com.qxjerry.sleeptest.data.SqliteDataHelper;

import org.xml.sax.DTDHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SleepListActivity extends Activity {
    ListView morningListView;

    ListView nightListView;

    SqliteDataHelper sqliteDataHelper;

    SimpleCursorAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sleeplist);

        morningListView = (ListView) findViewById(R.id.sleep_morning_list);
        nightListView = (ListView) findViewById(R.id.sleep_night_list);

        morningListView.setOnItemClickListener(onitemClklistener);
        nightListView.setOnItemClickListener(onitemClklistener);


    }

    private OnItemClickListener onitemClklistener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
            showSleepTimeDatailDialog(id);
        }
    };

    private void showSleepTimeDatailDialog(final long id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("操作");
        dialog.setItems(new String[] {
                "编辑", "删除", "取消"
        }, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // show edit ui.
                        updateSleepTime(id);

                        break;

                    case 1:
                        // show confirm dialog.
                        confirmDelete(id);

                        break;
                    case 2:
                        dialog.cancel();
                        break;
                    default:
                        dialog.cancel();
                        break;
                }
            }
        });
        dialog.show();
    }


    private    void    confirmDelete(final long id){
        AlertDialog.Builder     deleteDialog  =  new AlertDialog.Builder(this);
        deleteDialog.setTitle("确认删除？");
        deleteDialog.setMessage("确认删除该记录？");
        deleteDialog.setNegativeButton("取消",null);
        deleteDialog.setPositiveButton("确定", new  OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                sqliteDataHelper.openDBHelper();
                int deleteFlag = sqliteDataHelper.deleteOneTime(id);
                sqliteDataHelper.closeDBHelper();

                if (deleteFlag > -1) {
                    Toast.makeText(SleepListActivity.this, "删除成功。", Toast.LENGTH_SHORT).show();
                }
                setAdapter();
            }
        });
        deleteDialog.show();
    }

    private  void    updateSleepTime(final long id){
        Toast.makeText(SleepListActivity.this, "编辑～～～～～～～～。", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder   dateTimeDialog =   new AlertDialog.Builder(this);

        final DatePicker   dateView  =  new DatePicker(this);
        final TimePicker  timeView  = new TimePicker(this);

       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       sqliteDataHelper.openDBHelper();
       String    markString  = sqliteDataHelper.getMarkSleepTimeById(id);
       Date   markDate =  new  Date();
        try {
            markDate = dateFormat.parse(markString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LinearLayout   layout =  new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(dateView);
        layout.addView(timeView);


        Log.i("time" ,   "markDate.getYear()===,"+(markDate.getYear()+1900)+"   markDate.getMonth()==="+markDate.getMonth()+"  markDate.getDate() =="+ markDate.getDate());
        dateView.init(markDate.getYear() +1900, markDate.getMonth(),  markDate.getDate(), null);
        timeView.setCurrentHour(markDate.getHours());
        timeView.setCurrentMinute(markDate.getMinutes());
        timeView.setIs24HourView(true);


        dateTimeDialog.setView(layout);

        dateTimeDialog.setTitle("重新设置时间");
        dateTimeDialog.setNegativeButton("取消",null);
        dateTimeDialog.setPositiveButton("确定",new  OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("time" ,   "dateView.getYear()===,"+dateView.getYear());
              Date  getDate  =  new Date(dateView.getYear()- 1900, dateView.getMonth(), dateView.getDayOfMonth(), timeView.getCurrentHour(), timeView.getCurrentMinute(), 0);
              sqliteDataHelper.openDBHelper();
              Log.i("time" ,   "getDate.getYear()===,"+getDate.getYear());
             int    updateFlag = sqliteDataHelper.updateOneTime(id, getDate);
              sqliteDataHelper.closeDBHelper();

              if (updateFlag > -1) {
                  Toast.makeText(SleepListActivity.this, "时间修改成功。", Toast.LENGTH_SHORT).show();
              }
              setAdapter();
            }
        });
        dateTimeDialog.show();
//        updateDialog.setView();
    }

    @Override
    protected void onStart() {
        sqliteDataHelper = SqliteDataHelper.getDBHelper(this);
        setAdapter();

        super.onStart();
    }

    private   void  setAdapter(){
        sqliteDataHelper.openDBHelper();

        Cursor moringCursor = sqliteDataHelper.getMorningSignin();
        Cursor nightCursor = sqliteDataHelper.getNightSignin();

        morningListView.setAdapter(getAdapter(moringCursor));
        nightListView.setAdapter(getAdapter(nightCursor));

        sqliteDataHelper.closeDBHelper();

        if(nightListView.getCount() <= 0  ){
            nightListView.setVisibility(View.GONE);
            findViewById(R.id.sleep_night_list_empty).setVisibility(View.VISIBLE);
        }else{
            nightListView.setVisibility(View.VISIBLE);
            findViewById(R.id.sleep_night_list_empty).setVisibility(View.GONE);
        }

        if(morningListView.getCount() <= 0  ){
            morningListView.setVisibility(View.GONE);
            findViewById(R.id.sleep_morning_list_empty).setVisibility(View.VISIBLE);
        }else{
            morningListView.setVisibility(View.VISIBLE);
            findViewById(R.id.sleep_morning_list_empty).setVisibility(View.GONE);
        }

    }


    private SimpleCursorAdapter getAdapter(Cursor c) {
        adapter = new SimpleCursorAdapter(this, R.layout.sleep_item, c,
                new String[] {
                    SqliteDataHelper.SLEEP_MARK_TIME
                }, new int[] {
                    R.id.time_show
                });

        return adapter;
    }

    class SleepCursorAdapter extends SimpleCursorAdapter {
        private View view;

        public SleepCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
            this.view = view;
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(SqliteDataHelper._ID));
            view.findViewById(R.id.time_show).setTag(id);
        }

        @Override
        public long getItemId(int position) {
            return (Long) view.findViewById(R.id.time_show).getTag();
        }

    }
}
