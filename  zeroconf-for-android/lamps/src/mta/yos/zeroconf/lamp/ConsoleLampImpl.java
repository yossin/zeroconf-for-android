package mta.yos.zeroconf.lamp;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ConsoleLampImpl implements Lamp{
	int state;
	private void printStatus(){
		System.out.println("lamp is "+ (state==1?"on":"off"));
	}
	
	@Override
	public int status() {
		//printStatus();
		return state;
	}

	@Override
	public void turnOff() {
		printStatus();
		state=0;
		System.out.println("TURN OFF!");
		printStatus();
	}

	@Override
	public void turnOn() {
		printStatus();
		state=1;
		System.out.println("TURN ON!");
		printStatus();
	}

	@Override
	public void display() {
		System.out.println("press q for exit");
		status();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			listener.onStartup();
			String line=null;
			do {
				System.out.println(">");
				line = reader.readLine();
				if (line.toLowerCase().contains("state")){
					printStatus();
				} else if (line.toLowerCase().contains("turn on")){
					turnOn();
				} else if (line.toLowerCase().contains("turn off")){
					turnOff();
				}
			}while (line.toLowerCase().contains("q") == false);
		} catch (Exception e){
		}
		listener.onShutdown();
	}

	LampListener listener;
	@Override
	public void setListener(LampListener listener) {
		this.listener=listener;
		listener.setLamp(this);
	}
}