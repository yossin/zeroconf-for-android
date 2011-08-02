package com.HomeRemoteApp;

public class DeviceItem 
{
	static final int DeviceStat_OFF 	= 0;
	static final int DeviceStat_ON 		= 1;
	static final int DeviceStat_Unknown = 2;
	
	private String 				m_sID;
	private String 				m_sName;
	private int		 			m_nStat = DeviceStat_Unknown;

	public DeviceItem(String sID, String sName, int nStatus)
	{
		m_sID	 	= sID;
		m_sName 	= sName;
		m_nStat 	= nStatus;
	}
		
	public String getID()
	{
		return m_sID;
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
		m_nStat = nStat;
	}
}
