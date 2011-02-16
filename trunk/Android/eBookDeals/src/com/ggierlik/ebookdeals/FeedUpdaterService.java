//based on e:\android-sdk-windows\platforms\android-4\samples\ApiDemos\src\com\example\android\apis\app\AlarmService_Service.java

package com.ggierlik.ebookdeals;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.Toast;

public class FeedUpdaterService extends Service {

	NotificationManager mNM;
	
	public void onCreate() {
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		showNotification();
		
		Thread thr = new Thread(null, mTask, "FeedUpdaterService");
		thr.start();
	}
	
	public void onDestroy() {
		mNM.cancel(R.string.feed_updater_started);
	
		Toast.makeText(this, R.string.feed_updater_finished, Toast.LENGTH_SHORT).show();
	}
	
	Runnable mTask = new Runnable() {
		public void run() {
			//update feeds here
			FeedUpdaterService.this.stopSelf();
		}
	};
	
	public IBinder onBind(Intent i) {
		return mBinder;
	}
	
	private void showNotification() {
		CharSequence text = getText(R.string.feed_updater_started);
		Notification notification = new Notification(R.drawable.stat_sample, text, System.currentTimeMillis());
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, AlarmReceiver.class), 0);
		notification.setLatestEventInfo(this, getText(R.string.feed_updater_label), text, pi);
		mNM.notify(R.string.feed_updater_started, notification);
	}
	
	private final IBinder mBinder = new Binder() {
		protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
			return super.onTransact(code, data, reply,flags);
		}
	};
}
