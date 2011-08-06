package mta.yos.zeroconf.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.Location;
import mta.yos.zeroconf.domain.Zone;
import mta.yos.zeroconf.helper.LocationHelper;
import mta.yos.zeroconf.session.DeviceLocationRoles;
import mta.yos.zeroconf.session.DeviceManager;
import mta.yos.zeroconf.session.Devices;
import mta.yos.zeroconf.session.Services;
import mta.yos.zeroconf.session.Zones;

@Stateless
@WebService(
        portName = "DeviceManagerServicePort",
        serviceName = "DeviceManagerService",
        endpointInterface = "mta.yos.zeroconf.service.DeviceManagerService")
public class DeviceManagerServiceImpl implements DeviceManagerService {

	@EJB
	DeviceManager manager;
	@EJB
	DeviceLocationRoles locationRoles;
	@EJB
	Devices devices;
	@EJB
	Services services;
	@EJB
	Zones zones;
	
	
	@Override
	public List<Device> deviceList() throws Exception {
		return manager.deviceList();
	}

	@Override
	public void createZone(Zone zone) throws Exception {
		Location location = zone.getLocation();
		//override location (in case location is set to 0,0)
		location = LocationHelper.createLocation(location);
		zone.setLocation(location);
		zones.save(zone);
		
	}

	@Override
	public void assignDeviceToZone(String deviceId, String zoneName)
			throws Exception {
		Zone zone = zones.find(zoneName);
		Device device = devices.find(deviceId);
		device.setZone(zone);
		devices.save(device);		
	}

	@Override
	public void updateDeviceRadius(String deviceId, long radius)
			throws Exception {
		Device device = devices.find(deviceId);
		device.setRadius(radius);
		devices.save(device);
	}

	@Override
	public void deleteDevice(String deviceId) throws Exception {
		services.delete(deviceId);
		devices.delete(deviceId);
	}

}
