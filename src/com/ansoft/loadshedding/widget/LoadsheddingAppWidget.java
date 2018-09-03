package com.ansoft.loadshedding.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.ansoft.loadshedding.FlashTorchActivity;
import com.ansoft.loadshedding.Home;
import com.ansoft.loadshedding.R;
import com.ansoft.loadshedding.Settings;
import com.ansoft.loadshedding.provider.loadsheddingdayinfo.LoadsheddingDayInfoCursor;
import com.ansoft.loadshedding.provider.loadsheddingdayinfo.LoadsheddingDayInfoSelection;
import com.ansoft.loadshedding.utils.DateUtils;

/**
 * Implementation of App Widget functionality. App Widget Configuration
 * implemented in {@link LoadsheddingAppWidgetConfigureActivity
 * LoadsheddingAppWidgetConfigureActivity}
 */
public class LoadsheddingAppWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// There may be multiple widgets active, so update all of them
		for (int i = 0; i < appWidgetIds.length; i++) {
			updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		for (int i = 0; i < appWidgetIds.length; i++) {
			LoadsheddingAppWidgetConfigureActivity.deleteGroupPref(context,
					appWidgetIds[i]);
		}
	}

	@Override
	public void onEnabled(Context context) {
		// Enter relevant functionality for when the first widget is created
	}

	@Override
	public void onDisabled(Context context) {
		// Enter relevant functionality for when the last widget is disabled
	}

	static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.loadshedding_app_widget);
		int groupNumber = LoadsheddingAppWidgetConfigureActivity.loadGroupPref(
				context, appWidgetId);

		if (groupNumber <= 0 || groupNumber > 7) {
			return;
		}

		views.setTextViewText(R.id.section_label,
				context.getString(R.string.group_number, groupNumber)
						.toUpperCase());

		int currentWeekDay = DateUtils.getCurrentWeekday();
		views.setTextViewText(R.id.dayOfWeek, context.getResources()
				.getTextArray(R.array.weekdaysCheckedShort)[currentWeekDay]
				.toString().toUpperCase());

		String timeValue = getDayInfo(context, groupNumber, context
				.getResources().getTextArray(R.array.weekdays)[currentWeekDay]
				.toString());
		int nextDayNumber = (currentWeekDay == 6) ? 0 : currentWeekDay + 1;
		String nextDayTimeValue = getDayInfo(context, groupNumber, context
				.getResources().getTextArray(R.array.weekdays)[nextDayNumber]
				.toString());

		int[] timeLeftValue = DateUtils.getTimeToNextAction(timeValue,
				nextDayTimeValue);

		views.setTextViewText(R.id.timeLeft, context.getString(
				R.string.time_format, timeLeftValue[0], timeLeftValue[1]));

		if (DateUtils.isOn(timeValue)) {
			views.setTextViewText(R.id.onOff, context.getString(R.string.won));
			views.setImageViewResource(R.id.onOffIndicator,
					R.drawable.ic_on_widget);
			views.setTextColor(R.id.onOff, Color.parseColor("#c6fc8b"));
			views.setTextColor(R.id.timeLeft, Color.parseColor("#c6fc8b"));
		} else {
			views.setTextViewText(R.id.onOff, context.getString(R.string.woff));
			views.setImageViewResource(R.id.onOffIndicator,
					R.drawable.ic_off_widget);
			views.setTextColor(R.id.onOff, Color.parseColor("#9c9c9c"));
			views.setTextColor(R.id.timeLeft, Color.parseColor("#9c9c9c"));
		}
		Settings settings = new Settings(context.getApplicationContext());
		if (settings.is12HoursTimeFormat()) {
			views.setTextViewText(R.id.time, DateUtils.toPmAmFormat(timeValue)
					.replace(',', '\n'));
		} else {
			views.setTextViewText(R.id.time, timeValue.replace(',', '\n'));
		}

		Intent flashLightIntent = new Intent(context, FlashTorchActivity.class);
		PendingIntent flashLightPendingIntent = PendingIntent.getActivity(
				context, appWidgetId, flashLightIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		views.setOnClickPendingIntent(R.id.flashLight, flashLightPendingIntent);

		Intent homeIntent = new Intent(context, Home.class);
		PendingIntent homePendingIntent = PendingIntent.getActivity(context,
				appWidgetId, homeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		views.setOnClickPendingIntent(R.id.time, homePendingIntent);

		Intent configureWidgetIntent = new Intent(context,
				LoadsheddingAppWidgetConfigureActivity.class);
		Bundle params = new Bundle();
		params.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		configureWidgetIntent.putExtras(params);
		PendingIntent configureWidgetPendingIntent = PendingIntent.getActivity(
				context, appWidgetId, configureWidgetIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		views.setOnClickPendingIntent(R.id.groupConfigure,
				configureWidgetPendingIntent);

		// Instruct the widget manager to update the widget
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	private static String getDayInfo(Context context, int groupNumber,
			String weekDay) {
		LoadsheddingDayInfoSelection selection = new LoadsheddingDayInfoSelection();
		LoadsheddingDayInfoCursor cursor = selection.day(weekDay).and()
				.groupNumber(groupNumber).query(context.getContentResolver());
		String time = null;
		if (cursor.moveToFirst()) {
			time = cursor.getTime();
		}
		cursor.close();
		return time;
	}

	public static void scheduleWidgetUpdate(Context context) {
		Intent intent = new Intent(context, UpdateWidgetService.class);
		PendingIntent pending = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarm = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pending);
		long start = SystemClock.elapsedRealtime();
		long interval = 15 * 1000L;
		alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, start, interval,
				pending);
	}
}
