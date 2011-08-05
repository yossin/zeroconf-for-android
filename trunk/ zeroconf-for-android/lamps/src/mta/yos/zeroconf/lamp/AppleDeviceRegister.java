package mta.yos.zeroconf.lamp;

import com.apple.dnssd.DNSSD;
import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDRegistration;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.RegisterListener;
import com.apple.dnssd.TXTRecord;

class AppleDeviceRegister implements DeviceRegister{
	
	LampInfo info;

	public static class Listener implements RegisterListener{
		
		@Override
		public void operationFailed(DNSSDService arg0, int arg1) {
			
		}

		@Override
		public void serviceRegistered(DNSSDRegistration arg0, int arg1,
				String arg2, String arg3, String arg4) {
			// TODO Auto-generated method stub
			
		}
		
	}
	RegisterListener listener = new Listener();
	DNSSDRegistration registration;
	
	public AppleDeviceRegister(LampInfo info) {
		this.info= info;
	}
	private TXTRecord createRecord() throws DNSSDException{
		TXTRecord record = new TXTRecord();
		record.set("deviceName", info.getDeviceName());
		record.set("providerClassName", info.getProvider());
		record.set("serialNumber", info.getSerialNumber());
		return record;
	}
	@Override
	public void register(int listenPort) throws Exception{
		int id = RegisterUtil.calculateId(info, listenPort);
		String serviceName=RegisterUtil.generateServiceName(info, id);
		TXTRecord record  = createRecord();
		int ifIndex=0;
		int flags=DNSSD.DEFAULT;
		registration = DNSSD.register(flags, ifIndex, serviceName, type, null, null, listenPort, record, listener);
		//registration = DNSSD.register(serviceName, type, listenPort, listener, record);
	}
	@Override
	public void unregister(){
		if (registration != null)
			registration.stop();
	}
}