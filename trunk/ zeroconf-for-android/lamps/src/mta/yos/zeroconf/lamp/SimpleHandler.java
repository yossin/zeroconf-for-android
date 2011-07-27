package mta.yos.zeroconf.lamp;


public class SimpleHandler implements LampHandler{
	Lamp lamp;

	public SimpleHandler(Lamp lamp) {
		this.lamp = lamp;
	}


	@Override
	public String handle(String command) {
		if (command.equalsIgnoreCase("turnon")){
			lamp.turnOn();
			return "turned on";
		} else if (command.equalsIgnoreCase("turnoff")){
			lamp.turnOff();
			return "turned off";
		} else if (command.equalsIgnoreCase("status")){
			return Integer.toString(lamp.status());
		} else {
			return "unknown command";
		}
	}
	
}