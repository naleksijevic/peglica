package com.example.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String DATABASE_STORAGE = "/data/data/com.example.database/databases";
	private static final String ASSETS_DATABASE_FILE = "mydb";
	private static final int BLOCK_SIZE = 1024;
	
	private DBAdapter adapter;
	private EditText idEditText;
	private EditText nameEditText;
	private EditText emailEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		createDataBaseFile();
		adapter = new DBAdapter(this);
		
		idEditText = (EditText) findViewById(R.id.idEditText);
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		
	}
	
	private void createDataBaseFile() {
		File dataBaseStorageDir = new File(DATABASE_STORAGE);
		File dataBaseFile = new File(DATABASE_STORAGE + "/" + DBAdapter.DATABASE);
		
		if (!dataBaseFile.exists()) {
			try {
				dataBaseStorageDir.mkdirs();
				dataBaseFile.createNewFile();
				
				OutputStream outputStream = new FileOutputStream(dataBaseFile);
				copyDB(getBaseContext().getAssets().open(ASSETS_DATABASE_FILE), outputStream);
				
			} catch (IOException e) {
				Log.d("DATABASE_FILE_NOT_EXISTS", e.getMessage());
			}
		}
	}
	
	public void copyDB(InputStream inputStream, OutputStream outputStream) {
		byte[] buffer = new byte[BLOCK_SIZE];
		int countReadedBytes;
		
		try {
			while ((countReadedBytes = inputStream.read(buffer)) >= 0) {
				outputStream.write(buffer, 0, countReadedBytes);
				buffer = new byte[BLOCK_SIZE];
			}
		} catch (IOException e) {
			Log.d("READ_ERROR", e.getMessage());
		}
	}

	
	public void onClickShowAllContacts(View view) {
		adapter.open();
		Cursor cursor = adapter.findAllContacts();
		if (cursor.moveToFirst()) {
			do {
				showMessage(cursor);
			} while (cursor.moveToNext());
		}
		adapter.close();
	}
	
	public void onClickRemoveDuplicate(View view) {
		adapter.open();
		adapter.removeDuplicate();
		adapter.close();
	}
	
	public void onClickUpdateContact(View view) {
		adapter.open();
		Integer contactId = Integer.parseInt(idEditText.getText().toString().trim());
		String name = nameEditText.getText().toString().trim();
		String email = emailEditText.getText().toString().trim();
		
		boolean success = adapter.update(contactId, name, email);
		if (success) {
			showMessage("Update Success");
		} else {
			showMessage("Update Failure");
		}
		adapter.close();
	}
	
//	public void onClickShowAllContacts(View view) {
//		adapter.open();
//		Cursor cursor = adapter.findAllContacts();
//		if (cursor.moveToFirst()) {
//			do {
//				showMessage(cursor);
//			} while (cursor.moveToNext());
//		}
//		adapter.close();
//	}
	
	private void showMessage(Cursor cursor) {
		Toast.makeText(this, cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2), Toast.LENGTH_SHORT).show();
	}
	
	private void showMessage(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
