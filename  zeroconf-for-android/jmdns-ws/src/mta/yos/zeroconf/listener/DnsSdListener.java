package mta.yos.zeroconf.listener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import mta.yos.zeroconf.devices.DeviceProvider;
import mta.yos.zeroconf.devices.ProviderFactory;
import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.helpers.AbstractDevicesHelper;
import mta.yos.zeroconf.helpers.AbstractZoneHelper;
import mta.yos.zeroconf.session.Devices;
import mta.yos.zeroconf.session.Zones;

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
	DevicesHelper devicesHelper;
	ZoneHelper zoneHelper;
	@EJB
	Devices devices;
	@EJB
	Zones zones;
	
	private class ZoneHelper extends AbstractZoneHelper{
		@Override
		protected Zones getZones() {
			return zones;
		}
	}
	
	private class DevicesHelper extends AbstractDevicesHelper{
		@Override
		protected Devices getDevices() {
			return devices;
		}
		@Override
		protected AbstractZoneHelper getZoneHelper() {
			return zoneHelper;
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
			devicesHelper.update(deviceId,deviceName, fullName, hostname, port, providerClassName);
			resolver.stop();
		}
	}

	
	public class CheckServiceTask extends TimerTask{
		ProviderFactory factory = new ProviderFactory();
		private int checkState(Device device){
			try {
				DeviceProvider provider = factory.create(device);
				return provider.status();
			} catch (Exception e) {
				return 2;
			}
		}
		
		@Override
		public void run() {
			List<Device> deviceList = devicesHelper.listAll();
			for (Device device : deviceList) {
				device.setState(checkState(device));
				devicesHelper.save(device);
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
		long period=5000;
		timer.schedule(task, delay, period);
    }
    
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent context){
    	devicesHelper = new DevicesHelper();
    	zoneHelper = new ZoneHelper();
		initBrowser();
		initTimer();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	browser.stop();
    	timer.cancel();
    	devicesHelper = null;
    	zoneHelper = null;
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
		devicesHelper.delete(serviceName);	
	}

	
}
