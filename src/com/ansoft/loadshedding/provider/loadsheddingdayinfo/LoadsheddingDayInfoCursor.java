/*
 */
package com.ansoft.loadshedding.provider.loadsheddingdayinfo;

import android.database.Cursor;

import com.ansoft.loadshedding.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code loadshedding_day_info} table.
 */
public class LoadsheddingDayInfoCursor extends AbstractCursor {
	public LoadsheddingDayInfoCursor(Cursor cursor) {
		super(cursor);
	}

	/**
	 * Get the {@code group_number} value. Can be {@code null}.
	 */
	public Integer getGroupNumber() {
		return getIntegerOrNull(LoadsheddingDayInfoColumns.GROUP_NUMBER);
	}

	/**
	 * Get the {@code day} value. Can be {@code null}.
	 */
	public String getDay() {
		Integer index = getCachedColumnIndexOrThrow(LoadsheddingDayInfoColumns.DAY);
		return getString(index);
	}

	/**
	 * Get the {@code time} value. Can be {@code null}.
	 */
	public String getTime() {
		Integer index = getCachedColumnIndexOrThrow(LoadsheddingDayInfoColumns.TIME);
		return getString(index);
	}
}
