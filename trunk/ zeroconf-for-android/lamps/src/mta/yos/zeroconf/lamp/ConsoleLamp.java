package mta.yos.zeroconf.lamp;


public class ConsoleLamp {

	public static void main(String[] args) throws Exception {
		LampApp app = new LampApp(new ConsoleLampImpl(), "ConsoleLamp",9009,"mta.yos.zeroconf.providers.JavaLampProvider");
		app.run();
	}
}
