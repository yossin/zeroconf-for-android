package mta.yos.zeroconf.session;

import java.util.List;

import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.Service;
import mta.yos.zeroconf.domain.Zone;

public interface DeviceManager {
	Service updateService(String serviceId, String serviceName, String hostname, int port, String providerClassName) throws Exception;
	void deleteServiceByName(String serviceName) throws Exception;
	Device updateDevice(String deviceName, String serviceId, String serviceName, String hostname, int port, String providerClassName) throws Exception;
	void deleteDevice(String deviceId) throws Exception;
	Zone getDefaultZone() throws Exception;
	List<Device> deviceList() throws Exception;
}
