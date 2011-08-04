package mta.yos.zeroconf.service;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

@Stateless
@WebService(
        portName = "DeviceManagerServicePort",
        serviceName = "DeviceManagerService",
        targetNamespace = "http://yos-hp/jmdns-ws/wsdl",
        endpointInterface = "mta.yos.zeroconf.service.DeviceManagerService")
public class DeviceManagerServiceImpl implements DeviceManagerService {

	
	@WebMethod
	public String hello(){
		return "hello";
	}

}
