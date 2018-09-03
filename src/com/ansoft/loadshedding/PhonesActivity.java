package com.ansoft.loadshedding;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ansoft.loadshedding.webservice.Phone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhonesActivity extends SherlockFragmentActivity {

	private PhoneAdapter adapter;
	private ListView locationsList;
	private ImageButton tallfree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phones);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setIcon(R.drawable.null_icon);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.veiw_actionbar_complain);
		locationsList = (ListView) findViewById(R.id.list);
		Location lastKnownLocation = ((LoadsheddingApplication) getApplication())
				.getLocationHandler().getLastKnownLocation();

		List<Phone> phoneList = new ArrayList(
				((LoadsheddingApplication) getApplication()).getPhoneInfo()
						.getLocations());

		List<Phone> phoneListForNearest = new ArrayList<Phone>();

		if (lastKnownLocation != null) {
			for (int i = 0, n = phoneList.size(); i < n; i++) {
				if (phoneList.get(i).getGeo().length() != 0) {
					String[] geo = phoneList.get(i).getGeo().split(",");
					float[] distance = new float[1];
					Location.distanceBetween(Double.parseDouble(geo[0]),
							Double.parseDouble(geo[1]),
							lastKnownLocation.getLatitude(),
							lastKnownLocation.getLongitude(), distance);
					phoneList.get(i).setDistance(distance[0]);
					phoneListForNearest.add(phoneList.get(i));
				}
			}
		}

		Collections.sort(phoneListForNearest);

		if (phoneListForNearest.size() > 0) {
			Phone nearestTitle = new Phone();
			nearestTitle.setCode("");
			nearestTitle.setName("Nearest Locations".toUpperCase());
			nearestTitle.setPhone("");
			nearestTitle.setGeo("");
			phoneList.add(0, nearestTitle);
			for (int i = 0; i < 3; i++) {
				phoneList.add(i + 1, phoneListForNearest.get(i));
			}
		}

		adapter = new PhoneAdapter(this, phoneList);
		locationsList.setAdapter(adapter);

		tallfree = (ImageButton) findViewById(R.id.tallfree);
		tallfree.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
						"tel", "16600130303", null));
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onPause() {
		((LoadsheddingApplication) getApplication()).getLocationHandler()
				.stopListening();
		super.onPause();
	}

	@Override
	protected void onResume() {
		((LoadsheddingApplication) getApplication()).getLocationHandler()
				.startListening();
		super.onResume();
	}

}
