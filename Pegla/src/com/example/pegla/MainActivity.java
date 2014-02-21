package com.example.pegla;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {
	private String tag = "Android Application";
	private CharSequence[] items = new CharSequence[] { "Google", "Apple",
			"Microsoft" };
	private boolean[] itemsChecked = new boolean[items.length];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Log.d(tag, "onCreate()");
	}

	public void onClick(View view) {
		showDialog(0);
	}

	public void onClickProgress(View view) {
		final ProgressDialog progressDialog = ProgressDialog.show(this, "Title", "Message", true);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					progressDialog.dismiss();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		thread.start();

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case 0:
			return new AlertDialog.Builder(this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("Pljugaj ga")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(getBaseContext(),
											"OK clicked!", Toast.LENGTH_SHORT)
											.show();

								}
							})
					.setNegativeButton("CANCEL",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(getBaseContext(),
											"Cancel clicked!",
											Toast.LENGTH_SHORT).show();
								}
							})
					.setMultiChoiceItems(items, itemsChecked,
							new DialogInterface.OnMultiChoiceClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									Toast.makeText(
											getBaseContext(),
											items[which]
													+ (isChecked ? " checked"
															: " unchecked"),
											Toast.LENGTH_SHORT).show();

								}
							}).create();

		}
		return null;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(tag, "onStart()");
	}

	@Override
	protected void onResume() {
		super.onStart();
		Log.d(tag, "onResume()");
	}

	@Override
	protected void onRestart() {
		super.onStart();
		Log.d(tag, "onRestart()");
	}

	@Override
	protected void onStop() {
		super.onStart();
		Log.d(tag, "onStop()");
	}

	@Override
	protected void onPause() {
		super.onStart();
		Log.d(tag, "onPause()");
	}

	@Override
	protected void onDestroy() {
		super.onStart();
		Log.d(tag, "onDestroy()");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
