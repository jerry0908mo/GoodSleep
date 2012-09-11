
package com.qxjerry.sleeptest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

public class AlertSettingActivity extends Activity {

    Button   addButton;
    ListView    alarmListView;
    public  int    ADD_ALARM_REQUEST = 901;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting);

    }

    @Override
    protected void onStart() {

        addButton  =   (Button) findViewById(R.id.add_alarm_button);
        alarmListView  =  (ListView) findViewById(R.id.alarm_list);
        addButton.setOnClickListener(btnOnClickListener);
        super.onStart();
    }

    View.OnClickListener   btnOnClickListener  =  new   OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.add_alarm_button:
                    startActivity(new  Intent(AlertSettingActivity.this,AddAlertActivity.class));
                    break;

                default:
                    break;
            }
        }
    };

    public   void startAlarm(){

        //      AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

              Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
              intent.setAction("short");
              PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

              // 设定一个五秒后的时间
              Calendar calendar = Calendar.getInstance();
              calendar.setTimeInMillis(System.currentTimeMillis());
              calendar.add(Calendar.SECOND, 5);

              AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
              alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
              // 或者以下面方式简化
              // alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5*1000,
              // sender);
             // alarm.cancel(sender);
              Toast.makeText(this, "五秒后alarm开启", Toast.LENGTH_LONG).show();
    }
}
