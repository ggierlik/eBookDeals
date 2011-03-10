//based on e:\android-sdk-windows\platforms\android-4\samples\ApiDemos\src\com\example\android\apis\app\AlarmService_Service.java

package com.ggierlik.ebookdeals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

public class FeedUpdaterService extends Service {
	private static final int PERIOD = 1800000;// 30 secs	//10800000;	//3 hours
	
	NotificationManager mNM;
	AlarmManager alarms;
	PendingIntent alarmIntent;
	private UpdateFeedsAsyncTask lastLookup = null;
	
	public void onCreate() {
		
		super.onCreate();
		
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		alarms =  (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		String ALARM_ACTION;
		ALARM_ACTION = AlarmReceiver.ACTION_REFRESH_ALARM;
		
		Intent intentToFire = new Intent(ALARM_ACTION);
		alarmIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0); 
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Context context = getApplicationContext();
		
		Toast.makeText(this, "Start Update...", Toast.LENGTH_SHORT).show();
		
		int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP ;
		long timeToRefresh = SystemClock.elapsedRealtime() + PERIOD;
		
		alarms.setInexactRepeating(alarmType, timeToRefresh, PERIOD, alarmIntent);
		
		return 2; //Service.START_NOT_STICKY; //.START_NOT_STICKY;
	}
//	public void onStart(Intent intent, int val) {
//		super.onStart(intent, val);
//		
//		Toast.makeText(this, "Start Update...", Toast.LENGTH_SHORT).show();
//		
//		showNotification();
//		
//		Thread thr = new Thread(null, mTask, "FeedUpdaterService");
//		thr.start();
//		
//		//new UpdateFeedsAsyncTask().execute();
//		
//		stopSelf();
//	}
	
	private class UpdateFeedsAsyncTask extends AsyncTask<Void, Book, Void> {

		private final String PATH = "http://ebook-deals.appspot.com/get_deals";
		//private final String PATH = "http://10.0.2.2:8080/get_deals";
		
		protected Void doInBackground(Void... params) {
			try {
				URL url = new URL(PATH); 
				
				URLConnection conn = url.openConnection();
				InputStream is = conn.getInputStream();
				
				BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				
				StringBuilder jsonInputBuffer = new StringBuilder();
				
				String line;
				
				while ((line = r.readLine()) != null) {
					jsonInputBuffer.append(line);
				}
				
				String jsonInput = jsonInputBuffer.toString();
				
				JSONArray jsonBooks = new JSONArray(jsonInput);
				
				int len = jsonBooks.length();
				
				for (int i = 0; i < len; i++) {
					JSONObject b = jsonBooks.getJSONObject(i);
					
					Book book = new Book(
							b.getString("publisher"),
							b.getString("title"),
							b.getString("link"));
					
					addNewBook(book);
				}
				
				//result = true;
			}
			catch (IOException ex) {
				//result = false;
				ex.printStackTrace();
			}
			catch (JSONException ex) {
				//result = false;
				ex.printStackTrace();
			}
			catch (Exception ex) {
				//result = false;
				ex.printStackTrace();
			}
			
			return null;
		}
		
		protected void onPostExecute(Void result) {
			stopSelf();
		}
	}
	
	public void onDestroy() {
		mNM.cancel(R.string.feed_updater_started);
	
		Toast.makeText(this, R.string.feed_updater_finished, Toast.LENGTH_SHORT).show();
	}
	
//	Runnable mTask = new Runnable() {
//		public void run() {
//			//update feeds here
//			
//			//FeedUpdaterService.this.setAlarm(PERIOD);
//			FeedUpdaterService.this.stopSelf();
//		}
//	};
	
	public IBinder onBind(Intent i) {
		return null;
	}
	
	private void addNewBook(Book _book) {
		ContentResolver cr = getContentResolver();
		
		
		
	}
	
	private void showNotification() {
		CharSequence text = getText(R.string.feed_updater_started);
		
		Notification notification = new Notification(R.drawable.stat_sample, text, System.currentTimeMillis());
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, AlarmReceiver.class), 0);
		
		notification.setLatestEventInfo(this, getText(R.string.feed_updater_label), text, pi);
		mNM.notify(R.string.feed_updater_started, notification);
	}
	
	private void refreshDeals() {
		if (lastLookup == null || lastLookup.getStatus().equals(AsyncTask.Status.FINISHED )) {
			lastLookup = new UpdateFeedsAsyncTask();
			lastLookup.execute((Void[])null);
		}
	}
	
	
//	private final IBinder mBinder = new Bider() {
//		protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
//			return super.onTransact(code, data, reply,flags);
//		}
//	};
	
//	private void setAlarm(long period) {
//		alarms.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + period, AlarmManager.INTERVAL_HALF_DAY, alarmIntent);
//	}
}
