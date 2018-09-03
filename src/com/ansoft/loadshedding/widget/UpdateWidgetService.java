package com.ansoft.loadshedding.widget;

import com.ansoft.loadshedding.R;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Ilya on 24.08.2014.
 */
public class UpdateWidgetService extends IntentService {

	/**
	 * Creates an IntentService. Invoked by your subclass's constructor.
	 * 
	 */
	public UpdateWidgetService() {
		super("UpdateWidgetService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		RemoteViews remoteViews = new RemoteViews(getApplicationContext()
				.getPackageName(), R.layout.loadshedding_app_widget);

		AppWidgetManager manager = AppWidgetManager
				.getInstance(getApplicationContext());
		final int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(
				getApplicationContext(), LoadsheddingAppWidget.class));

		for (int i = 0; i < appWidgetIds.length; i++) {
			LoadsheddingAppWidget.updateAppWidget(getApplicationContext(),
					manager, appWidgetIds[i]);
		}
	}
}
