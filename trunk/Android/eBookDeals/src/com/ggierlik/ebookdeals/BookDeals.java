package com.ggierlik.ebookdeals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BookDeals extends ListActivity {
	// private PendingIntent alarmSender;

	private static final String TAG = "BookDeals";

	private List<Book> books; // = new ArrayList<Book>();
	private ArrayBookAdapter arrayBookAdapter;
	
	private ListView lv;
	private ProgressBar pb;
	
	private final String PATH = "http://ebook-deals.appspot.com/get_deals";

	// private final String PATH = "http://10.0.2.2:8080/get_deals";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * books.add(new Book( "O'Reilly Media",
		 * "In celebration of the one-year anniversary of the Microsoft Press/O'Reilly Media alliance, we've extended our Deal of the Day to the entire catalog of Microsoft Press ebooks. For one day only, you can SAVE 60% on hundreds of titles. Use..."
		 * ,
		 * "http://feedproxy.google.com/~r/oreilly/ebookdealoftheday/~3/QK5Xkn_xjGY/ddmsp.html"
		 * )); books.add(new Book( "Microsoft Press",
		 * "In celebration of the one-year anniversary of the Microsoft Press/O'Reilly Media alliance, we've extended our Deal of the Day to the entire catalog of Microsoft Press ebooks. For one day only, you can SAVE 60% on hundreds of titles. Use..."
		 * ,
		 * "http://feedproxy.google.com/~r/oreilly/ebookdealoftheday/~3/QK5Xkn_xjGY/ddmsp.html"
		 * )); books.add(new Book( "Manning Books",
		 * "ManningBooks: Deal of the Day! 40% off R in Action, PostGIS in Action, and Gnuplot in Action! Use code dotd1215tw at checkout."
		 * , "http://www.manning.com/bochicchio/")); books.add(new Book(
		 * "Apress",
		 * "apressdotd: iPhone User Interface Design Projects http://apress.com/info/dailydeal"
		 * , "http://apress.com/info/dailydeal")); books.add(new Book(
		 * "informIT",
		 * "$9.99 eBook Deal of the Day :: Programming in Objective-C 2.0, 2nd Edition by Stephen G. Kochan"
		 * , "http://www.informit.com/deals/"));
		 */

		// set alarm to call deal feed update as service
		// alarmSender = PendingIntent.getService(BookDeals.this, 0, new
		// Intent(BookDeals.this, FeedUpdaterService.class), 0);
		// AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		// am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		// SystemClock.elapsedRealtime(), 30*1000, alarmSender);

		// if (loadBooks()) {
		// arrayBookAdapter = new ArrayBookAdapter(this, R.layout.booklist_item,
		// books);
		// }

		Log.d(TAG, "Creating books list...");
		books = new ArrayList<Book>();
		
		Log.d(TAG, "Creating books adapter...");
		arrayBookAdapter = new ArrayBookAdapter(this, R.layout.booklist_item, books);

		Log.d(TAG, "Finding pb...");
		pb = (ProgressBar) findViewById(R.id.pbHorizontal);
		if (pb == null) {
			Log.e(TAG, "pb is null");
		}
		
		Log.d(TAG, "Managing lv...");
		lv = getListView();
		lv.setTextFilterEnabled(false);
		lv.setAdapter(arrayBookAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Uri uri;
				try {
					uri = Uri.parse(books.get(position).url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);

					startActivity(intent);

				} catch (Exception ex) {
					Log.e(TAG, ex.getMessage());
					Toast.makeText(getApplicationContext(),
							position + "/" + id + "\n", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		loadBooksAsynchronously();
	}

	/*
	 * private boolean loadBooks() {
	 * 
	 * boolean result = false;
	 * 
	 * try { URL url = new URL(PATH);
	 * 
	 * URLConnection conn = url.openConnection(); InputStream is =
	 * conn.getInputStream();
	 * 
	 * BufferedReader r = new BufferedReader(new InputStreamReader(is,
	 * "UTF-8"));
	 * 
	 * StringBuilder jsonInputBuffer = new StringBuilder();
	 * 
	 * String line;
	 * 
	 * while ((line = r.readLine()) != null) { jsonInputBuffer.append(line); }
	 * 
	 * String jsonInput = jsonInputBuffer.toString();
	 * 
	 * JSONArray jsonBooks = new JSONArray(jsonInput);
	 * 
	 * int len = jsonBooks.length();
	 * 
	 * for (int i = 0; i < len; i++) { JSONObject b =
	 * jsonBooks.getJSONObject(i);
	 * 
	 * books.add(new Book( b.getString("publisher"), b.getString("title"),
	 * b.getString("link"))); }
	 * 
	 * result = true; } catch (IOException ioEx) { Log.e(TAG,
	 * ioEx.getMessage()); result = false; } catch (JSONException jsonEx) {
	 * Log.e(TAG, jsonEx.getMessage()); result = false; } catch (Exception ex) {
	 * Log.e(TAG, ex.getMessage()); result = false; }
	 * 
	 * return result; }
	 */

	// based on http://www.android4devs.pl/2011/08/asynctask-asynchroniczne-wykonywanie-czasochlonnych-zadan/
	private class BooksLoaderTask extends AsyncTask<Void, Integer, Void> {
		
		private static final String TAG = "BooksLoaderTask";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			Log.d(TAG, "preExecute starts...");
			
			if (pb != null) {
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(0);
			}
			else {
				Log.e(TAG, "pb is null");
			}
			
			Log.d(TAG, "preExecute ends...");
		}

		@Override
		protected Void doInBackground(Void... params) {

			Log.d(TAG, "doInBackground stats...");
			
			JSONArray jsonBooks = loadBooks();

			Log.d(TAG, "Books have been read");
			
			if (jsonBooks != null) {
				Log.d(TAG, "and there are some... :)");
								
				int len = jsonBooks.length();

				for (int i = 0; i < len; i++) {
					try {
						JSONObject b = jsonBooks.getJSONObject(i);

						books.add(new Book(b.getString("publisher"), 
								b.getString("title"), 
								b.getString("link")));
						
						publishProgress(i/len);
						
					} 
					catch (JSONException ex) {
						Log.e(TAG, ex.getMessage());
					}
				}
			}

			Log.d(TAG, "doInBackground ends...");
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (pb != null) {
			pb.setProgress(values[0]);
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			
			Log.d(TAG, "onPostExecute stats...");
			
			super.onPostExecute(result);
			if (pb != null) {
			pb.setVisibility(View.GONE);
			}
			lv.invalidateViews();
			
			Log.d(TAG, "onPostExecute ends...");
		}

		private JSONArray loadBooks() {
			JSONArray jsonBooks = null;

			Log.d(TAG, "Loading books...");
			
			try {
				URL url = new URL(PATH);

				Log.d(TAG, "opening connection...");
				URLConnection conn = url.openConnection();
				
				Log.d(TAG, "reading data...");
				InputStream is = conn.getInputStream();

				BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				StringBuilder jsonInputBuffer = new StringBuilder();

				String line;

				while ((line = r.readLine()) != null) {
					jsonInputBuffer.append(line);
				}

				Log.d(TAG, "convering to JSON...");
				String jsonInput = jsonInputBuffer.toString();

				jsonBooks = new JSONArray(jsonInput);
				
			} catch (Exception ex) {
				Log.e(TAG, ex.getMessage());
			}

			return jsonBooks;
		}

	}

	private void loadBooksAsynchronously() {
		Log.d(TAG, "Starting loading books asynchronously...");
		new BooksLoaderTask().execute();
	}

}