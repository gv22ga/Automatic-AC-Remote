package com.example.gaurav.irremote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by gaurav on 19/5/17.
 */
public class RemoteReceiver extends BroadcastReceiver {

    private String log_tag = "ir";
    private int status = 0;
    private int start = 1;
    ConsumerIrManager irmanager;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("com.example.gaurav.irremote_preferences", Context.MODE_PRIVATE);
        status = prefs.getInt("status", 0);
        start = prefs.getInt("start", 1);
        Log.e(log_tag, "broadcast received!" +Integer.toString(start));


        if (start == 0) {
            start = 1;
            startAC(context);
        } else if (start == 1) {
            start = 0;
            stopAC(context);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("start", start);
        editor.apply();
    }

    public static void transmitIR (int frame[], Context c) {
        ConsumerIrManager irmanager = (ConsumerIrManager) c.getSystemService(Context.CONSUMER_IR_SERVICE);

        int f = 38000;
        int i = frame.length;
        for (int k = 0; k < i; k++) {
            frame[k] = (int) ((1000000.0f * ((float) frame[k])) / ((float) f));
        }
        irmanager.transmit(f,frame);
    }

    public static void startAC(Context c) {
        int start_frame[] = {346,173,29,60,28,17,28,16,28,16,29,16,29,16,28,60,28,16,28,16,29,16,28,16,28,60,28,16,28,17,28,16,28,16,28,17,28,16,28,16,29,16,29,16,28,16,29,16,28,16,28,17,28,16,28,16,28,17,28,60,29,16,28,60,29,16,28,16,28,60,28,16,29,5000};
        transmitIR(start_frame, c);
    }

    public static void stopAC(Context c) {
        int stop_frame[] = {346,173,28,60,28,16,28,16,29,60,28,60,28,16,29,60,28,16,28,17,28,16,28,17,28,60,29,16,28,16,28,16,29,16,28,16,28,16,29,16,28,16,28,17,28,16,28,16,29,16,28,16,28,17,28,16,28,16,28,60,28,16,28,60,28,17,28,16,28,60,28,16,28,5000};
        transmitIR(stop_frame, c);
    }
}
