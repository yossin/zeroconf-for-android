package com.HomeRemoteApp;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocationListener implements LocationListener
{
	HomeRemote homeRemote;
	
	public void onLocationChanged(Location loc)
	{
		homeRemote.GPSLocationChanged(loc);
	}
	
	public void setContext(HomeRemote andro2Acy_)
	{
		homeRemote = andro2Acy_;
	}
		
	public void onProviderDisabled(String provider)
	{
		Toast.makeText( homeRemote, "Gps Disabled", Toast.LENGTH_SHORT ).show();
	}

	public void onProviderEnabled(String provider)
	{
		Toast.makeText( homeRemote, "Gps Enabled", Toast.LENGTH_SHORT).show();
	}

	public void onStatusChanged(String provider, int status, Bundle extras)
	{
	}
	
	
}
