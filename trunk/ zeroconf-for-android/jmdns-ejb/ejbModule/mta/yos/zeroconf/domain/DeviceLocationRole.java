package mta.yos.zeroconf.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DeviceLocationRole  implements Serializable {
	private static final long serialVersionUID = 2525420789685371886L;

	@Id @Column(nullable=false)
    private String name;
    
    private long radius;
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.REFRESH, mappedBy="locationRole", orphanRemoval=false)
	//@JoinColumn(name="DEVICELOCATIONROLE_NAME")
    private Set<Device> devices;
    
    public DeviceLocationRole(){
    }
    
    public DeviceLocationRole(String name, int radius) {
    	this.name = name;
    	this.setRadius(radius);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Device> getDevices() {
		return devices;
	}
	
	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}
	
	public void addDevice(Device device){
		if (devices == null) devices = new HashSet<Device>();
		devices.add(device);
	}


	public void setRadius(long radius) {
		this.radius = radius;
	}

	public long getRadius() {
		return radius;
	}   
}
