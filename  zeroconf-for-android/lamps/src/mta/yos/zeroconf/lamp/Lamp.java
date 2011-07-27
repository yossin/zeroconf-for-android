package mta.yos.zeroconf.lamp;

public interface Lamp {
	int status();
	void turnOff();
	void turnOn();
	void display();
}