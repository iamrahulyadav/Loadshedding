package com.ansoft.loadshedding;

import android.app.Application;
import android.content.res.AssetManager;

import com.ansoft.loadshedding.location.LocationHandler;
import com.ansoft.loadshedding.provider.loadsheddingdayinfo.LoadsheddingDayInfoCursor;
import com.ansoft.loadshedding.provider.loadsheddingdayinfo.LoadsheddingDayInfoSelection;
import com.ansoft.loadshedding.utils.NepaliDateConverter;
import com.ansoft.loadshedding.webservice.LoadsheddingInfo;
import com.ansoft.loadshedding.webservice.LoadsheddingWebService;
import com.ansoft.loadshedding.webservice.LocationInfo;
import com.ansoft.loadshedding.webservice.PhoneInfo;
import com.ansoft.loadshedding.webservice.SimpleXMLConverter;
import com.ansoft.loadshedding.webservice.UpdateIntentService;
import com.crittercism.app.Crittercism;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import retrofit.RestAdapter;

public class LoadsheddingApplication extends Application {

	public static LoadsheddingApplication app;

	private LocationInfo locationInfo;
	private PhoneInfo phoneInfo;
	private LoadsheddingWebService service;
	private Settings settings;
	private Map<String, String> time;
	private LocationHandler locationHandler;

	public LocationInfo getLocationInfo() {
		return locationInfo;
	}

	public PhoneInfo getPhoneInfo() {
		return phoneInfo;
	}

	public LoadsheddingWebService getService() {
		return service;
	}

	public Settings getSettings() {
		return settings;
	}

	public Map<String, String> getTime() {
		return time;
	}

	public LocationHandler getLocationHandler() {
		return locationHandler;
	}

	public static LoadsheddingApplication getInstance() {
		return app;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		Crittercism.initialize(getApplicationContext(),
				"53f4b9c183fb790a77000004");
		settings = new Settings(getBaseContext());
		locationHandler = new LocationHandler(getApplicationContext());
		Serializer serializer = new Persister();
		AssetManager am = getAssets();
		if (settings.getEffDate() == null) {
			LoadsheddingInfo info = null;
			try {
				InputStream is = am.open("nls_schedule.xml");
				info = serializer.read(LoadsheddingInfo.class, is);
			} catch (Exception e) {
				// ignore
			}
			UpdateIntentService
					.updateSchedule(getBaseContext(), settings, info);
		}

		try {
			InputStream is = am.open("locations.xml");
			locationInfo = serializer.read(LocationInfo.class, is);
		} catch (Exception e) {
			// ignore
		}

		try {
			InputStream is = am.open("phones_ktm.xml");
			phoneInfo = serializer.read(PhoneInfo.class, is);
		} catch (Exception e) {
			// ignore
			int a = 1;
		}

		time = new HashMap<String, String>();
		recacheTime();
		RestAdapter adapter = new RestAdapter.Builder()
				.setEndpoint("http://ansoft.co")
				// .setEndpoint("https://dl.dropboxusercontent.com")
				// .setEndpoint("http://emoontech.com")
				// .setLogLevel(RestAdapter.LogLevel.FULL)
				.setConverter(new SimpleXMLConverter()).build();
		service = adapter.create(LoadsheddingWebService.class);
		new Thread() {
			@Override
			public void run() {
				// cache date values
				Calendar calendar = Calendar.getInstance();
				calendar.setFirstDayOfWeek(Calendar.SUNDAY);
				calendar.setTime(new Date());
				for (int i = 0; i < 7; i++) {
					calendar.set(Calendar.DAY_OF_WEEK, i);
					NepaliDateConverter.convertCurrentDate(calendar);
				}
			}
		}.start();
		// String time = "5:30-10:30,13:00-18:00";
		// int[] left = DateUtils.getTimeToNextAction(time, 7, 13);
		// String text="";
	}

	public void recacheTime() {
		LoadsheddingDayInfoSelection selection = new LoadsheddingDayInfoSelection();
		LoadsheddingDayInfoCursor cursor = selection
				.query(getContentResolver());
		while (cursor.moveToNext()) {
			time.put(cursor.getGroupNumber() + cursor.getDay(),
					cursor.getTime());
		}
		cursor.close();
	}
}
