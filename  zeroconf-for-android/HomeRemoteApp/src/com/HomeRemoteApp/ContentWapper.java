package com.HomeRemoteApp;

import java.util.ArrayList;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.CheckBox;

public class ContentWapper 
{
	private ServiceConnector m_serviceConnector = null;
	
	public ContentWapper(ServiceConnector serviceConnector)
	{
		m_serviceConnector = serviceConnector;
	}

	public Vector<GroupItem> GetGroupsContent()
	{
		return ParseContent(m_serviceConnector.GetGroupsContent());
	}

	private Vector<GroupItem> ParseContent(JSONArray arrayObj)
	{
		Vector<GroupItem> GroupItems = new Vector<GroupItem>();
	    for (int ii = 0; ii < arrayObj.length(); ii++) 
	    {
	    	JSONObject obj_g = null;
	    	JSONArray  arr_d = null;
	        try 
	        {
	        	obj_g = arrayObj.getJSONObject(ii);
	        	
	        	GroupItem gi = GetGroupItemFromJSONObject(obj_g);
	        	
	        	arr_d = obj_g.getJSONArray("devices");
	        	for (int jj = 0; jj < arr_d.length(); jj++) 
			    	gi.add(GetDeviceItemFromJSONObject(arr_d.getJSONObject(jj)));
			    
	        	GroupItems.add(gi);
			} 
	        catch (JSONException e) 
			{
				e.printStackTrace();
			}
	    }
	    return GroupItems;
	}
		
	public void SendManualUpdate(Vector<GroupItem> GroupItems)
	{
		JSONArray arrayObj = new JSONArray();
		for (int gii = 0 ; gii < GroupItems.size() ; gii++ ) 
		{
			arrayObj.put(BouildGroupStatusMsg(GroupItems.get(gii)));
		}
		m_serviceConnector.sendMessage(arrayObj, ServiceConnector.UpdateSubject_ZoneUpdate);
	}

	private JSONObject BouildGroupStatusMsg(GroupItem groupItem)
	{
		JSONObject JSONObj = new JSONObject();
		try 
		{
			JSONObj.put("name", groupItem.getName());
//			JSONObj.put("state", groupItem.getStatus());
			
			/* put all devices */
			JSONArray arrayObjG = new JSONArray();
			for (int dii = 0 ; dii < groupItem.Size() ; dii++ ) 
			{
				arrayObjG.put(BouildDeviceStatusMsg(groupItem.get(dii)));
			}
			JSONObj.put("devices", arrayObjG);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return JSONObj;
	}
	
	private GroupItem GetGroupItemFromJSONObject(JSONObject JSONObj)
	{
		try 
		{
			return new GroupItem(JSONObj.getString("name"), JSONObj.getInt("state"));
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private JSONObject BouildDeviceStatusMsg(DeviceItem deviceItem)
	{
		JSONObject JSONObj = new JSONObject();
		try 
		{
			JSONObj.put("id", 	deviceItem.getID());
			JSONObj.put("name", deviceItem.getName());
			JSONObj.put("state", deviceItem.getStatus());
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return JSONObj;
	}
	
	private DeviceItem GetDeviceItemFromJSONObject(JSONObject JSONObj)
	{
		try 
		{
			return new DeviceItem(JSONObj.getString("id"), JSONObj.getString("name"), JSONObj.getInt("state"));
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void SendGPSLocation(long la, long lo)
	{
		JSONArray arrayObj = new JSONArray();
		JSONObject JSONObj = new JSONObject();
		try 
		{
			JSONObj.put("latitude",  la);
			JSONObj.put("longitude", lo);
			arrayObj.put(JSONObj);
			m_serviceConnector.sendMessage(arrayObj, ServiceConnector.UpdateSubject_LocationUpdate);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}
}
