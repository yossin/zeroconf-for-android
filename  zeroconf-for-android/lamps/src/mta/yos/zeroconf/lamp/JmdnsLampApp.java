package mta.yos.zeroconf.lamp;


public class JmdnsLampApp extends BaseLampApp{
	public JmdnsLampApp(LampInfo info){
		super(info, new JmdnsDeviceRegister(info));
	}
}