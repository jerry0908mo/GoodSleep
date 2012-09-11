
package com.qxjerry.sleeptest;

import com.qxjerry.sleeptest.data.LocalDataHelper;
import com.qxjerry.sleeptest.data.SqliteDataHelper;
import com.qxjerry.sleeptest.data.SqliteDataHelper.SleepType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SleepTestActivity extends Activity {
    Button sleepbtn;
    Button gotolistBtn;
    Button alarmBtn;
    View    mainView  ;

    SleepType sleepStatus = SleepType.GOTO_BED;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mainView  =  findViewById(R.id.main_activity);
        sleepbtn = (Button) findViewById(R.id.sleep_btn);
        sleepbtn.setOnClickListener(btnOnClickListener);

        gotolistBtn = (Button) findViewById(R.id.list_btn);
        gotolistBtn.setOnClickListener(btnOnClickListener);

        alarmBtn =  (Button) findViewById(R.id.alert_btn);
        alarmBtn.setOnClickListener(btnOnClickListener);
    }

    @Override
    protected void onResume() {
        sleepStatus = LocalDataHelper.getSleepStatus(this);

        switch (sleepStatus) {
            case GOTO_BED:
                sleepbtn.setText(R.string.goto_bed);
                mainView.setBackgroundResource(R.drawable.morning);
                break;

            case WAKEUP:
                sleepbtn.setText(R.string.wake_up);
                mainView.setBackgroundResource(R.drawable.night);
                break;
            default:
                sleepbtn.setText(R.string.goto_bed);
                mainView.setBackgroundResource(R.drawable.morning);
                break;
        }

        super.onResume();
    }

    private void signIn() {
        SqliteDataHelper sqliteDataHelper = SqliteDataHelper.getDBHelper(SleepTestActivity.this);
        sqliteDataHelper.openDBHelper();

        sqliteDataHelper.addOneTime( sleepStatus);
        sqliteDataHelper.closeDBHelper();

        switch (sleepStatus) {
            case GOTO_BED:
                Toast.makeText(SleepTestActivity.this, R.string.good_night_toast,
                        Toast.LENGTH_SHORT).show();
                sleepStatus = SleepType.WAKEUP;
                break;

            case WAKEUP:
                Toast.makeText(SleepTestActivity.this, R.string.good_morning_toast,
                        Toast.LENGTH_SHORT).show();
                sleepStatus = SleepType.GOTO_BED;
                break;
            default:
                Toast.makeText(SleepTestActivity.this, R.string.good_night_toast,
                        Toast.LENGTH_SHORT).show();
                sleepStatus = SleepType.WAKEUP;
                break;
        }
        LocalDataHelper.setSleepStatus(SleepTestActivity.this, sleepStatus);
        startActivity(SayHiDialog.class);
    }

    private void startActivity(Class<?>  cls) {
        startActivity(new Intent(SleepTestActivity.this, cls));

    }
    private View.OnClickListener btnOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.list_btn:
                    startActivity(SleepListActivity.class);
                    break;

                case R.id.sleep_btn:
                    signIn();
                    break;
                case R.id.alert_btn:
                    startActivity(AlertSettingActivity.class);
                    break;
                default:
                    break;
            }
        }

    };
}
