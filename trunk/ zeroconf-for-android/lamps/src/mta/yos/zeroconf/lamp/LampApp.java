package mta.yos.zeroconf.lamp;

import mta.yos.zeroconf.lamp.Lamp.LampListener;

public class LampApp implements LampListener{
	LampHandler handler;
	Listener listener;
	DeviceRegister register;
	public LampApp(LampInfo info){
		handler = new SimpleHandler();
		listener = new Listener(info.getPort(), handler);
		register = new DeviceRegister(info);
	}
	
	public void setLamp(Lamp lamp){
		handler.setLamp(lamp);
	}
	

	@Override
	public void onStartup() throws Exception {
		listener.start();
		register.register(listener.getListenPort());
	}

	@Override
	public void onShutdown() {
		listener.interrupt();
		register.close();
	}
	
}