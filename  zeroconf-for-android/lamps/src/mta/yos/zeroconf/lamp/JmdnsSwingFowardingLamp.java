package mta.yos.zeroconf.lamp;


public class JmdnsSwingFowardingLamp {

	
	public static void main(String[] args) throws Exception {
		String name="JmdnsSwingLamp";
		int port=7007;
		String provider="mta.yos.zeroconf.providers.JavaLampForwardingProvider";
		String serialNumber="123454321";
		LampInfo info = new LampInfo(name, port, provider, serialNumber);
		BaseLampApp app = new JmdnsLampApp(info);
		Lamp lamp = new SwingLampImpl();
		lamp.setListener(app);
		lamp.display();
		// wait for unregistration
		//Thread.sleep(10000);
	}
}
