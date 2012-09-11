
package com.qxjerry.sleeptest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

public class AlarmShowDialog extends Activity {

    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toast.makeText(this, "short alarm", Toast.LENGTH_LONG).show();
        mediaPlayer = MediaPlayer.create(this, R.raw.all_4_one);
        mediaPlayer.start();// 开始
        AlertDialog.Builder   alarmDialog =   new  AlertDialog.Builder(this);
        alarmDialog.setMessage("message");
        alarmDialog.setPositiveButton("关闭", new  OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
                dialog.dismiss();
                AlarmShowDialog.this.finish();
            }
        });
        alarmDialog.setNegativeButton("稍候",new   OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
                dialog.dismiss();
                AlarmShowDialog.this.finish();
            }
        });
        alarmDialog.show();

        super.onCreate(savedInstanceState);
    }
}
