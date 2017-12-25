package com.developinggeek.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.security.Provider;


public class RingtonePlay extends Service {

    MediaPlayer media_song;
    boolean isRunning;
    int startId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        String state = intent.getExtras().getString("extra");

        assert state != null;
        switch (state)
        {
            case "alarm on":
                startId = 1;
                break;

            case "alarm off":
                startId = 0;
                break;

            default:
                startId = 0;
                break;
        }

        // music is not playing and alarm is on play music
        if(!this.isRunning && startId==1)
        {
            media_song = MediaPlayer.create(this,R.raw.dove);
            media_song.start();
            media_song.setLooping(true);

            this.isRunning = true;
            this.startId = 0;

            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            Intent intent_mainActivity = new Intent(this.getApplicationContext(),MainActivity.class);

            //int id_notify = (int) System.currentTimeMillis();
            PendingIntent pendingIntent_MainActivity = PendingIntent.getActivity(this,0,intent_mainActivity,0);

            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Alarm is Active")
                    .setContentText("Touch to cancel")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent_MainActivity)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            notificationManager.notify(0,notification);


        }
        // music is playing and alarm off is pressed turn off the music
        else if(this.isRunning && startId==0)
        {
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0 ;
        }

        // these are just to bug-proof the app
        // music is not playing and alarm is off do nothing
        else if(!this.isRunning && startId==0)
        {
          this.isRunning = false;
          this.startId = 0;
        }

        // music is playing and alarm is on do nothing
        else if(this.isRunning && startId==1)
        {
          this.isRunning = true;
          this.startId = 1;
        }
        else
        {

        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
       super.onDestroy();
       this.isRunning = false;
    }

}
