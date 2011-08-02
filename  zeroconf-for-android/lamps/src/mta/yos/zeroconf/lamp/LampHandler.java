package mta.yos.zeroconf.lamp;

public interface LampHandler{
	String handle(String command);
	void setLamp(Lamp lamp);
}