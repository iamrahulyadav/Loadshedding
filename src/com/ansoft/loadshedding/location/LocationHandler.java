package com.ansoft.loadshedding.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHandler implements LocationListener {
	private Context context;

	private Location lastKnownLocation = null;

	private LocationManager locationManager;

	public LocationHandler(Context context) {
		this.context = context;
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		lastKnownLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation == null) {
			lastKnownLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (lastKnownLocation == null) {
			lastKnownLocation = locationManager
					.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		}

	}

	public void startListening() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
	}

	public void stopListening() {
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			lastKnownLocation = location;
		}
	}

	public Location getLastKnownLocation() {
		return lastKnownLocation;
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
}
