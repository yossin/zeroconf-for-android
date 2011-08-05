package mta.yos.zeroconf.lamp;


public class AppleSwingLamp {

	
	public static void main(String[] args) throws Exception {
		String name="AppleSwingLamp";
		int port=8008;
		String provider="mta.yos.zeroconf.providers.JavaLampProvider";
		String serialNumber="1234321";
		LampInfo info = new LampInfo(name, port, provider, serialNumber);
		BaseLampApp app = new AppleLampApp(info);
		Lamp lamp = new SwingLampImpl();
		lamp.setListener(app);
		lamp.display();
	}
}
