package mta.yos.zeroconf.lamp;


public class AppleLampApp extends BaseLampApp{
	public AppleLampApp(LampInfo info){
		super(info, new AppleDeviceRegister(info));
	}
}