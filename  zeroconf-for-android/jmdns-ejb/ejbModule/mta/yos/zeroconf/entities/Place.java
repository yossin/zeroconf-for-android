package mta.yos.zeroconf.entities;

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

@Entity
public class Place  implements Serializable {

	private static final long serialVersionUID = 6345655593639832657L;
	@Id @Column(nullable=false)
    private String name;
	@Embedded
    private Location location;
    //, mappedBy="place",
	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="place")
    private Set<Lamp> lamps;
    
    public Place(){
    }
    
    public Place(String name, Location location) {
		this.name = name;
		this.location =location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Set<Lamp> getLamps() {
		return lamps;
	}
	
	public void setLamps(Set<Lamp> lamps) {
		this.lamps = lamps;
	}
	
	public void addLamp(Lamp lamp){
		lamp.setPlace(this);
		if (lamps == null) lamps = new HashSet<Lamp>();
		lamps.add(lamp);
	}
	

    
}
