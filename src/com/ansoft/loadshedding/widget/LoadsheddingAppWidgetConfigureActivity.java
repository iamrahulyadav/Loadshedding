package com.ansoft.loadshedding.widget;

import com.ansoft.loadshedding.R;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * The configuration screen for the {@link LoadsheddingAppWidget
 * LoadsheddingAppWidget} AppWidget.
 */
public class LoadsheddingAppWidgetConfigureActivity extends Activity implements
		AdapterView.OnItemClickListener {

	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private ListView groupList;
	private static final String PREFS_NAME = "com.ansoft.loadshedding.widget.LoadsheddingAppWidget";
	private static final String PREF_PREFIX_KEY = "appwidget_";

	public LoadsheddingAppWidgetConfigureActivity() {
		super();
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Set the result to CANCELED. This will cause the widget host to cancel
		// out of the widget placement if the user presses the back button.
		setResult(RESULT_CANCELED);

		setContentView(R.layout.loadshedding_app_widget_configure);

		setTitle(getString(R.string.widget_configuration_title));

		// Find the widget id from the intent.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// If this activity was started with an intent without an app widget ID,
		// finish with an error.
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
			return;
		}

		groupList = (ListView) findViewById(R.id.groupList);
		ArrayAdapter<String> groupsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.groups));
		groupList.setAdapter(groupsAdapter);

		groupList.setOnItemClickListener(this);
	}

	// Write the prefix to the SharedPreferences object for this widget
	static void saveGroupPref(Context context, int appWidgetId, int groupId) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREFS_NAME, 0).edit();
		prefs.putInt(PREF_PREFIX_KEY + appWidgetId, groupId);
		prefs.commit();
	}

	// Read the prefix from the SharedPreferences object for this widget.
	// If there is no preference saved, get the default from a resource
	static int loadGroupPref(Context context, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		int groupIdValue = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, -1);
		return groupIdValue;
	}

	static void deleteGroupPref(Context context, int appWidgetId) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREFS_NAME, 0).edit();
		prefs.remove(PREF_PREFIX_KEY + appWidgetId);
		prefs.commit();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Context context = LoadsheddingAppWidgetConfigureActivity.this;

		saveGroupPref(context, mAppWidgetId, position + 1);

		// It is the responsibility of the configuration activity to update the
		// app widget
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		LoadsheddingAppWidget.updateAppWidget(context, appWidgetManager,
				mAppWidgetId);

		// Make sure we pass back the original appWidgetId
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();
	}
}
