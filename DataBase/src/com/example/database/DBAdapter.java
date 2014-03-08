package com.example.database;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String EMAIL = "email";
	
	private static final String TABLE = "contacts";
	public static final String DATABASE = "MyDB";
	private static final int DATABASE_VERSION = 1;
	
	private static final String CREATE_TABLE = 
			"create table contacts ( " +
					"_id integer primary key autoincrement, " +
					"name text not null, " +
					"email text not null " +
			");";
	private static final String DROP_TABLE = "drop table if exists contacts";
	private static final String[] COLUMNS = new String[] {ID, NAME, EMAIL};
 	
	private Context context;
	private DBHelper dbHelper;
	private SQLiteDatabase sqLiteDatabase;
	
	public DBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DBHelper(context, DATABASE, null, DATABASE_VERSION);
	}
	
	public DBAdapter open() {
		sqLiteDatabase = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public boolean insert(String name, String email) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(NAME, name);
		contentValues.put(EMAIL, email);
		
		return sqLiteDatabase.insert(TABLE, null, contentValues) >= 0;
	}
	
	public Cursor findAllContacts() {
		return sqLiteDatabase.query(TABLE, COLUMNS, null, null, null, null, null);
	}
	
	public Cursor findContactById(Integer contactId) {
		return sqLiteDatabase.query(TABLE, COLUMNS, "_id = ? or name = ?", new String[] {contactId.toString(), "naleksijevic"}, null, null, null);
	}
	
	public boolean update(Integer contactId, String name, String email) {
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(NAME, name);
			contentValues.put(EMAIL, email);
			sqLiteDatabase.update(TABLE, contentValues, "_id = ?", new String[] {contactId.toString()});
			return true;
		} catch (Exception e) {
			Log.d("UPDATE ERROR", e.getMessage());
		}
		return false;
	}
	
	public boolean removeDuplicate() {
		try {
			String query = 
					"delete from contacts " +
					"where _id in (select _id from contacts where _id not in (select min(_id) from contacts group by name))";
			sqLiteDatabase.execSQL(query);
			return true;
		} catch (SQLException e) {
			Log.d("REMOVE ERROR", e.getMessage());
		}
		return false;
	}
	
	static private class DBHelper extends SQLiteOpenHelper {
		
		public DBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_TABLE);
			} catch (SQLException e) {
				Log.d("CREATE_TABLE", e.getMessage());
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_TABLE);
			db.setVersion(newVersion);
			onCreate(db);
		}
	}
}
