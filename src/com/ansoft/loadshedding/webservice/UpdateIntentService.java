package com.ansoft.loadshedding.webservice;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.ansoft.loadshedding.LoadsheddingApplication;
import com.ansoft.loadshedding.R;
import com.ansoft.loadshedding.Settings;
import com.ansoft.loadshedding.provider.loadsheddingdayinfo.LoadsheddingDayInfoContentValues;
import com.ansoft.loadshedding.provider.loadsheddingdayinfo.LoadsheddingDayInfoSelection;

import retrofit.RetrofitError;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class UpdateIntentService extends IntentService {

	public static final String UPDATE_FINISHED = "UPDATE_FINISHED";

	private static final String ACTION_UPDATE = "com.zynga.android.apps.loadsheddingpro.action.UPDATE";
	private Handler handler;

	public UpdateIntentService() {
		super("UpdateIntentService");
		handler = new Handler();
	}

	/**
	 * @see IntentService
	 */
	public static void startActionUpdate(Context context) {
		startActionUpdate(context, false);
	}

	/**
	 * @see IntentService
	 */
	public static void startActionUpdate(Context context,
			boolean withNotification) {
		Intent intent = new Intent(context, UpdateIntentService.class);
		Bundle params = new Bundle();
		params.putBoolean("with_notification", withNotification);
		intent.putExtras(params);
		intent.setAction(ACTION_UPDATE);
		context.startService(intent);
	}

	public static void updateSchedule(Context context, Settings settings,
			LoadsheddingInfo info) {
		if (info == null) {
			throw new IllegalArgumentException("Information could not be null");
		}
		ContentResolver contentResolver = context.getContentResolver();
		settings.setEffDate(info.getEffdate());
		settings.setLocUpdate(info.getLocUpdate());
		if (info.getDayInfos() != null) {
			LoadsheddingDayInfoSelection selection = null;
			for (LoadsheddingDayInfo dayInfo : info.getDayInfos()) {
				LoadsheddingDayInfoContentValues values = new LoadsheddingDayInfoContentValues();
				selection = new LoadsheddingDayInfoSelection();
				selection.day(dayInfo.getDay()).and()
						.groupNumber(dayInfo.getGroup());

				values.putDay(dayInfo.getDay())
						.putGroupNumber(dayInfo.getGroup())
						.putTime(dayInfo.getTime());

				if (values.update(contentResolver, selection) == 0) {
					values.insert(contentResolver);
				}
			}
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_UPDATE.equals(action)) {
				Bundle params = intent.getExtras();
				handleActionUpdate(params.getBoolean("with_notification"));
			}
		}
	}

	/**
	 * Handle action Foo in the provided background thread with the provided
	 * parameters.
	 */
	private void handleActionUpdate(boolean withNotification) {
		LoadsheddingApplication application = (LoadsheddingApplication) getApplication();
		LoadsheddingWebService service = application.getService();
		Settings settings = application.getSettings();
		try {
			LoadsheddingInfo info = service.getLoadsheddingInfo();
			if (info != null) {
				String message = null;
				if (settings.getEffDate().equals(info.getEffdate())) {
					message = getString(R.string.update_up_to_date);
				} else {
					message = getString(R.string.update_successful);
				}
				updateSchedule(this, application.getSettings(), info);
				showToast(message, withNotification);
			} else {
				showToast(getString(R.string.update_failed), withNotification);
			}

		} catch (RetrofitError error) {
			showToast(getString(R.string.update_failed), withNotification);
		}
		application.recacheTime();
		LocalBroadcastManager.getInstance(this).sendBroadcast(
				new Intent(UPDATE_FINISHED));
	}

	private void showToast(final String text, boolean showToUser) {
		if (!showToUser) {
			return;
		}
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
						.show();
			}
		});
	}
}
