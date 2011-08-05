package mta.yos.zeroconf.lamp;

import mta.yos.zeroconf.lamp.Lamp.LampListener;

public abstract class BaseLampApp implements LampListener{
	LampHandler handler;
	Listener listener;
	DeviceRegister register;
	protected BaseLampApp(LampInfo info, DeviceRegister register){
		handler = new SimpleHandler();
		listener = new Listener(info.getPort(), handler);
		this.register = register;
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
		register.unregister();
	}
	
}