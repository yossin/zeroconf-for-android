package tests;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.Location;
import mta.yos.zeroconf.domain.Zone;
import mta.yos.zeroconf.session.Devices;
import mta.yos.zeroconf.session.Zones;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Servlet implementation class Test1
 */
public class Test1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @EJB
    private Zones zones;
    @EJB
    private Devices devices;
    
    
	ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
    
    private void toJason() throws Exception{
    	List<Zone> list = zones.listAll();
    	//ArrayIn
    	
    	String res = mapper.writeValueAsString(list);
    	System.out.println(res);
    }
    
    
    private void testPlaces2(ServletRequest request) throws Exception{    	
    	String zoneId = request.getParameter("id");
		int id= zoneId==null?1:Integer.parseInt(zoneId);
		
		listZones();
		addZone(id);
		listZones();
		//addToDefZone(name);
        
		
    	//listZones();
        
    }
    
    private void addZone(int id) throws Exception{
    	
    	int loc= (int)(Math.random()*100);
    	String name="Zone"+id;
        Zone zone1 = zones.find(name);
        if (zone1 == null) {
        	zone1= new Zone(name, new Location(loc, loc));
            zones.save(zone1);
        }
        
        for (int i=1; i<=2;i++){
        	String deviceName="Device-"+i;
        	double onRand = Math.random();
            Device device = new Device(deviceName+"["+name+"]",deviceName,onRand>0.5);
        	device.setZone(zone1);
        	devices.save(device);
        }
    }
    private void addToDefZone(String name) throws Exception{
    	
    	int loc= (int)(Math.random()*100);
        Zone zone1 = zones.find(name);
        if (zone1 == null) {
        	return;
        }
        Zone zone0 = zones.find("default");
        if (zone0 == null) {
        	zone0= new Zone("default", new Location(loc, loc));
            zones.save(zone0);
        }
        
        Set<Device> devices = zone1.getDevices();
        for (Device device : devices) {
			device.setZone(zone0);
			this.devices.save(device);
		}
        
    }
    
    private void listZones() throws Exception{
        List<Zone> list = zones.listAll();

        for (Zone zone: list) {
        	Location location= zone.getLocation();
        	System.out.println(zone.getName() + " location: (" +location.getX()+","+location.getY() +")");
        	Set<Device> devices = zone.getDevices();
        	for (Device device : devices) {
        		System.out.println(device.getName() + " is " + (device.isOn()?"on":"off")+"!");
			}
        }

    }
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

	        
	        //testDevice();
	      //testDevices();
//	      testDevices();
			testPlaces2(request); 
			toJason();
//			testParent(request); 
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		


	}

	/*
	private void testLapms(){
	      //DeviceKey key1 = new DeviceKey();
      //key1.setName("device1");
      Device device1 = new Device();
      //device1.setKey(key1);
      device1.setName("device1");
      device1.setOn(false);
      devices.add(device1);
      
      //DeviceKey key2 = new DeviceKey();
      //key2.setName("device2");
      Device device2 = new Device();
      //device2.setKey(key2);
      device1.setName("device2");
      device2.setOn(true);
      devices.add(device2);
      
      List<Device> all = devices.listAll();
      for (Device device : all) {
			System.out.println(device.getName()+" is "+(device.getOn()!=null&&device.getOn()?"on":"off"));
			devices.delete(device);
		}
  
	}
	*/
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
