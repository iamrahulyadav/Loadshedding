package com.ansoft.loadshedding;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);

		findViewById(R.id.mail).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri
						.fromParts("mailto", getString(R.string.email), null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						getString(R.string.mail_subject));
				startActivity(Intent.createChooser(emailIntent,
						getString(R.string.mailto_chooser_title)));
			}
		});
		findViewById(R.id.gplay).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri
						.parse("market://details?id=com.ansoft.loadshedding"));
				startActivity(intent);
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, 0);
	}
}
