package com.HomeRemoteApp;

import java.lang.reflect.Array;
import java.util.Vector;

import com.google.android.maps.MapActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.text.style.UpdateLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ViewManualAdapter extends BaseExpandableListAdapter 
{
	private HomeRemote  		m_homeRemote;
    private LayoutInflater 		m_inflater;
    private boolean				m_UpdateGroupsFromUINedded = false;
    
    private Vector<GroupItem> 	m_GroupItems = new Vector<GroupItem>();
        
	public void setContext(HomeRemote homeRemote)
	{
		m_homeRemote = homeRemote;
        m_inflater = LayoutInflater.from(m_homeRemote);
	}

    public void setContent(Vector<GroupItem> GroupItems)
    {
    	m_GroupItems = GroupItems;
    }

    public long getChildId(int groupPosition, int childPosition) 
    {
        return childPosition;
    }

    
    public View getGenericView(final int groupPosition, final int childPosition, View convertView, ViewGroup parent) 
    { 
        View v = null;
    
        if (childPosition == -1) /* group */
        {
        	v = m_inflater.inflate(R.layout.group_row, parent, false); 
        	final CheckBox cb = (CheckBox)v.findViewById( R.id.group_status );
        	cb.setChecked( getGroupStatus(groupPosition) == GroupItem.GroupStat_ON );
            cb.setText( getGroupText(groupPosition) );
            
            cb.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) 
                {
            		SetGroupItemStatus(groupPosition, cb.isChecked());
            		if (cb.isChecked())
            		{
            			for (int ii = 0 ; ii < getDeviceCount(groupPosition) ; ii ++ )
            			{
            				if (getDeviceStatus(groupPosition, childPosition) != DeviceItem.DeviceStat_Unknown)
            				SetDeviceItemStatus(groupPosition, ii, true);
            			}
            			notifyDataSetChanged();
            		}
                }
            });
        }
        else  /* device */
        {
        	v = m_inflater.inflate(R.layout.device_row, parent, false); 
        	final CheckBox cb = (CheckBox)v.findViewById( R.id.device_status );
        	
        	int deviceStatus = getDeviceStatus(groupPosition, childPosition);
        	cb.setClickable(deviceStatus != DeviceItem.DeviceStat_Unknown);
        	cb.setEnabled  (deviceStatus != DeviceItem.DeviceStat_Unknown);
        	cb.setBackgroundColor(
        			deviceStatus != DeviceItem.DeviceStat_Unknown?
        					Color.TRANSPARENT : Color.RED);
        	cb.setChecked  (deviceStatus == DeviceItem.DeviceStat_ON);
        	
            cb.setText( getDeviceText(groupPosition, childPosition) );
       
            cb.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) 
                {
            		SetDeviceItemStatus(groupPosition, childPosition, cb.isChecked());
            		if (!cb.isChecked())
            		{
            			SetGroupItemStatus(groupPosition, false);            			
            			notifyDataSetChanged();
            		}
                }
            });
        }
        	
        return v;
    }
  
    public int getChildrenCount(int groupPosition) 
    {
    	return getDeviceCount(groupPosition); 
    }
    
    public int getDeviceCount(int groupPosition) 
    {
    	if (m_GroupItems.size() < groupPosition)
    		return 0;
    	return m_GroupItems.get(groupPosition).Size();
    }

    public int getGroupCount() 
    {
    	return m_GroupItems.size();
    }
    
    public DeviceItem getDeviceItem(int groupPosition, int childPosition)
    {
    	if (groupPosition < m_GroupItems.size()   			&&
        	childPosition < getDeviceCount(groupPosition)	)
    		return m_GroupItems.get(groupPosition).get(childPosition);
    	return null;
    }
    public CharSequence getDeviceText(int groupPosition, int childPosition)
    {
    	DeviceItem di = getDeviceItem(groupPosition, childPosition);
    	if (di != null)
    		return di.getName();
		return "";
    }

    public int getDeviceStatus(int groupPosition, int childPosition)
    {
    	int GroupStatus = getGroupStatus(groupPosition);
       	DeviceItem di = getDeviceItem(groupPosition, childPosition);
       	int DeviceStatus = DeviceItem.DeviceStat_Unknown;
       	if (di != null)
       		DeviceStatus = di.getStatus();

       	if (GroupStatus == GroupItem.GroupStat_ON && 
       		DeviceStatus != DeviceItem.DeviceStat_Unknown)
       	{
    		/* update device status */
       		DeviceStatus = DeviceItem.DeviceStat_ON;
    		SetDeviceItemStatus(groupPosition, childPosition, true);
    	}
    	
       	return DeviceStatus;
    }   

    public GroupItem getGroupItem(int groupPosition)
    {
    	if (groupPosition < m_GroupItems.size())
    		return m_GroupItems.get(groupPosition);
		return null;	
    }   
    
    public int getGroupStatus(int groupPosition)
    {
    	GroupItem gi = getGroupItem(groupPosition);
    	if (gi != null)
    		return gi.getStatus();
		return GroupItem.GroupStat_OFF;	
    }   
    
    public CharSequence getGroupText(int groupPosition)
    {
    	GroupItem gi = getGroupItem(groupPosition);
    	if (gi != null)
    		return gi.getName();
		return "";	
    }   
    
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {	
    	return getGenericView(groupPosition, childPosition, convertView, parent);
    }
    
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)  
    {
        return getGenericView(groupPosition, -1, convertView, parent);
    }
    

    public long getGroupId(int groupPosition)  
    {
        return groupPosition;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) 
    {
        return true;
    }

    public boolean hasStableIds() 
    {
        return true;
    }

	public Object getChild(int i, int j) 
	{
		return null;
	}

	public Object getGroup(int i) 
	{
		return null;
	}
	
	public void SetGroupItemStatus(int groupPosition, boolean bStatus)
    {
    	if (groupPosition < m_GroupItems.size())
    		m_GroupItems.get(groupPosition).setStatus(bStatus ? GroupItem.GroupStat_ON : GroupItem.GroupStat_OFF);
    }  
	
	public void SetDeviceItemStatus(int groupPosition, int childPosition, boolean bStatus)
    {
    	if (groupPosition < m_GroupItems.size()   			&&
            childPosition < getChildrenCount(groupPosition)	)
    		m_GroupItems.get(groupPosition).get(childPosition).setStatus(bStatus ? GroupItem.GroupStat_ON : GroupItem.GroupStat_OFF);
    }  
	
	public Vector<GroupItem> getGroups()
	{
		return m_GroupItems;
	}
}
