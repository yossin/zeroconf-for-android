package mta.yos.zeroconf.lamp;

public interface Lamp {
	public static interface LampListener{
		void onStartup() throws Exception;
		void onShutdown();
		void setLamp(Lamp lamp);
	}
	int status();
	void turnOff();
	void turnOn();
	void display();
	void setListener(LampListener listener);
}