package mta.yos.zeroconf.lamp;


public class AppleSwingLamp2 {

	
	public static void main(String[] args) throws Exception {
		String name="AppleSwingLamp";
		int port=8009;
		String provider="mta.yos.zeroconf.providers.JavaLampProvider";
		String serialNumber="1234322";
		LampInfo info = new LampInfo(name, port, provider, serialNumber);
		BaseLampApp app = new AppleLampApp(info);
		Lamp lamp = new SwingLampImpl();
		lamp.setListener(app);
		lamp.display();
	}
}
