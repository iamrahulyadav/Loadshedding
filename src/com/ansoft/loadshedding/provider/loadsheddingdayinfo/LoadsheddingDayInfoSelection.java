/*
 */
package com.ansoft.loadshedding.provider.loadsheddingdayinfo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.ansoft.loadshedding.provider.base.AbstractSelection;

/**
 * Selection for the {@code loadshedding_day_info} table.
 */
public class LoadsheddingDayInfoSelection extends
		AbstractSelection<LoadsheddingDayInfoSelection> {
	@Override
	public Uri uri() {
		return LoadsheddingDayInfoColumns.CONTENT_URI;
	}

	/**
	 * Query the given content resolver using this selection.
	 * 
	 * @param contentResolver
	 *            The content resolver to query.
	 * @param projection
	 *            A list of which columns to return. Passing null will return
	 *            all columns, which is inefficient.
	 * @param sortOrder
	 *            How to order the rows, formatted as an SQL ORDER BY clause
	 *            (excluding the ORDER BY itself). Passing null will use the
	 *            default sort order, which may be unordered.
	 * @return A {@code LoadsheddingDayInfoCursor} object, which is positioned
	 *         before the first entry, or null.
	 */
	public LoadsheddingDayInfoCursor query(ContentResolver contentResolver,
			String[] projection, String sortOrder) {
		Cursor cursor = contentResolver.query(uri(), projection, sel(), args(),
				sortOrder);
		if (cursor == null)
			return null;
		return new LoadsheddingDayInfoCursor(cursor);
	}

	/**
	 * Equivalent of calling {@code query(contentResolver, projection, null}.
	 */
	public LoadsheddingDayInfoCursor query(ContentResolver contentResolver,
			String[] projection) {
		return query(contentResolver, projection, null);
	}

	/**
	 * Equivalent of calling
	 * {@code query(contentResolver, projection, null, null}.
	 */
	public LoadsheddingDayInfoCursor query(ContentResolver contentResolver) {
		return query(contentResolver, null, null);
	}

	public LoadsheddingDayInfoSelection id(long... value) {
		addEquals(LoadsheddingDayInfoColumns._ID, toObjectArray(value));
		return this;
	}

	public LoadsheddingDayInfoSelection groupNumber(Integer... value) {
		addEquals(LoadsheddingDayInfoColumns.GROUP_NUMBER, value);
		return this;
	}

	public LoadsheddingDayInfoSelection groupNumberNot(Integer... value) {
		addNotEquals(LoadsheddingDayInfoColumns.GROUP_NUMBER, value);
		return this;
	}

	public LoadsheddingDayInfoSelection groupNumberGt(int value) {
		addGreaterThan(LoadsheddingDayInfoColumns.GROUP_NUMBER, value);
		return this;
	}

	public LoadsheddingDayInfoSelection groupNumberGtEq(int value) {
		addGreaterThanOrEquals(LoadsheddingDayInfoColumns.GROUP_NUMBER, value);
		return this;
	}

	public LoadsheddingDayInfoSelection groupNumberLt(int value) {
		addLessThan(LoadsheddingDayInfoColumns.GROUP_NUMBER, value);
		return this;
	}

	public LoadsheddingDayInfoSelection groupNumberLtEq(int value) {
		addLessThanOrEquals(LoadsheddingDayInfoColumns.GROUP_NUMBER, value);
		return this;
	}

	public LoadsheddingDayInfoSelection day(String... value) {
		addEquals(LoadsheddingDayInfoColumns.DAY, value);
		return this;
	}

	public LoadsheddingDayInfoSelection dayNot(String... value) {
		addNotEquals(LoadsheddingDayInfoColumns.DAY, value);
		return this;
	}

	public LoadsheddingDayInfoSelection dayLike(String... value) {
		addLike(LoadsheddingDayInfoColumns.DAY, value);
		return this;
	}

	public LoadsheddingDayInfoSelection time(String... value) {
		addEquals(LoadsheddingDayInfoColumns.TIME, value);
		return this;
	}

	public LoadsheddingDayInfoSelection timeNot(String... value) {
		addNotEquals(LoadsheddingDayInfoColumns.TIME, value);
		return this;
	}

	public LoadsheddingDayInfoSelection timeLike(String... value) {
		addLike(LoadsheddingDayInfoColumns.TIME, value);
		return this;
	}
}
