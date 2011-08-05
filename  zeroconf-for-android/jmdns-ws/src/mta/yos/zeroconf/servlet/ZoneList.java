package mta.yos.zeroconf.servlet;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mta.yos.zeroconf.domain.Zone;
import mta.yos.zeroconf.session.Zones;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Servlet implementation class ZoneList
 */
public class ZoneList extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @EJB
    private Zones zones;
	ObjectMapper mapper = new ObjectMapper();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
	    	List<Zone> list = zones.findDefinedZones();
	    	mapper.writeValue(response.getWriter(), list);
		} catch (Exception e){
			throw new ServletException(e);
		}
	}

}
