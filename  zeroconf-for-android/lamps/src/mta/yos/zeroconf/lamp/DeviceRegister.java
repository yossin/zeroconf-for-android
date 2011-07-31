package mta.yos.zeroconf.lamp;

import com.apple.dnssd.DNSSD;
import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDRegistration;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.RegisterListener;
import com.apple.dnssd.TXTRecord;

class DeviceRegister {
	
	final static String type="_device._tcp";
	int basePort;
	String name;
	String serialNumber;
	String providerClassName;

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
	
	public DeviceRegister(String name, String serialNumber, int basePort, String providerClassName) {
		this.name = name;
		this.serialNumber = serialNumber;
		this.basePort= basePort;
		this.providerClassName = providerClassName;
	}

	private int calculateId(int listenPort){
		return listenPort-basePort+1;
	}
	
	private TXTRecord createRecord() throws DNSSDException{
		TXTRecord record = new TXTRecord();
		record.set("deviceName", name+serialNumber);
		record.set("providerClassName", providerClassName);
		record.set("serialNumber", serialNumber);
		return record;
	}
	public void register(int listenPort) throws Exception{
		int id = calculateId(listenPort);
		String serviceName=name+id+"["+serialNumber+"]";
		TXTRecord record  = createRecord();
		int ifIndex=0;
		int flags=DNSSD.DEFAULT;
		registration = DNSSD.register(flags, ifIndex, serviceName, type, null, null, listenPort, record, listener);
		//registration = DNSSD.register(serviceName, type, listenPort, listener, record);
	}
	
	public void close(){
		if (registration != null)
			registration.stop();
	}
}