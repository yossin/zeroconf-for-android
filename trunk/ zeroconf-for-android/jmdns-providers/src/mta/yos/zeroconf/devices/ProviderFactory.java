package mta.yos.zeroconf.devices;

import java.lang.reflect.Constructor;

public class ProviderFactory {
	public DeviceProvider create(String className, String host, int port) throws Exception{
		Class<DeviceProvider> clazz = (Class<DeviceProvider>) Class.forName(className);
		Class<?> parameterTypes[] = new Class[]{String.class, Integer.TYPE};
		Constructor<DeviceProvider> constructor = clazz.getConstructor(parameterTypes);
		Object initargs[] = new Object[]{host, port};
		return constructor.newInstance(initargs);
	}
}
