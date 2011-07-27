package mta.yos.zeroconf.lamp;

import java.net.InetAddress;
import java.net.NetworkInterface;

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
	
	public DeviceRegister(String name, int basePort, String providerClassName) {
		this.name = name;
		this.basePort= basePort;
		this.providerClassName = providerClassName;
	}

	private static String getMacAddress() throws Exception{
		InetAddress local = InetAddress.getLocalHost();
		NetworkInterface ni = NetworkInterface.getByInetAddress(local);
		byte []mac = ni.getHardwareAddress();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			String m=String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : "");
			builder.append(m);
		}
		return builder.toString();
	}

	private int calculateId(int listenPort){
		return listenPort-basePort+1;
	}
	
	private TXTRecord createRecord(int id) throws DNSSDException{
		TXTRecord record = new TXTRecord();
		record.set("deviceName", name+id);
		record.set("providerClassName", providerClassName);
		return record;
	}
	public void register(int listenPort) throws Exception{
		int id = calculateId(listenPort);
		String serviceName=name+id+"["+getMacAddress()+"]";
		TXTRecord record  = createRecord(id);
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