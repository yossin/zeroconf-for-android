package mta.yos.zeroconf.lamp;


public class SwingLamp {

	
	public static void main(String[] args) throws Exception {
		String name="SwingLamp";
		int port=7007;
		String provider="mta.yos.zeroconf.providers.JavaLampProvider";
		String serialNumber="1234321";
		LampInfo info = new LampInfo(name, port, provider, serialNumber);
		LampApp app = new LampApp(info);
		Lamp lamp = new SwingLampImpl();
		lamp.setListener(app);
		lamp.display();
	}
}
