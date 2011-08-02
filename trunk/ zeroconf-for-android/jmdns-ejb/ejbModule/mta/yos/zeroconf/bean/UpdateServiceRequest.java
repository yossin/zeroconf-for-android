package mta.yos.zeroconf.bean;

import java.io.Serializable;

public class UpdateServiceRequest implements Serializable{
	private static final long serialVersionUID = 5392414787349680908L;
	String serviceId;
	String serviceName;
	String hostname;
	int port;
	String providerClassName;
	public UpdateServiceRequest(String serviceId,
			String serviceName, String hostname, int port,
			String providerClassName) {
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.hostname = hostname;
		this.port = port;
		this.providerClassName = providerClassName;
	}
	public String getServiceId() {
		return serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public String getHostname() {
		return hostname;
	}
	public int getPort() {
		return port;
	}
	public String getProviderClassName() {
		return providerClassName;
	}
	
}
