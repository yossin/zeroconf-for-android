package mta.yos.zeroconf.helpers;

import java.util.logging.Logger;

import mta.yos.zeroconf.domain.Location;
import mta.yos.zeroconf.domain.Zone;
import mta.yos.zeroconf.session.Zones;

public abstract class AbstractZoneHelper{
	protected abstract Zones getZones();
	static Logger logger = Logger.getLogger(AbstractZoneHelper.class.getName());
	private Zone find(String name){
		try {
			return getZones().find(name);
		} catch (Exception e){
			logger.throwing(AbstractZoneHelper.class.getName(), "find", e);
		}
		return null;
	}
	
	private Zone create(String name){
		Zone zone = new Zone();
		zone.setName(name);
		zone.setLocation(new Location(0,0));
		try {
			getZones().save(zone);
			return zone;
		} catch (Exception e){
			logger.throwing(AbstractZoneHelper.class.getName(), "save", e);
		}
		return null;
	}
	
	public Zone getDefault(){
		String name="default";
		Zone zone = find(name);
		if (zone == null){
			zone = create(name);
		}
		return zone;
	}
}