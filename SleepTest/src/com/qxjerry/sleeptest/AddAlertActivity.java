
package com.qxjerry.sleeptest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.Date;

public class AddAlertActivity extends Activity {

    TableRow toggleRow;

    TableRow timeRow;

    ToggleButton alarmToggle;

    TextView alarmTimeText;

    TextView timeLeave;

    Button addAlarmOk;

    Button addAlarmCancel;

    PendingIntent sender;

    // 设定一个五秒后的时间
    Calendar calendar;

    AlarmManager alarm;
    Date whenSetTime ;
    int leaveHours ;
    int leaveMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.add_alarm);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        toggleRow = (TableRow) findViewById(R.id.alarm_off_on_row);
        timeRow = (TableRow) findViewById(R.id.alarm_time_row);
        alarmToggle = (ToggleButton) findViewById(R.id.alarm_toggle_button);
        alarmTimeText = (TextView) findViewById(R.id.alarm_time_show_text);
        timeLeave = (TextView) findViewById(R.id.alarm_time_leave);

        addAlarmOk = (Button) findViewById(R.id.alarm_add_ok);
        addAlarmCancel = (Button) findViewById(R.id.alarm_add_cancel);

        addAlarmOk.setOnClickListener(viewClickListener);
        addAlarmCancel.setOnClickListener(viewClickListener);
        toggleRow.setOnClickListener(viewClickListener);
        alarmToggle.setOnCheckedChangeListener(checkedChangeListener);
        timeRow.setOnClickListener(viewClickListener);

        Intent intent = new Intent(AddAlertActivity.this, AlarmBroadcastReceiver.class);
        intent.setAction("short");
        sender = PendingIntent.getBroadcast(AddAlertActivity.this, 0, intent, 0);
        // 设定一个五秒后的时间
        calendar = Calendar.getInstance();
        // calendar.setTimeInMillis(System.currentTimeMillis());
        // calendar.add(Calendar.SECOND, 5);

        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        super.onStart();
    }

    OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {

            } else {

            }
        }
    };

    private void setLeaveTime(int hourOfDay, int minute ){
        // 2:40 6:00 4:50
        whenSetTime = new Date();
        int currentHours = whenSetTime.getHours();
        int currentMinute = whenSetTime.getMinutes();


        if (currentMinute > minute) {
            leaveMinute = 60 - (currentMinute - minute);
            if (currentHours >= hourOfDay) {
                leaveHours = 23 - (currentHours - hourOfDay);
            } else {
                leaveHours = hourOfDay - currentHours - 1;
            }
        } else {
            leaveMinute = minute - currentMinute;
            if (currentHours <= hourOfDay) {
                leaveHours = hourOfDay - currentHours;
            } else {
                leaveHours = 23 - (currentHours - hourOfDay);
            }
        }

        String formatStr = AddAlertActivity.this.getResources()
                .getString(R.string.time_leave);
        String leaveStr = String.format(formatStr, leaveHours,
                leaveMinute);
        timeLeave.setText(leaveStr);
    }


    OnClickListener viewClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.alarm_add_cancel:
                    alarm.cancel(sender);
                    finish();
                    break;
                case R.id.alarm_add_ok:
                    if(alarmToggle.isChecked()){
                        calendar.setTime(whenSetTime);
                        calendar.add(Calendar.MINUTE, leaveMinute);
                        calendar.add(Calendar.HOUR_OF_DAY, leaveHours);
                        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                        String formatStr = AddAlertActivity.this.getResources()
                                .getString(R.string.time_leave);
                        String leaveStr = String.format(formatStr, leaveHours,
                                leaveMinute);
                        Toast.makeText(AddAlertActivity.this, leaveStr, Toast.LENGTH_LONG).show();
                    }else{
                        alarm.cancel(sender);
                    }
                    finish();
                    break;
                case R.id.alarm_off_on_row:
                    alarmToggle.toggle();
                    break;
                case R.id.alarm_time_row:
                    Date date = new Date();
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddAlertActivity.this,
                            new OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    // TODO Auto-generated method stub
                                    alarmTimeText.setText(hourOfDay + ":" + minute);
                                    setLeaveTime(hourOfDay,minute);
                                }
                            }, date.getHours(), date.getMinutes(), true);
                    timePickerDialog.show();
                    break;

                default:

                    break;
            }
        }
    };
}
