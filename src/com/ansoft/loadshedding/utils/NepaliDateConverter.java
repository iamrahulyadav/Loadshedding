package com.ansoft.loadshedding.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class NepaliDateConverter {

	// The 0s at index 0 are dummy values so as to make the int array of
	// days in months seems more intuitive that index 1 refers to first
	// month "Baisakh", index 2 refers to second month "Jesth" and so on.

	private static Map<Integer, int[]> nepaliMap = new HashMap<Integer, int[]>();

	private static Map<String, int[]> datesCache = new HashMap<String, int[]>();

	static {
		nepaliMap.put(2071, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30 });
		nepaliMap.put(2072, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 29, 30,
				29, 30, 30 });
		nepaliMap.put(2073, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 31 });
		nepaliMap.put(2074, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
				29, 30, 30 });
		nepaliMap.put(2075, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30 });
		nepaliMap.put(2076, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 30 });
		nepaliMap.put(2077, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 29, 31 });
		nepaliMap.put(2078, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
				29, 30, 30 });
		nepaliMap.put(2079, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
				29, 30, 30 });
		nepaliMap.put(2080, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
				29, 30, 30 });
		nepaliMap.put(2081, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 30, 29,
				30, 30, 30 });
		nepaliMap.put(2082, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 30, 30 });
		nepaliMap.put(2083, new int[] { 0, 31, 31, 32, 31, 31, 30, 30, 30, 29,
				30, 30, 30 });
		nepaliMap.put(2084, new int[] { 0, 31, 31, 32, 31, 31, 30, 30, 30, 29,
				30, 30, 30 });
		nepaliMap.put(2085, new int[] { 0, 31, 32, 31, 32, 30, 31, 30, 30, 29,
				30, 30, 30 });
		nepaliMap.put(2086, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 30, 30 });
		nepaliMap.put(2087, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 30, 29,
				30, 30, 30 });
		nepaliMap.put(2088, new int[] { 0, 30, 31, 32, 32, 30, 31, 30, 30, 29,
				30, 30, 30 });
		nepaliMap.put(2089, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 30, 30 });
		nepaliMap.put(2090, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
				30, 30, 30 });
	}

	public static int[] convertCurrentDate(Calendar calendar) {
		int engDay = calendar.get(Calendar.DAY_OF_MONTH);
		int engMonth = calendar.get(Calendar.MONTH);
		int engYear = calendar.get(Calendar.YEAR);
		return convert(engDay, engMonth, engYear);
	}

	public static int[] convert(int engDay, int engMonth, int engYear) {
		if (datesCache.get("" + engDay + engMonth + engYear) != null) {
			return datesCache.get("" + engDay + engMonth + engYear);
		}

		int startingEngYear = 2014;

		int startingEngMonth = Calendar.SEPTEMBER;

		int startingEngDay = 26;

		int dayOfWeek = Calendar.FRIDAY;

		int startingNepYear = 2071;

		int startingNepMonth = 6;

		int startingNepDay = 10;

		Calendar currentEngDate = new GregorianCalendar();

		currentEngDate.set(engYear, engMonth, engDay);

		Calendar baseEngDate = new GregorianCalendar();

		baseEngDate.set(startingEngYear, startingEngMonth, startingEngDay);

		long totalEngDaysCount = daysBetween(baseEngDate, currentEngDate);

		// initialize required Nepali date variables with starting Nepali date

		int nepYear = startingNepYear;
		int nepMonth = startingNepMonth;
		int nepDay = startingNepDay;

		// decrement totalEngDaysCount until its value becomes 0
		while (totalEngDaysCount != 0) {

			// getting the total number of days in month nepMonth in year
			// nepYear
			int daysInIthMonth = nepaliMap.get(nepYear)[nepMonth];

			nepDay++; // incrementing nepali day

			if (nepDay > daysInIthMonth) {
				nepMonth++;
				nepDay = 1;
			}
			if (nepMonth > 12) {
				nepYear++;
				nepMonth = 1;
			}

			dayOfWeek++; // count the days in terms of 7 days
			if (dayOfWeek > 7) {
				dayOfWeek = 1;
			}

			totalEngDaysCount--;
		}
		datesCache.put("" + engDay + engMonth + engYear, new int[] { nepDay,
				nepMonth, nepYear });
		return new int[] { nepDay, nepMonth, nepYear };
	}

	public static long daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}
}
