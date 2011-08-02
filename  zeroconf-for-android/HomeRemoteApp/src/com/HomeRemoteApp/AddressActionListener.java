package com.HomeRemoteApp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.MapView;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddressActionListener implements OnClickListener 
{
	HomeRemote homeRemote = null;
	
	public void onClick(View view)
	{
        Geocoder geocoder = new Geocoder(homeRemote,new Locale("en", "il"));
        
        List<Address> addresses = null;
        try 
        {
        	EditText et = (EditText)homeRemote.findViewById(R.id.setAddresTxt);
       		addresses = geocoder.getFromLocationName(et.getText().toString(), 10);
       		if (addresses.size() > 0)
       			homeRemote.updateMap(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}    
	}
	
	public void SetContext(HomeRemote andro2Acy_)
	{
		homeRemote = andro2Acy_;	
	}

}
