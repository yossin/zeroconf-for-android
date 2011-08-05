package mta.yos.zeroconf.servlet;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mta.yos.zeroconf.domain.Location;
import mta.yos.zeroconf.session.DeviceManager;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * Servlet implementation class ZoneUpdate
 */
public class LocationUpdate extends HttpServlet {
       
	private static final long serialVersionUID = 8411727266586814602L;
	ObjectMapper mapper = new ObjectMapper();
	@EJB
	DeviceManager manager;

	public LocationUpdate() {
        super();
    }
	

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	String data = request.getParameter("data");
    	if (data==null){
    		return;
    	}
    	
    	List<Location> locations = mapper.readValue(data.getBytes(), new TypeReference<List<Location>>(){});
    	// expected to find only one..
    	Location location = locations.get(0);
    	try {
			manager.updateDeviceStatus(location);
		} catch (Exception e) {
			throw new ServletException("error while updating location", e);
		}

    }

}
