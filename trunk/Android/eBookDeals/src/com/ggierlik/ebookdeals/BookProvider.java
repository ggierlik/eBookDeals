/*
package com.ggierlik.ebookdeals;

import java.sql.SQLException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class BookProvider extends ContentProvider {
	public static final Uri CONTENT_URI = Uri.parse("content://com.ggierlik.ebookdeals.provider.book/books");

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		
		int count;
		
		switch (uriMatcher.match(uri)) {
		case BOOKS:
			count = bookDB.delete(BOOK_TABLE, where, whereArgs);
			break;
			
		case BOOK_ID:
			String segment = uri.getPathSegments().get(1);
			
			count = bookDB.delete(BOOK_TABLE, KEY_ID + "="
					+ segment
					+ (!TextUtils.isEmpty(where) ? "AND ("
					+ where + ')' : ""), whereArgs);
			
			break;
			
			default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri _uri, ContentValues values) {
		
		long rowID = bookDB.insert(BOOK_TABLE, "book", values);
		
		if (rowID>0) {
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);

			return uri;

		}
		throw new SQLException("Failed to insert row into " + _uri);
	}

	@Override
	public boolean onCreate() {
		
		Context context = getContext();
		
		bookDatabaseHelper  dbHelper = new bookDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		bookDB = dbHelper.getWritableDatabase();
		return (bookDB == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
//		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//		
//		qb.setTables(BOOK_TABLE);
//		
//		switch (UriMatcher.match(uri)) {
//		case BOOK_ID: qb.appendWhere(inWhere)
//		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private SQLiteDatabase bookDB;
	
	private static final String DATABASE_NAME = "ebookdeals.db";
	private static final int DATABASE_VERSION = 1;
	private static final String BOOK_TABLE = "books";
	
	public static final String KEY_ID = "id";
	public static final String KEY_PUBLISHER = "publisher";
	public static final String KEY_OFFER = "offer";
	public static final String KEY_URL = "url";
	
	private static class bookDatabaseHelper extends SQLiteOpenHelper {
		private static final String DATABASE_CREATE = 
			"CREATE TABLE " + BOOK_TABLE + " ("
			+ KEY_ID + " integer primary key autoincrement, "
			+ KEY_PUBLISHER + " TEXT, "
			+ KEY_OFFER + " TEXT, "
			+ KEY_URL + " TEXT);";
			
		public bookDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE );
			onCreate(db);
		}
	}
}

*/