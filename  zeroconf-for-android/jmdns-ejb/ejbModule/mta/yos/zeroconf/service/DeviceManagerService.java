package mta.yos.zeroconf.service;

import java.util.List;

import javax.jws.WebService;

import mta.yos.zeroconf.domain.Device;

@WebService
public interface DeviceManagerService {
	List<Device> deviceList() throws Exception;
}
