package mta.yos.zeroconf.service;

import javax.jws.WebService;

@WebService(targetNamespace="http://yos-hp/jmdns-ws/wsdl" )
public interface DeviceManagerService {
	String hello();
}
