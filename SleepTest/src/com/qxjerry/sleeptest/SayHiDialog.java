
package com.qxjerry.sleeptest;

import com.qxjerry.sleeptest.data.LocalDataHelper;
import com.qxjerry.sleeptest.data.SqliteDataHelper.SleepType;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SayHiDialog extends Activity {

    Button sayhiButton;

    TextView sayhiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sayhi);
        sayhiButton = (Button) findViewById(R.id.ok_button);
        sayhiText = (TextView) findViewById(R.id.sayhi_text);
        sayhiButton.setOnClickListener(buttonOnClickListner);
    }

    @Override
    protected void onResume() {
        SleepType sleepType = LocalDataHelper.getSleepStatus(this);
        switch (sleepType) {
            case GOTO_BED:
                sayhiButton.setText(R.string.good_morning);
                sayhiText.setText(R.string.good_morning_toast);
                break;

            case WAKEUP:
                sayhiButton.setText(R.string.good_night);
                sayhiText.setText(R.string.good_night_toast);

                break;

            default:
                sayhiButton.setText(R.string.good_morning);
                sayhiText.setText(R.string.good_morning_toast);
                break;
        }
        super.onResume();
    }

    private View.OnClickListener buttonOnClickListner = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.ok_button:
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

}
