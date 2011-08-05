package mta.yos.zeroconf.lamp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class JmdnsDeviceRegister implements DeviceRegister {
	LampInfo info;

	public JmdnsDeviceRegister(LampInfo info) {
		this.info = info;
		try {
			jmdns = JmDNS.create();
		} catch (IOException e) {
			throw new RuntimeException("unable to initialize JmDNS", e);
		}
	}

	@Override
	public void unregister() {
		jmdns.unregisterService(service);
	}
	
	JmDNS jmdns;
	ServiceInfo service;
	@Override
	public void register(int listenPort) throws Exception {
		int id = RegisterUtil.calculateId(info, listenPort);
		String serviceName=RegisterUtil.generateServiceName(info, id);
		int port=info.getPort();
		
		Map<String,Object> record = new HashMap<String, Object>();
		record.put("deviceName", info.getDeviceName());
		record.put("providerClassName", info.getProvider());
		record.put("serialNumber", info.getSerialNumber());
		
		service = ServiceInfo.create(type+".local", serviceName, port, 0,0, record);
		
		jmdns.registerService(service);
	}

}
