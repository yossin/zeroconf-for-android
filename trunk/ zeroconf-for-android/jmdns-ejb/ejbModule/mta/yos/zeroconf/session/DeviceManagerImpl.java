package mta.yos.zeroconf.session;

import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mta.yos.zeroconf.bean.UpdateDeviceRequest;
import mta.yos.zeroconf.bean.UpdateServiceRequest;
import mta.yos.zeroconf.devices.DeviceProvider;
import mta.yos.zeroconf.devices.ProviderFactory;
import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.DeviceLocationRole;
import mta.yos.zeroconf.domain.Location;
import mta.yos.zeroconf.domain.Service;
import mta.yos.zeroconf.domain.Zone;
import mta.yos.zeroconf.helper.LocationHelper;

@Stateless(name = "DeviceManager")
public class DeviceManagerImpl implements DeviceManager{

	@EJB
	Services services; 
	@EJB
	Devices devices; 
	@EJB
	Zones zones; 
	@EJB
	DeviceLocationRoles locationRoles;
	
	ProviderFactory factory = new ProviderFactory();
	
	@Override
	public Service updateService(UpdateServiceRequest request) throws Exception{
		Service service = services.find(request.getServiceId());
		if (service == null){
			service = new Service();
			service.setId(request.getServiceId());
		}
		service.setName(request.getServiceName());
		service.setHostname(request.getHostname());
		service.setPort(request.getPort());
		service.setProviderClassName(request.getProviderClassName());
		services.save(service);
		return service;
	}


	@Override
	public void deleteServiceByName(String serviceName) throws Exception{
		List<Service> serviceList = services.findByName(serviceName);
		for (Service service : serviceList) {
			String id = service.getId();
			Device device = devices.find(id);
			device.setState(2);
			devices.save(device);
		}
		services.deleteByName(serviceName);
	}


	@Override
	public Zone getDefaultZone() throws Exception{
		String name="default";
		Zone zone = zones.find(name);
		if (zone == null){
			zone = new Zone();
			zone.setName(name);
			// latitude, longitude
			// haifa: 32580964, 34929402
			// tlv-university: 32083738, 34770669
			zone.setLocation(LocationHelper.createLocation(0, 0));
			zones.save(zone);
		}
		return zone;
	}


	@Override
	public void deleteDevice(String deviceId) throws Exception {
		devices.delete(deviceId);
	}


	@Override
	public List<Device> deviceList() throws Exception {
		return devices.listAll();
	}


	@Override
	public void updateDevice(UpdateDeviceRequest request) throws Exception {
		Device device = devices.find(request.getServiceId());
		if (device == null){
			device = new Device();
			device.setId(request.getServiceId());
			device.setName(request.getDeviceName());
		}
		
		Service service = device.getService();
		if (service == null) {
			service = updateService(request);
			device.setService(service);
		}
		Zone zone = device.getZone();
		if (zone == null){
			zone = getDefaultZone();
			device.setZone(zone);
		}
		devices.save(device);
	}

	
	private static double toDouble(long x){
		return (double)x/(double)1000000;
	}
	
	private static long calcDistance(Location location1, Location location2){
		 double lat1 = toDouble(location1.getLatitude());
		 double lng1 = toDouble(location1.getLongitude());
		 double lat2 = toDouble(location2.getLatitude());
		 double lng2 = toDouble(location2.getLongitude());
		 
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
	
	private int calcDesiredState(Location clientLocation, Location zoneLocation, long radius){
		long distance = calcDistance(clientLocation, zoneLocation);
		if (distance<radius){
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void updateDeviceStatus(Location location) throws Exception {
		List<Device> deviceList = devices.listAll();
		for (Device device : deviceList) {
			if (device.getRadius()!=null){
				int newState = calcDesiredState(location, device.getZone().getLocation(), device.getRadius());
				updateDeviceState(device, newState);
			}
		}		
	}


	@Override
	public void updateDeviceState(Device device, int newState) throws Exception {
		if (newState != device.getState()){
			Service service = device.getService();
			DeviceProvider provider = factory.create(service.getProviderClassName(), service.getHostname(), service.getPort());
			if (newState == 0){
				provider.turnOff();
			} else if (newState == 1){
				provider.turnOn();
			}
		}
		
	}
	

}
