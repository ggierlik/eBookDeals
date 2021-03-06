package com.ggierlik.ebookdeals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BookDeals extends Activity {
	// private PendingIntent alarmSender;

//	private static final String TAG = "BookDeals";

	// synced with updatefeeds.py for GAE part
//	private static final String PUBLISHER_APRESS = "Apress";
//	private static final String PUBLISHER_MSPRESS = "Microsoft Press";
//	private static final String PUBLISHER_OREILLY = "O'Reilly Media";
//	private static final String PUBLISHER_INFORMIT = "informIT";
//	private static final String PUBLISHER_MANNING = "Manning Books";

	private ArrayList<Book> books; // = new ArrayList<Book>();
	private ArrayBookAdapter arrayBookAdapter;

	private ListView lv;
	private ProgressBar pb;
	private TextView msg;

	private final String PATH = "http://ebook-deals.appspot.com/get_deals";
	public static final String BOOKDEALS_FILE = "eBookDealsFile";

	// private final String PATH = "http://10.0.2.2:8080/get_deals";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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

		//Log.d(TAG, "Finding pb...");
		pb = (ProgressBar) findViewById(R.id.pbHorizontal);

		if (pb == null) {
			//Log.e(TAG, "pb is null");
		}

		msg = (TextView) findViewById(R.id.loading);

		if (msg == null) {
			//Log.e(TAG, "msg is null");

		}
		//Log.d(TAG, "Creating books list...");

		Serializable item = null;

		if (savedInstanceState != null) {
			item = savedInstanceState.getSerializable("books");
		}

		if (item != null) {
			books = (ArrayList<Book>) item;

			//Log.d(TAG, String.format("Found %d books in saved state", books.size()));

			// if (pb != null) {
			pb.setVisibility(View.GONE);
			// }

			if (msg != null) {
				msg.setText(R.string.deals_found);
			}

		} else {
			books = new ArrayList<Book>();
			//Log.d(TAG, "New books list created");
		}

		//Log.d(TAG, "Creating books adapter...");
		arrayBookAdapter = new ArrayBookAdapter(this, R.layout.booklist_item,
				books);

		//Log.d(TAG, "Managing lv...");

		lv = (ListView) findViewById(R.id.list);

		lv.setTextFilterEnabled(false);
		lv.setAdapter(arrayBookAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Uri uri;
				try {
					uri = Uri.parse(books.get(position).getUrl());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);

					startActivity(intent);

				} catch (Exception ex) {
					// //Log.e(TAG, ex.getMessage());
					Toast.makeText(getApplicationContext(),
							position + "/" + id + "\n", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		if (books.isEmpty()) {
			loadBooksAsynchronously();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("books", books);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}

	// based on
	// http://www.android4devs.pl/2011/08/asynctask-asynchroniczne-wykonywanie-czasochlonnych-zadan/
	private class BooksLoaderTask extends AsyncTask<Void, Integer, Void> {

		private static final String TAG = "BooksLoaderTask";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			//Log.d(TAG, "preExecute starts...");

			// if (pb != null) {
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(0);
			// } else {
			// //Log.e(TAG, "pb is null");
			// }

			msg.setTextColor(Color.WHITE);
			msg.setText(R.string.loading);

			//Log.d(TAG, "preExecute ends...");
		}

		@Override
		protected Void doInBackground(Void... params) {

			//Log.d(TAG, "doInBackground stats...");

			JSONArray jsonBooks = loadBooks();

			SharedPreferences settings = getSharedPreferences(BOOKDEALS_FILE, 0);
			SharedPreferences.Editor editor = settings.edit();

			editor.putLong("LastUpdate", (new Date()).getTime());

			//Log.d(TAG, "Books have been read");

			books.clear();

			if (jsonBooks != null) {
				//Log.d(TAG, "and there are some... :)");

				int len = jsonBooks.length();

				for (int i = 0; i < len; i++) {
					try {
						JSONObject b = jsonBooks.getJSONObject(i);

						books.add(new Book(b.getString("publisher"), b
								.getString("title"), b.getString("link")));

						editor.putString(b.getString("publisher") + ":TITLE",
								b.getString("title"));
						editor.putString(b.getString("publisher") + ":LINK",
								b.getString("link"));

						publishProgress(i / len);

					} catch (JSONException ex) {
						//Log.e(TAG, ex.getMessage());
					}
				}
			}

			editor.commit();

			//Log.d(TAG, "doInBackground ends...");

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

			//Log.d(TAG, "onPostExecute stats...");

			super.onPostExecute(result);
			if (pb != null) {
				pb.setVisibility(View.GONE);
			}

			// msg.setVisibility(View.GONE);
			if (books.isEmpty()) {
				msg.setTextColor(Color.RED);
				msg.setText(R.string.no_deals);
			} else {
				msg.setText(R.string.deals_found);
			}

			lv.invalidateViews();

			//Log.d(TAG, "onPostExecute ends...");
		}

		private JSONArray loadBooks() {
			JSONArray jsonBooks = null;

			//Log.d(TAG, "Loading books...");

			try {
				URL url = new URL(PATH);

				//Log.d(TAG, "opening connection...");
				URLConnection conn = url.openConnection();

				//Log.d(TAG, "reading data...");
				InputStream is = conn.getInputStream();

				BufferedReader r = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));

				StringBuilder jsonInputBuffer = new StringBuilder();

				String line;

				while ((line = r.readLine()) != null) {
					jsonInputBuffer.append(line);
				}

				//Log.d(TAG, "converting to JSON...");
				String jsonInput = jsonInputBuffer.toString();

				jsonBooks = new JSONArray(jsonInput);

			} catch (Exception ex) {
				//Log.e(TAG, ex.getMessage());
			}

			return jsonBooks;
		}

	}

	private void loadBooksAsynchronously() {
		//Log.d(TAG, "Starting loading books asynchronously...");
		new BooksLoaderTask().execute();
	}

}