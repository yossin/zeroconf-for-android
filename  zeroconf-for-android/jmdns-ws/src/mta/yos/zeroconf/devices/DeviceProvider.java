package mta.yos.zeroconf.devices;

public interface DeviceProvider {
	void turnOff();
	void turnOn();
	int status();
}
