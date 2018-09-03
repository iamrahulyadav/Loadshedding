package com.ansoft.loadshedding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.ansoft.loadshedding.widget.UpdateWidgetService;

/**
 *
 */
public class SettingsActivity extends SherlockPreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	private ListPreference notificationTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setIcon(R.drawable.null_icon);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.veiw_actionbar_settings);

		addPreferencesFromResource(R.xml.prefs);
		CheckBoxPreference notificationEnabled = (CheckBoxPreference) findPreference("notification");
		notificationTime = (ListPreference) findPreference("notification_time");
		notificationTime.setEnabled(notificationEnabled.isChecked());
		notificationTime.setSummary(notificationTime.getEntry());
		getSharedPreferences(null, -1)
				.registerOnSharedPreferenceChangeListener(this);
		notificationEnabled
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						findPreference("notification_time").setEnabled(
								newValue == Boolean.TRUE);
						getSharedPreferences(null, -1)
								.registerOnSharedPreferenceChangeListener(
										SettingsActivity.this);
						return true;
					}
				});
	}

	@Override
	public SharedPreferences getSharedPreferences(String name, int mode) {
		return ((LoadsheddingApplication) getApplication()).getSettings()
				.getPrefs();
	}

	@Override
	protected void onPause() {
		Intent intent = new Intent(this, UpdateWidgetService.class);
		startService(intent);
		super.onPause();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals("notification_time")) {
			notificationTime.setSummary(notificationTime.getEntry());
		}
	}
}
