package com.developinggeek.alarm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int id_alarm = 0;
    Context context;
    Button alarm_on , alarm_off ;
    TextView update_text;
    AlarmManager alarmManager;
    TimePicker alarm_timepicker;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        // typecasting
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm_on = (Button) findViewById(R.id.alarm_on);
        alarm_off = (Button) findViewById(R.id.alarm_off);
        update_text = (TextView) findViewById(R.id.update_text);
        final Calendar calendar = Calendar.getInstance();
        alarm_timepicker = (TimePicker) findViewById(R.id.alarm_timePicker);

        // creating intent to AlarmReceiver
        final Intent myIntent = new Intent(MainActivity.this,AlarmReceiver.class);

        // setting the alarm on
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                calendar.set(Calendar.HOUR_OF_DAY,alarm_timepicker.getCurrentHour());
                calendar.set(Calendar.MINUTE,alarm_timepicker.getCurrentMinute());

                int hour = alarm_timepicker.getCurrentHour();
                int minute = alarm_timepicker.getCurrentMinute();

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if (hour == 0) {
                    hour_string = String.valueOf(hour + 12);
                } else if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if(minute<10)
                {
                    minute_string = String.valueOf("0"+minute);
                }

              set_alarm_text("Alarm set to " + hour_string + ":" + minute_string);

                myIntent.putExtra("extra","alarm on");

              id_alarm = (int) System.currentTimeMillis();
              pendingIntent = PendingIntent.getBroadcast(MainActivity.this,id_alarm,myIntent,pendingIntent.FLAG_UPDATE_CURRENT);

              alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

            }
        });

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              set_alarm_text("Alarm off ");

              // cancel the alarm
              alarmManager.cancel(PendingIntent.getBroadcast(MainActivity.this,id_alarm,myIntent,pendingIntent.FLAG_UPDATE_CURRENT));

              myIntent.putExtra("extra","alarm off");

              // stopping the ringtone
              sendBroadcast(myIntent);

            }
        });

    }

    private void set_alarm_text(String output)
    { update_text.setText(output); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
