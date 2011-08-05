package mta.yos.zeroconf.lamp;

public interface DeviceRegister {
	final static String type="_device._tcp";

	public abstract void unregister();

	public abstract void register(int listenPort) throws Exception;

}
