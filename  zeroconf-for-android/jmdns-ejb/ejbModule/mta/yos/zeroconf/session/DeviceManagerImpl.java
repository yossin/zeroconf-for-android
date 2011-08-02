package mta.yos.zeroconf.session;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mta.yos.zeroconf.bean.UpdateDeviceRequest;
import mta.yos.zeroconf.bean.UpdateServiceRequest;
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
	

}
