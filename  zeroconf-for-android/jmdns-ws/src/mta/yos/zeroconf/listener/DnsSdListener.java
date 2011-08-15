package mta.yos.zeroconf.listener;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import mta.yos.zeroconf.bean.UpdateDeviceRequest;
import mta.yos.zeroconf.devices.DeviceProvider;
import mta.yos.zeroconf.devices.ProviderFactory;
import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.Service;
import mta.yos.zeroconf.session.DeviceManager;
import mta.yos.zeroconf.session.Devices;

import com.apple.dnssd.BrowseListener;
import com.apple.dnssd.DNSSD;
import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.ResolveListener;
import com.apple.dnssd.TXTRecord;

/**
 * Application Lifecycle Listener implementation class DnsSdCtxListener
 *
 */
public class DnsSdListener implements ServletContextListener, BrowseListener {
	Logger logger = Logger.getLogger(DnsSdListener.class.getName());
	Timer timer = new Timer();
	DNSSDService browser;
	@EJB
	DeviceManager deviceManager;
	DeviceManagerHelper helper;
	@EJB
	Devices devices;
	@Resource(name="jmdns.QCF")
	QueueConnectionFactory factory;
	@Resource(name="jmdns.updateQ")
	Destination updateQueue;
	@Resource(name="jmdns.deleteQ")
	Destination deleteQueue;
	
	private class DeviceManagerHelper {
		final Logger logger = Logger.getLogger(DeviceManagerHelper.class.getName());
		private void send(UpdateDeviceRequest request, Destination queue) throws JMSException{
			Connection connection=null;
			Session session = null;
			try {
				connection = factory.createConnection();
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Message message = session.createObjectMessage(request);
				MessageProducer producer = session.createProducer(queue);
				producer.send(message);
			} finally {
				close(connection);
				close(session);
			}
		}
		private void send(String text, Destination queue) throws JMSException{
			Connection connection=null;
			Session session = null;
			try {
				connection = factory.createConnection();
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Message message = session.createTextMessage(text);
				MessageProducer producer = session.createProducer(queue);
				producer.send(message);
			} finally {
				close(connection);
				close(session);
			}
		}
		private void close(Session session) {
			try {
				if (session!= null)
					session.close();
			} catch (JMSException e) {
			}
		}
		
		private void close(Connection connection) {
				try {
					if (connection!= null)
						connection.close();
				} catch (JMSException e) {
				}
		}

		public void updateDevice(UpdateDeviceRequest request){
			try {
				send(request, updateQueue);
			} catch (Exception e){
				logger.throwing(DeviceManager.class.getName(), "update", e);
				logger.severe("unable to update device "+request.getServiceId()+". message: "+ e.getMessage());
			}
		}

		public void deleteService(String serviceName){
			try {
				send(serviceName, deleteQueue);
			} catch (Exception e){
				logger.throwing(DeviceManager.class.getName(), "deleteServiceByName", e);
				logger.severe("unable to delete service "+serviceName+". message: "+ e.getMessage());
			}
		}
		
		public List<Device> deviceList(){
			try {
				return deviceManager.deviceList();
			} catch (Exception e){
				logger.throwing(DeviceManager.class.getName(), "deviceList", e);
				return new LinkedList<Device>();
			}
		}

		public void save(String deviceId, int state){
			Device device;
			try {
				device = devices.find(deviceId);
				device.setState(state);
				devices.save(device);
			} catch (Exception e){
				logger.throwing(Devices.class.getName(), "save", e);
				logger.severe("unable to update device "+deviceId+". message: "+ e.getMessage());
			}
		}
		
	}
		

	private class ServiceResolveListener implements ResolveListener{
		String deviceId;
		
		public ServiceResolveListener(String deviceId) {
			this.deviceId = deviceId;
		}

		@Override
		public void operationFailed(DNSSDService arg0, int arg1) {
			logger.severe("ubable to resolve service for device "+ deviceId);
		}

		@Override
		public void serviceResolved(DNSSDService resolver, int flags, int ifIndex, 
				String fullName, String hostname, int port, TXTRecord txtRecord) {
			logger.finer("RESOLVED: flags="+flags+", ifIndex="+ifIndex+
					", name="+fullName+", hostName="+hostname+", port="+port+", txtRecord="+txtRecord);
			String deviceName = txtRecord.getValueAsString("deviceName");
			String providerClassName = txtRecord.getValueAsString("providerClassName");
			String serialNumber= txtRecord.getValueAsString("serialNumber");
			
			UpdateDeviceRequest request = new UpdateDeviceRequest(deviceName, serialNumber, fullName, hostname, port, providerClassName);
			helper.updateDevice(request);
			resolver.stop();
		}
	}

	
	public class CheckServiceTask extends TimerTask{
		ProviderFactory factory = new ProviderFactory();
		private int checkState(Device device){
			try {
				Service service = device.getService();
				DeviceProvider provider = factory.create(service.getProviderClassName(), service.getHostname(), service.getPort());
				return provider.status();
			} catch (Exception e) {
				return 2;
			}
		}
		
		@Override
		public void run() {
			List<Device> deviceList = helper.deviceList();
			for (Device device : deviceList) {
				int state = checkState(device);
				helper.save(device.getId(), state);
			}
		}
		
	}
	
    public DnsSdListener() {
    }

    private void initBrowser(){
		String sd_service="_device._tcp";
		try {
			// initialize browser
			browser = DNSSD.browse(sd_service, this);
		} catch (DNSSDException e) {
			throw new RuntimeException(e);
		}
    }
    private void initTimer(){
		TimerTask task = new CheckServiceTask();
		long delay=1000;
		long period=1000;
		timer.schedule(task, delay, period);
    }
    
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent context){
    	helper = new DeviceManagerHelper();
		initBrowser();
		initTimer();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	browser.stop();
    	timer.cancel();
    	helper = null;
    }

	@Override
	public void operationFailed(DNSSDService arg0, int arg1) {
		logger.severe("error while browsing for services");
	}

	@Override
	public void serviceFound(DNSSDService browser, int flags, int ifIndex,
			String serviceName, String regType, String domain) {
		logger.finer("SERVICE FOUND: flags="+flags+", ifIndex="+ifIndex+", name="+serviceName+", type="+regType+", domain="+domain);
		try {
			DNSSD.resolve(flags, ifIndex, serviceName, regType, domain, new ServiceResolveListener(serviceName));
		} catch (DNSSDException e) {
		}
	}

	@Override
	public void serviceLost(DNSSDService browser, int flags, int ifFlags,
			String serviceName, String regType, String domain) {
		logger.finer("SERVICE LOST: flags="+flags+", ifFlags="+ifFlags+
				", name="+serviceName+", type="+regType+", domain="+domain);
		String fullName=serviceName+"."+regType+domain;
		helper.deleteService(fullName);	
	}

	
}
