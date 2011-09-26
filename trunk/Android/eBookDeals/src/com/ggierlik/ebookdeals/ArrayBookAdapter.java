package com.ggierlik.ebookdeals;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

// based on http://frogermcs.blogspot.com/2010/08/wasny-arrayadapter-wiele-informacji-w.html

public class ArrayBookAdapter extends ArrayAdapter<Book> {
	private int resource;
	
	public ArrayBookAdapter(Context context, int textViewResourceId, List<Book> books) {
		super(context, textViewResourceId, books);
		resource = textViewResourceId;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout bookView;
		
		Book book = getItem(position);
		
		if (null == convertView) {
			bookView = new RelativeLayout(getContext());
			
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(resource, bookView, true);
		}
		else {
			bookView = (RelativeLayout)convertView;
		}
		
		TextView tvPublisher = (TextView)bookView.findViewById(R.id.tvPublisher);
		TextView tvOffer = (TextView)bookView.findViewById(R.id.tvOffer);
		
		tvPublisher.setText(book.publisher);
		tvOffer.setText(book.offer);
		
		
		if (1 == position % 2) {
			tvPublisher.setBackgroundColor(Color.LTGRAY);
			tvOffer.setBackgroundColor(Color.LTGRAY);
		}
		
		
		return bookView;
	}
}
