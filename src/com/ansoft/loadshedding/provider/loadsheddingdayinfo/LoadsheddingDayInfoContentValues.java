/*
 */
package com.ansoft.loadshedding.provider.loadsheddingdayinfo;

import android.content.ContentResolver;
import android.net.Uri;

import com.ansoft.loadshedding.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code loadshedding_day_info} table.
 */
public class LoadsheddingDayInfoContentValues extends AbstractContentValues {
	@Override
	public Uri uri() {
		return LoadsheddingDayInfoColumns.CONTENT_URI;
	}

	/**
	 * Update row(s) using the values stored by this object and the given
	 * selection.
	 * 
	 * @param contentResolver
	 *            The content resolver to use.
	 * @param where
	 *            The selection to use (can be {@code null}).
	 */
	public int update(ContentResolver contentResolver,
			LoadsheddingDayInfoSelection where) {
		return contentResolver.update(uri(), values(), where == null ? null
				: where.sel(), where == null ? null : where.args());
	}

	public LoadsheddingDayInfoContentValues putGroupNumber(Integer value) {
		mContentValues.put(LoadsheddingDayInfoColumns.GROUP_NUMBER, value);
		return this;
	}

	public LoadsheddingDayInfoContentValues putGroupNumberNull() {
		mContentValues.putNull(LoadsheddingDayInfoColumns.GROUP_NUMBER);
		return this;
	}

	public LoadsheddingDayInfoContentValues putDay(String value) {
		mContentValues.put(LoadsheddingDayInfoColumns.DAY, value);
		return this;
	}

	public LoadsheddingDayInfoContentValues putDayNull() {
		mContentValues.putNull(LoadsheddingDayInfoColumns.DAY);
		return this;
	}

	public LoadsheddingDayInfoContentValues putTime(String value) {
		mContentValues.put(LoadsheddingDayInfoColumns.TIME, value);
		return this;
	}

	public LoadsheddingDayInfoContentValues putTimeNull() {
		mContentValues.putNull(LoadsheddingDayInfoColumns.TIME);
		return this;
	}

}
