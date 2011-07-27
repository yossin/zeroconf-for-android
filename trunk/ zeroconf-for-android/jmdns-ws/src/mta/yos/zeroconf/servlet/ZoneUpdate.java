package mta.yos.zeroconf.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mta.yos.zeroconf.devices.DeviceProvider;
import mta.yos.zeroconf.devices.ProviderFactory;
import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.Zone;
import mta.yos.zeroconf.session.Devices;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * Servlet implementation class ZoneUpdate
 */
public class ZoneUpdate extends HttpServlet {
	private static final long serialVersionUID = 2621683221688398318L;

	public ZoneUpdate() {
        super();
    }

	ObjectMapper mapper = new ObjectMapper();
	@EJB
	Devices devices;

	private void updateStatus(Device device , int requestedState) throws Exception{
		DeviceProvider provider = factory.create(device);
		if (requestedState == 0){
			provider.turnOff();
		} else if (requestedState == 1){
			provider.turnOn();
		}
	}
	
	ProviderFactory factory = new ProviderFactory();
	private void handleDevice(String id, int requestedState){
		try {
			Device device = devices.find(id);
			int state= device.getState();
			if (state == requestedState || state==2) return;
			updateStatus(device, requestedState);

		} catch (Exception e) {
			log(e.getMessage());
		}		
	}
	
    protected void service(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	// change the state only
    	String data = request.getParameter("data");
    	if (data==null){
    		return;
    	}
    
    	try {
        	List<Zone> zones = mapper.readValue(data.getBytes(), new TypeReference<List<Zone>>(){});
        	for (Zone zone : zones) {
				Set<Device> devices = zone.getDevices();
				for (Device device : devices) {
					handleDevice(device.getId(), device.getState());
				}
			}
    	}catch (Throwable e){
    		e.printStackTrace();
    	}
    	
    }

}
