package com.example.gaurav.irremote;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import org.json.JSONObject;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String log_tag = "ir";
    private int status = 0;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Button b1;
    TextView info;
    long interval_in_min=15;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.e(log_tag, "Main activity launched");

        settings = getApplicationContext().getSharedPreferences("com.example.gaurav.irremote_preferences", 0);
        status = settings.getInt("status", 0);

        ConsumerIrManager irmanager = (ConsumerIrManager) this.getSystemService(Context.CONSUMER_IR_SERVICE);
        boolean has_ir = irmanager.hasIrEmitter();

        if (!has_ir) {
            status = 2;
        }

        b1 = (Button) findViewById(R.id.b1);
        if (status==0) {
            b1.setText("start");
        } else if (status == 1){
            b1.setText("stop");
        } else {
            b1.setText("NO IR SENSOR");
        }

        info = (TextView) findViewById(R.id.info);
        // current city
        updateWeatherData("Varanasi");



        /*
        ConsumerIrManager.CarrierFrequencyRange cr[] = irmanager.getCarrierFrequencies();
        for(int i =0; i<cr.length; i++) {
            int maxf = cr[i].getMaxFrequency();
            int minf = cr[i].getMinFrequency();
            Log.e(log_tag,"Range: ("+Integer.toString(maxf)+","+Integer.toString(minf)+")");
        }
        */

        // works. yey!!!
        //int frame[] = {346,173,28,60,29,16,28,16,28,60,28,16,28,17,28,60,29,16,28,16,28,16,29,16,28,60,29,16,28,16,29,16,29,16,28,16,29,16,28,16,28,16,29,16,28,16,28,16,29,16,28,16,29,16,29,16,28,16,29,60,28,16,29,60,28,16,29,16,28,60,29,16,28,5000};
        //int start_frame[] = {346,173,28,60,28,16,28,16,29,60,28,60,28,16,29,60,28,16,28,17,28,16,28,17,28,60,29,16,28,16,28,16,29,16,28,16,28,16,29,16,28,16,28,17,28,16,28,16,29,16,28,16,28,17,28,16,28,16,28,60,28,16,28,60,28,17,28,16,28,60,28,16,28,5000};
        //int stop_frame[] = {346,173,29,60,28,17,28,16,28,16,29,16,29,16,28,60,28,16,28,16,29,16,28,16,28,60,28,16,28,17,28,16,28,16,28,17,28,16,28,16,29,16,29,16,28,16,29,16,28,16,28,17,28,16,28,16,28,17,28,60,29,16,28,60,29,16,28,16,28,60,28,16,29,5000};

    }

    /*
    "Power On"			: "346,173,28,60,29,16,28,16,28,60,28,16,28,17,28,60,29,16,28,16,28,16,29,16,28,60,29,16,28,16,29,16,29,16,28,16,29,16,28,16,28,16,29,16,28,16,28,16,29,16,28,16,29,16,29,16,28,16,29,60,28,16,29,60,28,16,29,16,28,60,29,16,28,5000",
    "Power Off"			: "346,173,29,60,28,17,28,16,28,16,29,16,29,16,28,60,28,16,28,16,29,16,28,16,28,60,28,16,28,17,28,16,28,16,28,17,28,16,28,16,29,16,29,16,28,16,29,16,28,16,28,17,28,16,28,16,28,17,28,60,29,16,28,60,29,16,28,16,28,60,28,16,29,5000",
    "Cool"			    : "38000,346,173,28,60,29,16,28,16,28,60,28,16,28,17,28,60,29,16,28,16,28,16,29,16,28,60,29,16,28,16,29,16,29,16,28,16,29,16,28,16,28,16,29,16,28,16,28,16,29,16,28,16,29,16,29,16,28,16,29,60,28,16,29,60,28,16,29,16,28,60,29,16,28,5000",
    "Dry"			    : "38000,346,173,29,16,29,60,30,16,29,60,30,60,29,16,29,60,29,16,29,16,29,16,29,16,29,60,29,16,29,16,29,16,28,16,29,16,29,16,29,16,29,16,29,16,29,16,29,16,28,16,29,16,29,16,28,16,29,16,29,60,29,16,29,60,29,16,29,16,29,60,30,16,29,5000",
    "Fan"  		        : "38000,346,173,30,60,30,60,29,16,30,60,29,60,30,16,29,60,29,16,29,16,29,16,29,16,29,60,29,16,30,16,29,16,29,16,30,16,29,16,29,16,29,16,29,16,29,16,29,16,29,16,29,16,30,16,29,16,30,16,29,60,30,16,29,60,30,16,29,16,29,60,29,16,30,5000",
    "Heat"              : "38000,346,173,29,16,28,16,29,60,29,60,29,60,29,16,29,60,29,16,29,16,28,16,29,16,29,60,29,16,29,16,29,16,29,16,29,16,29,16,29,16,28,16,29,16,29,16,28,16,29,16,29,16,29,16,29,16,29,16,28,60,28,16,29,60,29,16,29,16,29,60,29,16,28,5000",
    "Auto"              : "38000,346,173,29,16,28,16,29,16,29,60,29,60,29,16,29,60,29,16,29,16,30,16,29,16,29,16,28,16,29,16,29,16,29,16,29,16,29,16,29,16,29,16,30,16,29,16,29,16,28,16,29,16,29,16,29,16,29,16,29,60,29,16,28,60,29,16,28,16,29,60,29,16,29,5000",
    "Fan Auto"          : "38000,346,173,29,60,28,17,28,16,28,60,28,16,28,17,28,60,29,16,29,16,28,16,28,17,28,60,28,16,28,16,28,17,28,16,28,16,29,16,28,16,28,16,29,16,28,16,28,17,28,16,28,16,28,17,28,16,28,17,28,60,29,16,28,60,29,16,28,16,28,60,28,16,28,5000",
    "Fan High"          : "38000,346,173",
    "Fan Medium"		: "38000,346,173,28,60,28,16,28,17,28,60,29,16,28,60,29,60,28,16,29,16,28,16,28,17,28,60,28,16,28,16,29,16,28,16,28,16,29,16,28,16,28,16,28,17,28,16,28,17,28,16,28,16,28,17,28,16,28,17,28,60,28,17,28,60,28,17,28,16,28,60,28,16,28,5000",
    "Fan Low"			: "346,173,28,60,28,16,28,16,29,60,28,60,28,16,29,60,28,16,28,17,28,16,28,17,28,60,29,16,28,16,28,16,29,16,28,16,28,16,29,16,28,16,28,17,28,16,28,16,29,16,28,16,28,17,28,16,28,16,28,60,28,16,28,60,28,17,28,16,28,60,28,16,28,5000",
    */

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

    public void onB1Clicked(View v) {

        if (status == 0) {
            Log.e(log_tag,"start button clicked");
            AlarmManager alarmManager=(AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, RemoteReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval_in_min * 1000,
                    pendingIntent);
            Log.e(log_tag, "set repeating alarm");
            Log.e(log_tag, "interval= " + interval_in_min);


            intent = new Intent(this, StartStopReceiver.class);
            intent.putExtra("type", false);
            pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
            Calendar now = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 30);
            long alarmMillis = calendar.getTimeInMillis();
            if (calendar.before(now)) alarmMillis+= 86400000L;
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmMillis, pendingIntent);

            /*
            intent = new Intent(this, StartStopReceiver.class);
            intent.putExtra("type", false);
            pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 2);
            calendar.set(Calendar.MINUTE, 16);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);*/

            status = 1;
            editor = settings.edit();
            editor.putInt("status", status);
            editor.apply();

            b1.setText("stop");

        } else if (status == 1) {
            Log.e(log_tag,"stop button clicked");
            AlarmManager alarmManager=(AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, RemoteReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.cancel(pendingIntent);
            Log.e(log_tag, "alarm cancelled");

            /*AlarmManager alarmManager=(AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, StartStopReceiver.class);
            intent.putExtra("type", true);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.cancel(pendingIntent);

            intent = new Intent(this, StartStopReceiver.class);
            intent.putExtra("type", false);
            pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
            alarmManager.cancel(pendingIntent);*/

            status = 0;
            editor = settings.edit();
            editor.putInt("status", status);
            editor.apply();

            b1.setText("start");
        } else if (status == 2) {

        }


    }

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = WeatherFetch.getJSON(MainActivity.this, city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Log.e(log_tag, "error weather api");
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {
            JSONObject main = json.getJSONObject("main");
            Double temp = main.getDouble("temp");
            interval_in_min = (long)((40*7)/temp);
            info.setText(String.format("Current Temp: %.2f ℃\n" +
                    "\n" +
                    "AC will automatically be started/stopped every %d minutes", temp, interval_in_min));

            Log.e(log_tag, String.format("%.2f", main.getDouble("temp")) + " ℃");

        }catch(Exception e){
            info.setText("Error in collecting weather info!");
            Log.e(log_tag, "Error in collecting weather info!");
        }
    }

}
