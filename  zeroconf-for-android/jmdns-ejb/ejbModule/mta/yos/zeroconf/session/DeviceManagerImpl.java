package mta.yos.zeroconf.session;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.Location;
import mta.yos.zeroconf.domain.Service;
import mta.yos.zeroconf.domain.Zone;

@Stateless(name = "DeviceManager")
public class DeviceManagerImpl implements DeviceManager{

	@EJB
	Services services; 
	@EJB
	Devices devices; 
	@EJB
	Zones zones; 
	
	
	@Override
	public Service updateService(String serviceId, String serviceName, String hostname, int port, String providerClassName) throws Exception{
		Service service = services.find(serviceId);
		if (service == null){
			service = new Service();
			service.setId(serviceId);
		}
		service.setName(serviceName);
		service.setHostname(hostname);
		service.setPort(port);
		service.setProviderClassName(providerClassName);
		services.save(service);
		return service;
	}


	@Override
	public void deleteServiceByName(String serviceName) throws Exception{
		services.deleteByName(serviceName);
	}


	@Override
	public Device updateDevice(String deviceName, String serviceId, String serviceName,
			String hostname, int port, String providerClassName) throws Exception{
		Device device = devices.find(serviceId);
		if (device == null){
			device = new Device();
			device.setId(serviceId);
			device.setName(deviceName);
		}
		
		Service service = device.getService();
		if (service == null) {
			service = updateService(serviceId, serviceName, hostname, port, providerClassName);
			device.setService(service);
		}
		Zone zone = device.getZone();
		if (zone == null){
			zone = getDefaultZone();
			device.setZone(zone);
		}
		devices.save(device);
		return device;
	}

	@Override
	public Zone getDefaultZone() throws Exception{
		String name="default";
		Zone zone = zones.find(name);
		if (zone == null){
			zone = new Zone();
			zone.setName(name);
			zone.setLocation(new Location(0,0));
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
	

}
