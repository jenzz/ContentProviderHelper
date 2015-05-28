package com.jensdriller.contentproviderhelper.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.jensdriller.contentproviderhelper.app.ContentProviderHelper;

public class Database extends SQLiteOpenHelper {

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_URI = "uri";

	private static final int DB_VERSION = 1;
	private static final String DB_NAME = Database.class.getPackage().getName();
	private static final String DB_CREATE = "CREATE TABLE uris (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, uri TEXT) ";
	private static final String TABLE_NAME = "uris";
	private static final String DB_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

	private Context mContext;

	public Database(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(DB_CREATE);
		} catch (SQLiteException e) {
			ContentProviderHelper.handleException(mContext, e, true);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL(DB_DROP);
			onCreate(db);
		} catch (SQLException e) {
			ContentProviderHelper.handleException(mContext, e, true);
		}
	}

	public List<String> getAllUris() {
		List<String> uris = new ArrayList<String>();
		SQLiteDatabase db = null;
		Cursor cursor = null;

		try {
			db = getReadableDatabase();

			cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
			if (cursor.moveToFirst()) {
				do {
					uris.add(cursor.getString(cursor.getColumnIndex(COLUMN_URI)));
				} while (cursor.moveToNext());
			}
		} catch (SQLException e) {
			ContentProviderHelper.handleException(mContext, e, true);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}

		return uris;
	}

	public long insert(String uri) {
		SQLiteDatabase db = null;
		long id = -1;

		try {
			db = getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(COLUMN_URI, uri);
			id = db.insert(TABLE_NAME, null, values);
		} catch (SQLException e) {
			ContentProviderHelper.handleException(mContext, e, true);
		} finally {
			if (db != null) {
				db.close();
			}
		}

		return id;
	}

	public int delete(String uri) {
		SQLiteDatabase db = null;
		int count = 0;

		try {
			db = getWritableDatabase();

			count = db.delete(TABLE_NAME, COLUMN_URI + " = ?", new String[] { uri });
		} catch (SQLiteException e) {
			ContentProviderHelper.handleException(mContext, e, true);
		} finally {
			if (db != null) {
				db.close();
			}
		}

		return count;
	}
}
