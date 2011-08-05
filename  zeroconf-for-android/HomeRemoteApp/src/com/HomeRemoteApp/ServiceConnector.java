

package com.HomeRemoteApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceConnector
{
	static final int UpdateSubject_ZoneUpdate 		= 0;
	static final int UpdateSubject_LocationUpdate 	= 1;
	
	int iMockTimer;
	String m_sHostIP = "";
	
	public ServiceConnector()
	{
		iMockTimer = 0;
	}
	
	public void SetHostIP(String sHostIP)
	{
		m_sHostIP = sHostIP;
	}
	
	String GetURLPrefix()
	{
		StringBuilder sbURL = new StringBuilder();
		sbURL.append("http://"); 
		sbURL.append(m_sHostIP);
		sbURL.append(":8080/jmdns-ws/");
		return sbURL.toString();
	}
	
	public void sendMessage(JSONArray arrayObj, int UpdateSubject)
	{
		try{
		String message= URLEncoder.encode(arrayObj.toString(), "UTF-8");
		StringBuilder sbURL = new StringBuilder();
		sbURL.append(GetURLPrefix()); 
		switch (UpdateSubject)
		{
		case UpdateSubject_ZoneUpdate:
			sbURL.append("ZoneUpdate");
			break;
		case UpdateSubject_LocationUpdate:
			sbURL.append("LocationUpdate");
			break;
		}
		URL groupUpdateUrl = new URL(sbURL.toString());
		
		URLConnection connection = groupUpdateUrl.openConnection();
		connection.setDoOutput(true);
		connection.setConnectTimeout(1500);
		connection.setReadTimeout(5000);

		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		out.write("data=" + message);
		out.flush();
		out.close();

		BufferedReader in = new BufferedReader(
				new InputStreamReader(
				connection.getInputStream()));
				
		in.close();
		

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
		
	public JSONArray GetGroupsContent()
	{
		try {
			URL groupsUrl;
			StringBuilder sbURL = new StringBuilder();
			sbURL.append(GetURLPrefix());
			sbURL.append("ZoneList");
			groupsUrl = new URL(sbURL.toString());
	        URLConnection yc = groupsUrl.openConnection();
	        yc.setConnectTimeout(1500);
	        yc.setReadTimeout(5000);
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                yc.getInputStream()));
	        String inputLine;
	        StringBuilder builder = new StringBuilder();
	        while ((inputLine = in.readLine()) != null) 
	        	builder.append(inputLine);
	        in.close();
	        	
	        System.out.println(builder);

	        return new JSONArray(builder.toString());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		
		return GetGroupsContentMock();
	}

	public JSONArray GetGroupsContentMock()
	{
		iMockTimer ++;
		iMockTimer %=3;
		
		JSONArray  arr_main = new JSONArray();
		
		JSONObject obj_G1 = new JSONObject();
		JSONArray  arr_G1 = new JSONArray();
		
		JSONObject obj_G2 = new JSONObject();
		JSONArray  arr_G2 = new JSONArray();

		JSONObject obj_G3 = new JSONObject();
		JSONArray  arr_G3 = new JSONArray();

		JSONObject obj_G1_D1 = new JSONObject();
		JSONObject obj_G1_D2 = new JSONObject();
		JSONObject obj_G2_D1 = new JSONObject();
		JSONObject obj_G2_D2 = new JSONObject();

		JSONObject obj_Gt_Dt1 = new JSONObject();
		JSONObject obj_Gt_Dt2 = new JSONObject();

		
		try {
			obj_G1_D1.put("id", 2);
			obj_G1_D1.put("name", "d_1_1");
			obj_G1_D1.put("state", 1);
	
			obj_G1_D2.put("id", 3);
			obj_G1_D2.put("name", "d_1_2");
			obj_G1_D2.put("state", 0);

			
			obj_G2_D1.put("id", 5);
			obj_G2_D1.put("name", "d_2_1");
			obj_G2_D1.put("state", 1);

			obj_G2_D2.put("id", 7);
			obj_G2_D2.put("name", "d_2_2");
			obj_G2_D2.put("state", 2);

			obj_Gt_Dt1.put("id", 8);
			obj_Gt_Dt1.put("name", "d_2_2");
			obj_Gt_Dt1.put("state", 1);

			obj_Gt_Dt2.put("id", 9);
			obj_Gt_Dt2.put("name", "Gt_Dt2");
			obj_Gt_Dt2.put("state", 0);
				
			obj_G1.put("id", 5);
			obj_G1.put("name", "g_1");
			obj_G1.put("state", 0);
			arr_G1.put(obj_G1_D1);
			arr_G1.put(obj_G1_D2);
			obj_G1.put("devices", arr_G1);
			
			obj_G2.put("id", 8);
			obj_G2.put("name", "g_2");
			obj_G2.put("state", 1);
			arr_G2.put(obj_G2_D1);
			arr_G2.put(obj_G2_D2);
			if (iMockTimer==2)
				arr_G2.put(obj_Gt_Dt1);
			obj_G2.put("devices", arr_G2);
		
			
			arr_main.put(obj_G1);
			arr_main.put(obj_G2);
			if (iMockTimer==1)
			{
				obj_G3.put("id", 78);
				obj_G3.put("name", "g_3");
				obj_G3.put("state", 0);
				arr_G3.put(obj_Gt_Dt1);
				arr_G3.put(obj_Gt_Dt2);
				obj_G3.put("devices", arr_G3);

				arr_main.put(obj_G3);
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return arr_main;
	}
	
}
