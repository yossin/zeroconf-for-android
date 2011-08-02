package mta.yos.zeroconf.lamp;

public class LampInfo {
	String name;
	int port;
	String provider;
	String serialNumber;
	public LampInfo(String name, int port, String provider, String serialNumber) {
		this.name = name;
		this.port = port;
		this.provider = provider;
		this.serialNumber = serialNumber;
	}
	public String getName() {
		return name;
	}
	public int getPort() {
		return port;
	}
	public String getProvider() {
		return provider;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public String getDeviceName(){
		return name+serialNumber;
	}
}
