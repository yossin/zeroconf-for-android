package mta.yos.zeroconf.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.sun.xml.bind.CycleRecoverable;

@Entity
public class Zone  implements Serializable,CycleRecoverable {

	private static final long serialVersionUID = 6345655593639832657L;
	@Id @Column(nullable=false)
    private String name;
    
	@Embedded @JsonIgnore 
    private Location location;
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.REFRESH, mappedBy="zone", orphanRemoval=false)
    private Set<Device> devices;
    
    public Zone(){
    }
    
    public Zone(String name, Location location) {
    	this.name = name;
		this.location =location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public Location getLocation() {
		return location;
	}

	@JsonIgnore
	public void setLocation(Location location) {
		this.location = location;
	}

	public Set<Device> getDevices() {
		return devices;
	}
	
	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}
	
	public void addDevice(Device device){
		device.setZone(this);
		if (devices == null) devices = new HashSet<Device>();
		devices.add(device);
	}

	
	public int getState() {
		// 0 - off
		// 1 - on
		// ?? unimplemented ?? 2 - unknown
		// ?? unimplemented ?? 3 - partial
		for (Device device : devices) {
			if (device.isOn() == false) return 0;
		}
		// all on
		return 1;
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		Zone zone = new Zone();
		zone.setName(name);
		return zone;
	}

    
}
