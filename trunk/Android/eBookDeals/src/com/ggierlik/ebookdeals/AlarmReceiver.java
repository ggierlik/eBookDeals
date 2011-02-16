package com.ggierlik.ebookdeals;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	private static final int PERIOD = 1800000;// 30 secs	//10800000;	//3 hours
	
	public void onReceive(Context context, Intent intent) {
		try {
			//Bundle bundle = intent.getExtras();
			String message = "Updating feeds..."; //bundle.getString("");
			Toast.makeText(context, message, Toast.LENGTH_SHORT ).show();
			
			AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			
			//PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
			
			//mgr.setInexactRepeating (AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pi);
		
		}
		catch (Exception ex) {
			Toast.makeText(context, "Something goes wrong", Toast.LENGTH_SHORT).show();
		}
	}

}
