/*
 */
package com.ansoft.loadshedding.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.ansoft.loadshedding.BuildConfig;
import com.ansoft.loadshedding.provider.loadsheddingdayinfo.LoadsheddingDayInfoColumns;

public class LoadsheddingSQLiteOpenHelper extends SQLiteOpenHelper {
	public static final String DATABASE_FILE_NAME = "loadshedding.db";
	private static final String TAG = LoadsheddingSQLiteOpenHelper.class
			.getSimpleName();
	private static final int DATABASE_VERSION = 2;
	// @formatter:off
	private static final String SQL_CREATE_TABLE_LOADSHEDDING_DAY_INFO = "CREATE TABLE IF NOT EXISTS "
			+ LoadsheddingDayInfoColumns.TABLE_NAME
			+ " ( "
			+ LoadsheddingDayInfoColumns._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ LoadsheddingDayInfoColumns.GROUP_NUMBER
			+ " INTEGER, "
			+ LoadsheddingDayInfoColumns.DAY
			+ " TEXT, "
			+ LoadsheddingDayInfoColumns.TIME + " TEXT " + " );";
	private static final String SQL_CREATE_INDEX_LOADSHEDDING_DAY_INFO_GROUP_NUMBER = "CREATE INDEX IDX_LOADSHEDDING_DAY_INFO_GROUP_NUMBER "
			+ " ON "
			+ LoadsheddingDayInfoColumns.TABLE_NAME
			+ " ( "
			+ LoadsheddingDayInfoColumns.GROUP_NUMBER + " );";
	private static final String SQL_CREATE_INDEX_LOADSHEDDING_DAY_INFO_DAY = "CREATE INDEX IDX_LOADSHEDDING_DAY_INFO_DAY "
			+ " ON "
			+ LoadsheddingDayInfoColumns.TABLE_NAME
			+ " ( "
			+ LoadsheddingDayInfoColumns.DAY + " );";
	private static final String SQL_CREATE_INDEX_LOADSHEDDING_DAY_INFO_TIME = "CREATE INDEX IDX_LOADSHEDDING_DAY_INFO_TIME "
			+ " ON "
			+ LoadsheddingDayInfoColumns.TABLE_NAME
			+ " ( "
			+ LoadsheddingDayInfoColumns.TIME + " );";
	private static LoadsheddingSQLiteOpenHelper sInstance;
	private final Context mContext;
	private final LoadsheddingSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

	// @formatter:on

	private LoadsheddingSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
		mOpenHelperCallbacks = new LoadsheddingSQLiteOpenHelperCallbacks();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private LoadsheddingSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		mContext = context;
		mOpenHelperCallbacks = new LoadsheddingSQLiteOpenHelperCallbacks();
	}

	/*
	 * Pre Honeycomb.
	 */

	public static LoadsheddingSQLiteOpenHelper getInstance(Context context) {
		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null) {
			sInstance = newInstance(context.getApplicationContext());
		}
		return sInstance;
	}

	private static LoadsheddingSQLiteOpenHelper newInstance(Context context) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return newInstancePreHoneycomb(context);
		}
		return newInstancePostHoneycomb(context);
	}

	/*
	 * Post Honeycomb.
	 */

	private static LoadsheddingSQLiteOpenHelper newInstancePreHoneycomb(
			Context context) {
		return new LoadsheddingSQLiteOpenHelper(context, DATABASE_FILE_NAME,
				null, DATABASE_VERSION);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static LoadsheddingSQLiteOpenHelper newInstancePostHoneycomb(
			Context context) {
		return new LoadsheddingSQLiteOpenHelper(context, DATABASE_FILE_NAME,
				null, DATABASE_VERSION, new DefaultDatabaseErrorHandler());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "onCreate");
		mOpenHelperCallbacks.onPreCreate(mContext, db);
		db.execSQL(SQL_CREATE_TABLE_LOADSHEDDING_DAY_INFO);
		db.execSQL(SQL_CREATE_INDEX_LOADSHEDDING_DAY_INFO_GROUP_NUMBER);
		db.execSQL(SQL_CREATE_INDEX_LOADSHEDDING_DAY_INFO_DAY);
		db.execSQL(SQL_CREATE_INDEX_LOADSHEDDING_DAY_INFO_TIME);
		mOpenHelperCallbacks.onPostCreate(mContext, db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
	}
}
