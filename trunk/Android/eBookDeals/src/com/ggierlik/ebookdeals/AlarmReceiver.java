package com.ggierlik.ebookdeals;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	public static final String ACTION_REFRESH_ALARM = "com.ggierlik.ebookdeals.REFRESH_ALARM";
	
	public void onReceive(Context context, Intent intent) {
		try {
			//Bundle bundle = intent.getExtras();
			//String message = "Updating feeds..."; //bundle.getString("");
			Toast.makeText(context, "Updating feeds...", Toast.LENGTH_SHORT ).show();
			
			//AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			
			//PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
			
			//mgr.setInexactRepeating (AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pi);
			
			//based on http://stackoverflow.com/questions/3599351/android-broadcast-receiver
			//based on Professional Android Development 
			Intent startIntent = new Intent(context, FeedUpdaterService.class);
			context.startService(startIntent);
		
		}
		catch (Exception ex) {
			Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
		}
	}

}
