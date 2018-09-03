/*
 */
package com.ansoft.loadshedding.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ansoft.loadshedding.BuildConfig;

/**
 * Implement your custom database creation or upgrade code here.
 * <p/>
 * This file will not be overwritten if you re-run the content provider
 * generator.
 */
public class LoadsheddingSQLiteOpenHelperCallbacks {
	private static final String TAG = LoadsheddingSQLiteOpenHelperCallbacks.class
			.getSimpleName();

	public void onOpen(final Context context, final SQLiteDatabase db) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "onOpen");
		// Insert your db open code here.
	}

	public void onPreCreate(final Context context, final SQLiteDatabase db) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "onPreCreate");
		// Insert your db creation code here. This is called before your tables
		// are created.
	}

	public void onPostCreate(final Context context, final SQLiteDatabase db) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "onPostCreate");
		// Insert your db creation code here. This is called after your tables
		// are created.
	}

	public void onUpgrade(final Context context, final SQLiteDatabase db,
			final int oldVersion, final int newVersion) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion);
		}
		// Insert your upgrading code here.
		new LoadsheddingSQLiteUpgradeHelper().onUpgrade(db, oldVersion,
				newVersion);
	}

	private class LoadsheddingSQLiteUpgradeHelper {
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			throw new UnsupportedOperationException("Not implemented");
		}
	}
}
