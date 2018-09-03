/*
 */
package com.ansoft.loadshedding.provider.loadsheddingdayinfo;

import android.net.Uri;
import android.provider.BaseColumns;

import com.ansoft.loadshedding.provider.LoadsheddingProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * Columns for the {@code loadshedding_day_info} table.
 */
public class LoadsheddingDayInfoColumns implements BaseColumns {
	public static final String TABLE_NAME = "loadshedding_day_info";
	public static final Uri CONTENT_URI = Uri
			.parse(LoadsheddingProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

	public static final String _ID = BaseColumns._ID;
	public static final String DEFAULT_ORDER = TABLE_NAME + "." + _ID;
	public static final String GROUP_NUMBER = "group_number";
	public static final String DAY = "day";
	public static final String TIME = "time";
	// @formatter:off
	public static final String[] FULL_PROJECTION = new String[] {
			TABLE_NAME + "." + _ID + " AS " + BaseColumns._ID,
			TABLE_NAME + "." + GROUP_NUMBER, TABLE_NAME + "." + DAY,
			TABLE_NAME + "." + TIME };
	// @formatter:on

	private static final Set<String> ALL_COLUMNS = new HashSet<String>();

	static {
		ALL_COLUMNS.add(_ID);
		ALL_COLUMNS.add(GROUP_NUMBER);
		ALL_COLUMNS.add(DAY);
		ALL_COLUMNS.add(TIME);
	}

	public static boolean hasColumns(String[] projection) {
		if (projection == null)
			return true;
		for (String c : projection) {
			if (ALL_COLUMNS.contains(c))
				return true;
		}
		return false;
	}
}
