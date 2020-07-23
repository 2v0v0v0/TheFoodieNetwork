package com.fbu.thefoodienetwork;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

public class MyBroadcastReceiver extends ParsePushBroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Log.e(TAG, "onPushReceive");
    }
}
