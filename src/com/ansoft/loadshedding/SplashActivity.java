package com.ansoft.loadshedding;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// ignore
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
				finish();
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				// startActivity(new Intent(SplashActivity.this, Home.class));
			}
		}.execute();
	}

}
