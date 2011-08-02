package com.HomeRemoteApp;

import java.util.Vector;

public class GroupItem 
{
	static final int GroupStat_OFF 		= 0;
	static final int GroupStat_ON 		= 1;

	private Vector<DeviceItem> 	m_deviceItems = new Vector<DeviceItem>();
	private String 				m_sName = "";
	private int 				m_nStat = GroupStat_OFF;

	public GroupItem(String sName, int nStat)
	{
		m_sName 	= sName;
		m_nStat 	= nStat;
	}
	
	public void add(DeviceItem deviceItem)
	{
		m_deviceItems.add(deviceItem);
	}
	
	public int Size()
	{
		return m_deviceItems.size();
	}
	
	public DeviceItem get(int deviceNum)
	{
		if ( deviceNum >= 0 && deviceNum < m_deviceItems.size() )	
			return m_deviceItems.get(deviceNum);
		return null;
	}
	
	public void ClearAll()
	{
		m_deviceItems.clear();
	}

	public String getName()
	{
		return m_sName;
	}
	
	public int getStatus()
	{
		return m_nStat;
	}
	
	public void setStatus(int nStat)
	{
		m_nStat 	= nStat;
	}
}
