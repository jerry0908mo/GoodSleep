
package com.qxjerry.sleeptest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("short")) {
            Intent i = new Intent(context, AlarmShowDialog.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        } else {
            Toast.makeText(context, "repeating alarm", Toast.LENGTH_LONG).show();
        }
    }

}
