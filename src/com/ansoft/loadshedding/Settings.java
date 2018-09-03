package com.ansoft.loadshedding;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

	private SharedPreferences prefs;

	public Settings(Context context) {
		prefs = context.getSharedPreferences("settings.xml",
				Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
	}

	/* package */SharedPreferences getPrefs() {
		return prefs;
	}

	public String getEffDate() {
		return prefs.getString("effdate", null);
	}

	public void setEffDate(String date) {
		prefs.edit().putString("effdate", date).commit();
	}

	public String getLocUpdate() {
		return prefs.getString("loc_update", null);
	}

	public void setLocUpdate(String date) {
		prefs.edit().putString("loc_update", date).commit();
	}

	public int getDefaultGroup() {
		return prefs.getInt("group_id", -1);
	}

	public void setDefaultGroup(int groupId) {
		if (groupId < 0 || groupId >= 7) {
			throw new IllegalArgumentException("Wrong groupId value - "
					+ groupId);
		}
		prefs.edit().putInt("group_id", groupId).commit();
	}

	public boolean is12HoursTimeFormat() {
		return prefs.getBoolean("time_format", false);
	}

	public void setLastnotificationTime(long time) {
		prefs.edit().putLong("time", time).commit();
	}

	public long getLastNotificationTime() {
		return prefs.getLong("time", 0);
	}

	public boolean isNotificationEnabled() {
		return prefs.getBoolean("notification", true);
	}

	public boolean isChargeInstructionsEnabled() {
		return prefs.getBoolean("charge_instructions", true);
	}

	public boolean isAutoupdateEnabled() {
		return prefs.getBoolean("autoupdate", true);
	}

	public int getNotificationTime() {
		String time = prefs.getString("notification_time", "15");
		return Integer.parseInt(time);
	}

}
