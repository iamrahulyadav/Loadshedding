package com.ansoft.loadshedding;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.ansoft.loadshedding.webservice.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationsActivity extends SherlockFragmentActivity implements
		AdapterView.OnItemClickListener {

	private ListView locationsList;

	private EditText searchText;

	private SimpleAdapter adapter;

	private List<Map<String, String>> locations = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setIcon(R.drawable.null_icon);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(
				R.layout.veiw_actionbar_search_group);
		searchText = (EditText) findViewById(R.id.search);
		searchText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				onSearchTextChanged();
			}
		});
		locations = new ArrayList<Map<String, String>>();
		locationsList = (ListView) findViewById(R.id.list);
		adapter = new SimpleAdapter(this, locations, R.layout.adapter_location,
				new String[] { "name", "group" }, new int[] {
						android.R.id.text1, android.R.id.text2 });
		locationsList.setAdapter(adapter);
		locationsList.setOnItemClickListener(this);
		onSearchTextChanged();
	}

	private void onSearchTextChanged() {
		String searchString = searchText.getText() != null ? searchText
				.getText().toString() : "";
		List<Location> locationsData = ((LoadsheddingApplication) getApplication())
				.getLocationInfo().getLocations();
		Collections.sort(locationsData);
		locations.clear();
		for (Location location : locationsData) {
			if (searchString.length() == 0
					|| location.getName().toLowerCase()
							.contains(searchString.toLowerCase())
					|| ("Group " + location.getGroup()).toLowerCase().contains(
							searchString.toLowerCase())
					|| location.getCity().toLowerCase()
							.contains(searchString.toLowerCase())) {
				Map<String, String> locInfo = new HashMap<String, String>();
				locInfo.put("name", location.getName());
				locInfo.put("group", "Group " + location.getGroup());
				locInfo.put("groupId", location.getGroup());
				locations.add(locInfo);
			}
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("group",
				Integer.parseInt(locations.get(position).get("groupId")));
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}
}
