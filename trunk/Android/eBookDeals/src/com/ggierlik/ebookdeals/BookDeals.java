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

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BookDeals extends ListActivity {
	private PendingIntent alarmSender;
	
	private List<Book> books = new ArrayList<Book>();
	private ArrayBookAdapter arrayBookAdapter;

	private final String PATH = "http://ebook-deals.appspot.com/get_deals";
	//private final String PATH = "http://10.0.2.2:8080/get_deals";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		books.add(new Book(
				"O'Reilly Media",
				"In celebration of the one-year anniversary of the Microsoft Press/O'Reilly Media alliance, we've extended our Deal of the Day to the entire catalog of Microsoft Press ebooks. For one day only, you can SAVE 60% on hundreds of titles. Use...",
				"http://feedproxy.google.com/~r/oreilly/ebookdealoftheday/~3/QK5Xkn_xjGY/ddmsp.html"));
		books.add(new Book(
				"Microsoft Press",
				"In celebration of the one-year anniversary of the Microsoft Press/O'Reilly Media alliance, we've extended our Deal of the Day to the entire catalog of Microsoft Press ebooks. For one day only, you can SAVE 60% on hundreds of titles. Use...",
				"http://feedproxy.google.com/~r/oreilly/ebookdealoftheday/~3/QK5Xkn_xjGY/ddmsp.html"));
		books.add(new Book(
				"Manning Books",
				"ManningBooks: Deal of the Day! 40% off R in Action, PostGIS in Action, and Gnuplot in Action! Use code dotd1215tw at checkout.",
				"http://www.manning.com/bochicchio/"));
		books.add(new Book(
				"Apress",
				"apressdotd: iPhone User Interface Design Projects http://apress.com/info/dailydeal",
				"http://apress.com/info/dailydeal"));
		books.add(new Book(
				"informIT",
				"$9.99 eBook Deal of the Day :: Programming in Objective-C 2.0, 2nd Edition by Stephen G. Kochan",
				"http://www.informit.com/deals/"));
		*/
		
		alarmSender = PendingIntent.getService(BookDeals.this, 0, new Intent(BookDeals.this, FeedUpdaterService.class), 0);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 30*1000, alarmSender);
		
		if (loadBooks()) {
			arrayBookAdapter = new ArrayBookAdapter(this, R.layout.booklist_item, books);
		}
		
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(false);
		lv.setAdapter(arrayBookAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Toast.makeText(getApplicationContext(), position + "/" + id, Toast.LENGTH_SHORT).show();
				Uri uri;
				try {
					uri = Uri.parse(books.get(position).url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);

					startActivity(intent);

					
				}
				catch (Exception ex) {
					Toast.makeText(getApplicationContext(), position + "/" + id + "\n", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private boolean loadBooks() {
		
		boolean result = false;
		
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
				
				books.add(new Book(
						b.getString("publisher"),
						b.getString("title"),
						b.getString("link")));
			}
			
			result = true;
		}
		catch (IOException ioEx) {
			result = false;
		}
		catch (JSONException jsonEx) {
			result = false;
		}
		catch (Exception ex) {
			result = false;
		}
		
		return result;
	}
}