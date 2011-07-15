package tests;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import m2m.test2.Child;
import m2m.test2.Parent;
import m2m.test2.ParentPk;
import m2m.test2.Parents;
import mta.yos.zeroconf.entities.Lamp;
import mta.yos.zeroconf.entities.Location;
import mta.yos.zeroconf.entities.Place;
import mta.yos.zeroconf.entities.Places;

import org.superbiz.calculator.CalculatorLocal;

/**
 * Servlet implementation class Test1
 */
public class Test1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @EJB
    private Places places;
    
    @EJB
    private CalculatorLocal calculator;
        
    @EJB
    private Parents parents;
    
    private void testPlaces2(ServletRequest request) throws Exception{    	
		String placeName = request.getParameter("name");
		String name = placeName==null?"place":placeName;
    	
    	int loc= (int)(Math.random()*100);
        Place place1 = new Place(name, new Location(loc, loc));
        for (int i=1; i<=2;i++){
            Lamp lamp = new Lamp("Lamp-"+i,Math.random()>0);
        	lamp.setPlace(place1);
        	place1.addLamp(lamp);
        }
        places.save(place1);
        
        List<Place> list = places.listAll();

        for (Place place: list) {
        	Location location= place.getLocation();
        	System.out.println(place.getName() + " location: (" +location.getX()+","+location.getY() +")");
        	Set<Lamp> lamps = place.getLamps();
        	for (Lamp lamp : lamps) {
        		System.out.println(lamp.getName() + " is " + (lamp.isOn()?"on":"off")+"!");
			}
        }
    	
    }
    private void testParent(ServletRequest request) throws Exception{    	
		String parentName = request.getParameter("name");
		String name = parentName==null?"parent":parentName;
    	

		ParentPk pk = new ParentPk();
		pk.firstName=name+"-first";
		pk.lastName=name+"-last";
        Parent parent1 = new Parent();
        parent1.age=(int)(Math.random()*100);
        parent1.id=pk;
        parent1.children=new HashSet<Child>();
        for (int i=1; i<=2;i++){
            Child child = new Child();
            child.id=i;
            child.parent=parent1;
            parent1.children.add(child);
        }
        parents.save(parent1);
        
        List<Parent> list = parents.listAll();

        for (Parent parent: list) {
        	System.out.println(parent.id.firstName + ", "+parent.id.lastName + ", "+ parent.age);
        	Set<Child> children= parent.children;
        	for (Child child : children) {
        		System.out.println("child id:"+child.id);
			}
        }
    	
    }

    private void testCalc() throws Exception{
        int x= calculator.sum(11, 12);
        System.out.println("sum="+x);
    }   
    
//    private void testLamps() throws Exception{
//        lamps.add(new Lamp("lamp1", false));
//        lamps.add(new Lamp("lamp2", true));
//        
//        List<Lamp> all = lamps.listAll();
//        for (Lamp lamp : all) {
//  			System.out.println(lamp.getName()+" is "+(lamp.getOn()!=null&&lamp.getOn()?"on":"off"));
//  			lamps.delete(lamp);
//  		}
//    	
//    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

	        
	        //testLamp();
	      //testLamps();
//	      testLamps();
//			testPlaces2(request); 
			testParent(request); 
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		


	}

	/*
	private void testLapms(){
	      //LampKey key1 = new LampKey();
      //key1.setName("lamp1");
      Lamp lamp1 = new Lamp();
      //lamp1.setKey(key1);
      lamp1.setName("lamp1");
      lamp1.setOn(false);
      lamps.add(lamp1);
      
      //LampKey key2 = new LampKey();
      //key2.setName("lamp2");
      Lamp lamp2 = new Lamp();
      //lamp2.setKey(key2);
      lamp1.setName("lamp2");
      lamp2.setOn(true);
      lamps.add(lamp2);
      
      List<Lamp> all = lamps.listAll();
      for (Lamp lamp : all) {
			System.out.println(lamp.getName()+" is "+(lamp.getOn()!=null&&lamp.getOn()?"on":"off"));
			lamps.delete(lamp);
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
