package mta.yos.zeroconf.providers;

import mta.yos.zeroconf.devices.DeviceProvider;
import mta.yos.zeroconf.domain.Device;

public class JavaLampProvider implements DeviceProvider{

	private Device device;
	
	public JavaLampProvider(Device device) {
		this.device = device;
	}

	@Override
	public void turnOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnOn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int status() {
		// TODO Auto-generated method stub
		return 2;
	}

}
