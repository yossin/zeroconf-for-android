package mta.yos.zeroconf.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.session.DeviceManager;

@Stateless
@WebService(
        portName = "DeviceManagerServicePort",
        serviceName = "DeviceManagerService",
        endpointInterface = "mta.yos.zeroconf.service.DeviceManagerService")
public class DeviceManagerServiceImpl implements DeviceManagerService {

	@EJB
	DeviceManager manager;
	
	@Override
	public List<Device> deviceList() throws Exception {
		return manager.deviceList();
	}

}
