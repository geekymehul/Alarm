package com.developinggeek.alarm;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by DELL-PC on 6/13/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e("we are in","success");

        String get_your_key = intent.getExtras().getString("extra");

        Intent serviceIntent = new Intent(context,RingtonePlay.class);

        serviceIntent.putExtra("extra",get_your_key);
        Log.e("Key is",get_your_key);

        context.startService(serviceIntent);
    }
}
