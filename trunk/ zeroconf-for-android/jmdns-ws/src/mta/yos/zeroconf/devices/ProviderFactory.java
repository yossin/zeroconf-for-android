package mta.yos.zeroconf.devices;

import java.lang.reflect.Constructor;

import mta.yos.zeroconf.domain.Device;

public class ProviderFactory {
	public DeviceProvider create(Device device) throws Exception{
		Class<DeviceProvider> clazz = (Class<DeviceProvider>) Class.forName(device.getService().getProviderClassName());
		Class<?> parameterTypes[] = new Class[]{Device.class};
		Constructor<DeviceProvider> constructor = clazz.getConstructor(parameterTypes);
		Object initargs[] = new Object[]{device};
		return constructor.newInstance(initargs);
	}
}
