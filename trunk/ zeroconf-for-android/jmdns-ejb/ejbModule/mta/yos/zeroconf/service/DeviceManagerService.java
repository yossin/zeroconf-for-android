package mta.yos.zeroconf.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.Zone;

@WebService
public interface DeviceManagerService {
	List<Device> deviceList() throws Exception;
	void assignDeviceToZone(@WebParam(name="deviceId")String deviceId, @WebParam(name="zoneName")String zoneName) throws Exception;
	void createZone(@WebParam(name="zone")Zone zone) throws Exception;
	void deleteDevice(@WebParam(name="deviceId")String deviceId) throws Exception;
	void updateDeviceRadius(@WebParam(name="deviceId")String deviceId, @WebParam(name="radius") long radius) throws Exception;
}
