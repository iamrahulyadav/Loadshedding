package com.ansoft.loadshedding.notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.ansoft.loadshedding.BuildConfig;
import com.ansoft.loadshedding.Home;
import com.ansoft.loadshedding.R;
import com.ansoft.loadshedding.Settings;
import com.ansoft.loadshedding.provider.loadsheddingdayinfo.LoadsheddingDayInfoCursor;
import com.ansoft.loadshedding.provider.loadsheddingdayinfo.LoadsheddingDayInfoSelection;
import com.ansoft.loadshedding.utils.DateUtils;
import com.ansoft.loadshedding.widget.LoadsheddingAppWidget;

import java.util.Calendar;
import java.util.Date;

public class EventBroadcastReceiver extends BroadcastReceiver {

	private static final float CRITICAL_BATTERY_LEVEL = 0.3f;
	private static final int NOTIFICATION_CHARGE_ID = 110;
	private static final int NOTIFICATION_ID = 111;
	private static final int NOTIFY_CHARGE_PERIOD = 60;
	private static final String TAG = "EventBroadcastReceiver";

	public static void scheduleEvent(Context context) {
		Intent myIntent = new Intent(context.getApplicationContext(),
				EventBroadcastReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				myIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// PendingIntent pIntent = PendingIntent.getBroadcast(context, 0,
		// myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		// boolean alarmUp = pIntent != null;
		// if (alarmUp) {
		// if (BuildConfig.DEBUG) Log.i(TAG, "Alarm is already active");
		// } else {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		if (BuildConfig.DEBUG)
			Log.i(TAG, calendar.toString());
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), 5 * 60 * 1000L, pendingIntent);
		// }

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Broadcast received");
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			scheduleEvent(context);
			LoadsheddingAppWidget.scheduleWidgetUpdate(context);
			return;
		}

		Settings settings = new Settings(context.getApplicationContext());
		Log.i(TAG, "Notification enabled: " + settings.isNotificationEnabled());
		if (!settings.isNotificationEnabled()) {
			return;
		}

		int currentWeekDay = DateUtils.getCurrentWeekday();
		int groupNumber = settings.getDefaultGroup();
		String timeValue = getDayInfo(context, groupNumber + 1, context
				.getResources().getTextArray(R.array.weekdays)[currentWeekDay]
				.toString());
		int nextDayNumber = (currentWeekDay == 6) ? 0 : currentWeekDay + 1;
		String nextDayTimeValue = getDayInfo(context, groupNumber + 1, context
				.getResources().getTextArray(R.array.weekdays)[nextDayNumber]
				.toString());
		if (timeValue == null || nextDayTimeValue == null) {
			return;
		}
		int[] timeLeftValue = DateUtils.getTimeToNextAction(timeValue,
				nextDayTimeValue);
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		int notifyMinutes = settings.getNotificationTime();
		int leftMinutes = timeLeftValue[0] * 60 + timeLeftValue[1];
		Log.i(TAG, "Left - " + leftMinutes + " notify " + notifyMinutes);
		if (leftMinutes >= notifyMinutes - 1
				&& leftMinutes <= notifyMinutes + 1) {
			Intent intentToStart = new Intent(context, Home.class);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					context)
					.setContentIntent(
							PendingIntent.getActivity(context, 1,
									intentToStart, 0))
					.setAutoCancel(true)
					.setVibrate(new long[] { 0, 1000, 100, 500 })
					.setSound(
							RingtoneManager
									.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
					.setContentTitle(context.getString(R.string.app_name));
			if (DateUtils.isOn(timeValue)) {
				builder.setSmallIcon(R.drawable.ic_off);
				builder.setContentText("Power will be cut off after "
						+ leftMinutes + " minutes");
			} else {
				builder.setSmallIcon(R.drawable.ic_on);
				builder.setContentText("Power will be on after " + leftMinutes
						+ " minutes");
			}
			manager.notify(NOTIFICATION_ID, builder.getNotification());
		} else if (leftMinutes > notifyMinutes + 1) {
			Log.i(TAG, "Cancelled ");
			manager.cancel(NOTIFICATION_ID);
		}
		if (leftMinutes >= NOTIFY_CHARGE_PERIOD - 1
				&& leftMinutes <= NOTIFY_CHARGE_PERIOD + 1) {
			IntentFilter ifilter = new IntentFilter(
					Intent.ACTION_BATTERY_CHANGED);
			Intent batteryStatus = context.getApplicationContext()
					.registerReceiver(null, ifilter);
			int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,
					-1);
			int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,
					-1);

			float batteryPct = level / (float) scale;

			if (batteryPct <= CRITICAL_BATTERY_LEVEL
					&& DateUtils.isOn(timeValue)
					&& settings.isChargeInstructionsEnabled()) {
				Intent intentToStart = new Intent(context, Home.class);
				NotificationCompat.Builder builder = new NotificationCompat.Builder(
						context)
						.setContentIntent(
								PendingIntent.getActivity(context, 1,
										intentToStart, 0))
						.setAutoCancel(true)
						.setVibrate(new long[] { 0, 1000, 100, 500 })
						.setSound(
								RingtoneManager
										.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
						.setContentTitle(context.getString(R.string.app_name));
				builder.setSmallIcon(R.drawable.ic_off);
				builder.setContentText("Charge your device! " + leftMinutes
						+ " min to power cut.");
				manager.notify(NOTIFICATION_CHARGE_ID,
						builder.getNotification());
			}
		}
	}

	private String getDayInfo(Context context, int groupNumber, String weekDay) {
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
}
