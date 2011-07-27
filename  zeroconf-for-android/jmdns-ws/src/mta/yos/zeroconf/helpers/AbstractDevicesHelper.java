package mta.yos.zeroconf.helpers;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.Service;
import mta.yos.zeroconf.domain.Zone;
import mta.yos.zeroconf.session.Devices;

public abstract class AbstractDevicesHelper{
	Logger logger = Logger.getLogger(AbstractDevicesHelper.class.getName());
	protected abstract Devices getDevices();
	protected abstract AbstractZoneHelper getZoneHelper();
	private Device find(String deviceId){
		try {
			return getDevices().find(deviceId);
		} catch (Exception e){
			logger.throwing(AbstractDevicesHelper.class.getName(), "find", e);
		}
		return null;
	}

	public void save(Device device) {
		try {
			getDevices().save(device);
		}catch (Exception e){
			logger.throwing(Devices.class.getName(), "save", e);
			logger.severe("unable to save device "+device.getId()+". message: "+ e.getMessage());
		}
	}
	
	public void update(String deviceId, String deviceName, String serviceId, String hostname, int port, String providerClassName){
		Device device = find(deviceId);
		if (device == null){
			device = new Device();
			device.setId(deviceId);
			device.setName(deviceName);
		}
		
		Service service = device.getService();
		if (service == null) {
			service = new Service(serviceId);
			device.setService(service);
		}
		Zone zone = device.getZone();
		if (zone == null){
			zone = getZoneHelper().getDefault();
			device.setZone(zone);
		}
		service.setHostname(hostname);
		service.setPort(port);
		service.setProviderClassName(providerClassName);
		save(device);
	}

	
	public void delete(String deviceId){
		try {
			getDevices().delete(deviceId);
		} catch (Exception e) {
			logger.severe("unable to delete device ("+deviceId+")");
			logger.throwing(Devices.class.getName(), "delete", e);
		}
	}
	
	public List<Device> listAll(){
		List<Device> deviceList = null;
		try {
			deviceList = getDevices().listAll();
		} catch (Exception e){
			logger.throwing(Devices.class.getName(), "listAll", e);
		}
		if (deviceList == null){
			//logger.finer("got null device list - return an empty list");
			deviceList = new LinkedList<Device>();
		}
		return deviceList;
	}
}