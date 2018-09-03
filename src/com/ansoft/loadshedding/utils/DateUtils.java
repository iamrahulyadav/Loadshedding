package com.ansoft.loadshedding.utils;

import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

	private DateUtils() {

	}

	public static int getCurrentWeekday() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	public static int[] getTimeToNextAction(String time, String nextDayTime) {
		int[] nextDayDigits = parseTime(nextDayTime);
		return getTimeToNextAction(time + "," + (nextDayDigits[0] + 24) + ":"
				+ nextDayDigits[1]);
	}

	public static int[] getTimeToNextAction(String time, int hours, int minutes) {
		int[] digits = parseTime(time);
		int currentHours = hours;
		int currentMinutes = minutes;
		int currentTimeInMinutes = currentHours * 60 + currentMinutes;

		int timeInMinutes = -1;

		for (int i = 0; i < digits.length / 2; i++) {
			timeInMinutes = digits[i * 2] * 60 + digits[i * 2 + 1]
					- currentTimeInMinutes;
			if (timeInMinutes >= 0)
				break;
		}
		return new int[] { timeInMinutes / 60,
				timeInMinutes - timeInMinutes / 60 * 60 };
	}

	public static int[] getTimeToNextAction(String time) {
		int[] digits = parseTime(time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int currentHours = calendar.get(Calendar.HOUR_OF_DAY);
		int currentMinutes = calendar.get(Calendar.MINUTE);
		int currentTimeInMinutes = currentHours * 60 + currentMinutes;

		int timeInMinutes = -1;

		for (int i = 0; i < digits.length / 2; i++) {
			timeInMinutes = digits[i * 2] * 60 + digits[i * 2 + 1]
					- currentTimeInMinutes;
			if (timeInMinutes >= 0)
				break;
		}
		return new int[] { timeInMinutes / 60,
				timeInMinutes - timeInMinutes / 60 * 60 };
	}

	public static String toPmAmFormat(String date24) {
		int[] digits24 = parseTime(date24);
		StringBuilder pmAmDateBuilder = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(0L));
		for (int i = 0; i < digits24.length; i += 2) {
			cal.set(Calendar.HOUR_OF_DAY, digits24[i]);
			int hour = cal.get(Calendar.HOUR);
			if (hour == 0) {
				hour = 12;
			}
			if (hour < 10) {
				pmAmDateBuilder.append("0");
			}
			pmAmDateBuilder.append(hour);
			pmAmDateBuilder.append(":");
			if (digits24[i + 1] < 10) {
				pmAmDateBuilder.append("0");
			}
			pmAmDateBuilder.append(digits24[i + 1]);
			pmAmDateBuilder.append(" ");
			pmAmDateBuilder
					.append(cal.get(Calendar.AM_PM) == Calendar.AM ? "am"
							: "pm");
			if (i == 0 || i == 4) {
				pmAmDateBuilder.append("-");
			} else if (i == 2) {
				pmAmDateBuilder.append(",");
			}
		}
		return pmAmDateBuilder.toString();

	}

	public static boolean isOn(String time) {
		int[] digits = parseTime(time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int currentHours = calendar.get(Calendar.HOUR_OF_DAY);
		int currentMinutes = calendar.get(Calendar.MINUTE);
		int currentTimeInMinutes = currentHours * 60 + currentMinutes;
		if ((digits[0] * 60 + digits[1] < currentTimeInMinutes && currentTimeInMinutes <= digits[2]
				* 60 + digits[3])
				|| (digits[4] * 60 + digits[5] < currentTimeInMinutes && currentTimeInMinutes <= digits[6]
						* 60 + digits[7])) {
			return false;
		}
		return true;
	}

	public static int[] getTotalShedding(String time) {
		if (time == null) {
			return null;
		}
		int[] digits = parseTime(time);
		int minutes = (digits[2] * 60 + digits[3])
				- (digits[0] * 60 + digits[1]) + (digits[6] * 60 + digits[7])
				- (digits[4] * 60 + digits[5]);
		return new int[] { (minutes / 60), (minutes - minutes / 60 * 60) };
	}

	private static int[] parseTime(String time) {
		String[] txtDigits = time.split(",|-|:");
		int[] digits = new int[txtDigits.length];
		for (int i = 0; i < digits.length; i++) {
			digits[i] = Integer.parseInt(txtDigits[i]);
		}
		return digits;
	}
}
