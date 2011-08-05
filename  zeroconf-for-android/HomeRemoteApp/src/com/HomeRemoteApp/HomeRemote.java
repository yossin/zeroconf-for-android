
package com.HomeRemoteApp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class HomeRemote extends MapActivity 
{
	enum ViewMode
	{
		VIEW_BY_GPS,
		VIEW_BY_MAP,
		VIEW_MANUAL
	}
	
	
	private MapView 			m_mapView;
	private MyLocationListener 	m_mlocListener;
	private LocationManager 	m_mlocManager;
	private long 				m_lastTouchTime = -1;
	private ViewMode 			m_viewMode = ViewMode.VIEW_BY_MAP;
	private ServiceConnector 	m_serviceConnector;
	private ContentWapper 	    m_contentWapper;
	private ViewManualAdapter 	m_ViewManualAdapter;
	private String 				m_sHostIP = "192.168.2.106";

	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        m_mapView = (MapView)findViewById(R.id.mapview);

        //mapView.setSatellite(true);
        m_mapView.setStreetView(true);
        
		m_serviceConnector = new ServiceConnector();
 	    m_contentWapper = new ContentWapper(m_serviceConnector);
 	    m_serviceConnector.SetHostIP(m_sHostIP);
   	
        /* Get GPS updates 
    	 * in emulator - use 
    	 * c:\telnet localhost 5554
    	 * geo fix 34.929402 32.580964
    	 *  */
		m_mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		m_mlocListener = new MyLocationListener();
		m_mlocListener.setContext(this);
		m_mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, m_mlocListener);
        
        /* update listener for location by address button */
        Button setAddresBtn = (Button)findViewById(R.id.setAddresBtn);
        AddressActionListener addressActionListener = new AddressActionListener();
        addressActionListener.SetContext(this);
        OnClickListener onClickListener = (View.OnClickListener)addressActionListener;
        setAddresBtn.setOnClickListener(onClickListener);

        /* update listener for set GPS coords according to map button */
        Button setGPSByMapBtn = (Button)findViewById(R.id.setGPSByMapBtn);
        setGPSByMapBtn.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
        		SendGPSPosByMapPos();	}
        	});
                
        /* update listener for GeoPoint button */
        Button showGeoPointBtn = (Button)findViewById(R.id.showGeoPoint);
        showGeoPointBtn.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
        		ShowGeoPoint(m_mapView.getMapCenter());	}
        	});
         
        m_mapView.setBuiltInZoomControls(true);
        
        
        /* tree view */
        ExpandableListView epView = (ExpandableListView) findViewById(R.id.ViewManual_ExpandableList);
        m_ViewManualAdapter = new ViewManualAdapter();
        m_ViewManualAdapter.setContext(this);
        epView.setAdapter(m_ViewManualAdapter);

        Button ManualUpdateBtn = (Button)findViewById(R.id.setManualUpdate);
        ManualUpdateBtn.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
            	m_contentWapper.SendManualUpdate(m_ViewManualAdapter.getGroups());
                m_ViewManualAdapter.notifyDataSetChanged();
        	}
        });

        Button RefrashHomeBtn = (Button)findViewById(R.id.refrashHome);
        RefrashHomeBtn.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
                m_ViewManualAdapter.setContent(m_contentWapper.GetGroupsContent());
                m_ViewManualAdapter.notifyDataSetChanged();
        	}
        });

        
        m_ViewManualAdapter.setContent(m_contentWapper.GetGroupsContent());
		UpdateControlsVisibility(m_viewMode);
    }
    
    public void updateMap(double la, double lo)
    {
    	GeoPoint geoPoint = new GeoPoint((int)(la*1000000),(int)(lo*1000000));
   		MapController mapController = m_mapView.getController();
   		mapController.setZoom(11);
   		mapController.animateTo(geoPoint);
	}
    
	public void ShowGeoPoint(GeoPoint geoPoint)
	{
		double la = ((double)geoPoint.getLatitudeE6()) / 1000000;
		double lo = ((double)geoPoint.getLongitudeE6()) / 1000000;
		String Text = "My current location is:\n" +
				"Latitud = " + la + 
				"\nLongitud = " + lo ;
		Toast.makeText( this, Text,  Toast.LENGTH_SHORT).show();
	}
    
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
    	if (ev.getAction() == MotionEvent.ACTION_DOWN) 
    	{
    		long thisTime = System.currentTimeMillis();
    		if (thisTime - m_lastTouchTime < 250) 
    		{
    	        // Double tap
    	   		MapController mapController = m_mapView.getController();
    	   		mapController.zoomInFixing((int) ev.getX(), (int) ev.getY());
    	        //mapController.zoomIn();
    	   		m_lastTouchTime = -1;
    		}
    		else	
    		{
    			// Too slow
    			m_lastTouchTime = thisTime;
    		}
    	}
    	return super.dispatchTouchEvent(ev);
    } 

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	
	/* menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	public void UpdateHostIP(String sHostIP)
	{
		m_sHostIP = sHostIP;
		m_serviceConnector.SetHostIP(m_sHostIP);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.viewByGPS:     
	            m_mapView.setBuiltInZoomControls(true);
	        	UpdateView(ViewMode.VIEW_BY_GPS);
	        	GoToGPSPosition();
	        	break;
	        case R.id.viewByMap:     
	            m_mapView.setBuiltInZoomControls(true);
	        	UpdateView(ViewMode.VIEW_BY_MAP);
	            break;
	        case R.id.viewManual: 
	            m_mapView.setBuiltInZoomControls(false);
	        	UpdateView(ViewMode.VIEW_MANUAL);
	            break;
	        case R.id.setHostIP:
	        	AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
	            alertBuilder.setTitle("Set Host IP");
	            final EditText inputIP = new EditText(this);
	            inputIP.setText(m_sHostIP.toString());
	            alertBuilder.setView(inputIP);
	            alertBuilder.setMessage("Default body");
	            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                        dialog.dismiss();
	                    }
	                });

	            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	UpdateHostIP(inputIP.getText().toString());
	                    	dialog.dismiss();	                    	
	                    }
	                });
		        	alertBuilder.create();

		        alertBuilder.show();
		        	
	            break;
	    }
	    return true;
	}
	
	public void UpdateView(ViewMode viewMode)
	{
		m_viewMode = viewMode;		
		UpdateControlsVisibility(m_viewMode);
	}
	
	
	
	public void UpdateControlsVisibility(ViewMode viewMode)
	{
		Button AddresBtn = (Button)findViewById(R.id.setAddresBtn);
        Button GeoPointBtn = (Button)findViewById(R.id.showGeoPoint);
        Button SetGPSByMapBtn = (Button)findViewById(R.id.setGPSByMapBtn);
    	EditText AddresTxt = (EditText)findViewById(R.id.setAddresTxt);
    	ExpandableListView epView = (ExpandableListView) findViewById(R.id.ViewManual_ExpandableList);
        Button ManualUpdateBtn = (Button)findViewById(R.id.setManualUpdate);
        Button RefrashBtn = (Button)findViewById(R.id.refrashHome);
        
		AddresBtn.setVisibility(viewMode.equals(viewMode.VIEW_BY_MAP) ? View.VISIBLE : View.GONE);
		SetGPSByMapBtn.setVisibility(viewMode.equals(viewMode.VIEW_BY_MAP) ? View.VISIBLE : View.GONE);
		AddresTxt.setVisibility((viewMode.equals(viewMode.VIEW_BY_MAP) ) ? View.VISIBLE : View.GONE);
		GeoPointBtn.setVisibility((viewMode.equals(viewMode.VIEW_BY_MAP)) ? View.VISIBLE : View.GONE);
		m_mapView.setVisibility(viewMode.equals(viewMode.VIEW_BY_MAP) || viewMode.equals(viewMode.VIEW_BY_GPS) ? View.VISIBLE : View.GONE);
		epView.setVisibility((viewMode.equals(viewMode.VIEW_MANUAL) ) ? View.VISIBLE : View.GONE);
		ManualUpdateBtn.setVisibility((viewMode.equals(viewMode.VIEW_MANUAL) ) ? View.VISIBLE : View.GONE);
		RefrashBtn.setVisibility((viewMode.equals(viewMode.VIEW_MANUAL) ) ? View.VISIBLE : View.GONE);
	}
	
	public void GPSLocationChanged(Location loc)
	{
		if (m_viewMode == ViewMode.VIEW_BY_GPS)
		{
			long la = (long) (loc.getLatitude()  * 1000000);
			long lo = (long) (loc.getLongitude() * 1000000);
			m_contentWapper.SendGPSLocation(la, lo);
			updateMap(loc.getLatitude(), loc.getLongitude());
//			ShowGeoPoint(new GeoPoint((int)la, (int)lo));
		}
	}
	
	private void GoToGPSPosition()
	{
		Location ll = m_mlocManager.getLastKnownLocation("gps");
		if (ll != null)
			GPSLocationChanged(ll);
	}
	
	static GeoPoint CurGeoPoint = null;
	private void SendGPSPosByMapPos()
	{
		GeoPoint geoPoint = m_mapView.getMapCenter();
		m_contentWapper.SendGPSLocation(geoPoint.getLatitudeE6(), geoPoint.getLongitudeE6());		
		
		/* test */
		if (false && CurGeoPoint != null)
		{
			Long dis = new Long(GetDistance(CurGeoPoint, geoPoint));
//			boolean bOutOfRange = IsOutOfRange(CurGeoPoint, geoPoint, 1500);
			String Text = "Location changed,\nDistance = " + (dis.intValue() / 1000) + "," + (dis.intValue() % 1000); // +"\nOutOfRange ? " + bOutOfRange;
			Toast.makeText( this, Text,  Toast.LENGTH_SHORT).show();
		}
		CurGeoPoint = geoPoint;
		/* end test */
	}

	private boolean IsOutOfRange(GeoPoint g1, GeoPoint g2, int meters)
	{
		return GetDistance(g1, g2) > meters;
	}
	
	 public long GetDistance(GeoPoint g1, GeoPoint g2) 
	 {
		 float lat1 = (float)g1.getLatitudeE6() / 1000000;
		 float lng1 = (float)g1.getLongitudeE6() / 1000000;
		 float lat2 = (float)g2.getLatitudeE6() / 1000000;
		 float lng2 = (float)g2.getLongitudeE6() / 1000000;
		 double earthRadius = 3958.75;
		 double dLat = Math.toRadians(lat2-lat1);
		 double dLng = Math.toRadians(lng2-lng1);
		 double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		            Math.sin(dLng/2) * Math.sin(dLng/2);
		 double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		 double dist = earthRadius * c;
		
		 int meterConversion = 1609;
		
		return (long) (dist * meterConversion);
	}
	
}