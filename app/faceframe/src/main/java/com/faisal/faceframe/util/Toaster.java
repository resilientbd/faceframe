package com.faisal.faceframe.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Toaster extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      //  Log.d("chkbroadcast","recieved");
        String message = intent.getStringExtra(Constants.INTENT_MESSAGE);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
      //  Log.d("chkbroadcast","recieved toast:"+message);
    }
    public static void ShowText(String message)
    {
        Intent intent = new Intent(Constants.INTENT_MESSAGE);    //action: "msg"
        intent.setAction(Constants.INTENT_MESSAGE);
        intent.putExtra(Constants.INTENT_MESSAGE, message);
        GetContext.getApplicationUsingReflection().sendBroadcast(intent);
     //   Log.d("chkbroadcast","broadcast sent");
    }
}