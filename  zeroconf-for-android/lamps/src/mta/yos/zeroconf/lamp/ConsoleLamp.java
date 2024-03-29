package mta.yos.zeroconf.lamp;


public class ConsoleLamp {

	public static void main(String[] args) throws Exception {
		String name="ConsoleLamp";
		int port=9009;
		String provider="mta.yos.zeroconf.providers.JavaLampProvider";
		String serialNumber="123321";
		LampInfo info = new LampInfo(name, port, provider, serialNumber);
		AppleLampApp app = new AppleLampApp(info);
		Lamp lamp = new ConsoleLampImpl();
		lamp.setListener(app);
		lamp.display();
	}
}
