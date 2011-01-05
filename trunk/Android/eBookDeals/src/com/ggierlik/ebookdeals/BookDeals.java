package com.ggierlik.ebookdeals;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BookDeals extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		arrayBookAdapter = new ArrayBookAdapter(this, R.layout.booklist_item, books);
		
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

	private List<Book> books = new ArrayList<Book>();
	private ArrayBookAdapter arrayBookAdapter;
}