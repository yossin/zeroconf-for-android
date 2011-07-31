package mta.yos.zeroconf.lamp;

public class LampApp {
	LampHandler handler;
	Listener listener;
	DeviceRegister register;
	Lamp lamp;
	public LampApp(Lamp lamp, String name, String serialNumber, int basePort, String providerClass){
		this.lamp=lamp;
		handler = new SimpleHandler(lamp);
		listener = new Listener(basePort, handler);
		register = new DeviceRegister(name, serialNumber, basePort, providerClass);
	}
	
	public void run() throws Exception{
		try {
			start();
		} finally {
			stop();
		}
	}
	void start() throws Exception{
		listener.start();
		register.register(listener.getListenPort());
		lamp.display();
	}
	void stop(){
		listener.interrupt();
		register.close();
	}
	
}