import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.jmdns.impl.ServiceInfoImpl;

import junit.framework.TestCase;


public class MdnsTest extends TestCase{
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		jmdns = JmDNS.create();
		
	}

	
	JmDNS jmdns = null;
	private static ServiceInfo createSI(){
		
		// name= foobar
		// qualified service name= foobar._http._tcp.local.
		String type="_http._tcp.local.";
		String name="foobar";
		//String subtype;
		int port=1234;
		//int weight=100;
		//int priority=100;
		//boolean persistent=false;
		String text="fooooo";
		return ServiceInfo.create(type, name, port, text);
	}
	
	public void xtest1_register() throws Exception{
		System.out.println("Host: " + jmdns.getHostName() );
		System.out.println("Interface: " + jmdns.getInterface() );
		String type="_http._tcp.local.";
		String name="foobar";
		int port=1234;
		String text="fooooo";
		jmdns.registerService(ServiceInfo.create(type, name, port, text));
	}
	public void test2_list() throws Exception{
		System.out.println("Host: " + jmdns.getHostName() );
		System.out.println("Interface: " + jmdns.getInterface() );
		String q = "_http._tcp.local.";
		q="";
		ServiceInfo si[] = jmdns.list(q);
		for (int i = 0; i < si.length; i++) {
			System.out.println("Service 0 : " + si[ i ].getServer() + "--"
					+ si[ i ].getPort() + "--" + si[ i ].getNiceTextString() );
		}
		//jmdns.addServiceTypeListener( new MyServiceTypeListener() );
	}
}
