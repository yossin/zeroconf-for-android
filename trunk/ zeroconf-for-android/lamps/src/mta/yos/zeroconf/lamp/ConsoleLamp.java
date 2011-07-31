package mta.yos.zeroconf.lamp;


public class ConsoleLamp {

	
	public static void main(String[] args) throws Exception {
		String name="ConsoleLamp";
		int port=9009;
		String provider="mta.yos.zeroconf.providers.JavaLampProvider";
		String serialNumber="123321";
		LampApp app = new LampApp(new ConsoleLampImpl(),name , serialNumber, port, provider);
		app.run();
	}
}
