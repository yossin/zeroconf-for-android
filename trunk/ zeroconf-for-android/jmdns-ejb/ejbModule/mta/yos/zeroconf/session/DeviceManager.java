package mta.yos.zeroconf.session;

import java.util.List;

import mta.yos.zeroconf.bean.UpdateDeviceRequest;
import mta.yos.zeroconf.bean.UpdateServiceRequest;
import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.Location;
import mta.yos.zeroconf.domain.Service;
import mta.yos.zeroconf.domain.Zone;

public interface DeviceManager {
	void deleteServiceByName(String serviceName) throws Exception;
	void deleteDevice(String deviceId) throws Exception;
	Zone getDefaultZone() throws Exception;
	List<Device> deviceList() throws Exception;
	void updateDevice(UpdateDeviceRequest request) throws Exception;
	Service updateService(UpdateServiceRequest request) throws Exception;
	void updateDeviceStatus(Location location) throws Exception;
	void updateDeviceState(Device device, int newState) throws Exception;
}
