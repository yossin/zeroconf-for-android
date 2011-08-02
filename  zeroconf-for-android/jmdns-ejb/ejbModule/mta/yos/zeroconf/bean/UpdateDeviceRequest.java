package mta.yos.zeroconf.bean;

import java.io.Serializable;

public class UpdateDeviceRequest extends UpdateServiceRequest implements Serializable{
	private static final long serialVersionUID = -8083500342509862272L;
	String deviceName;
	public UpdateDeviceRequest(String deviceName, String serviceId,
			String serviceName, String hostname, int port,
			String providerClassName) {
		super(serviceId, serviceName, hostname, port, providerClassName);
		this.deviceName = deviceName;
	}
	public String getDeviceName() {
		return deviceName;
	}	
}
