package mta.yos.zeroconf.devices;

import java.io.IOException;

public interface DeviceProvider {
	void turnOff() throws IOException;
	void turnOn() throws IOException;
	int status() throws IOException;
}
