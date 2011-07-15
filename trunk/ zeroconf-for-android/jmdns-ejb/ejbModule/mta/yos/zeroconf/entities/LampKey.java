package mta.yos.zeroconf.entities;

import java.io.Serializable;

public class LampKey implements Serializable {

	
	private static final long serialVersionUID = 699495215766555387L;
	String name;
	String place;
	
	public LampKey(){}
	public LampKey(String name, String place) {
		this.place = place;
		this.name = name;
	}
	@Override
	public int hashCode() {
		return (place+name).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LampKey){
			LampKey key = (LampKey) obj;
			return name.equals(key.name) && place.equals(key.place);
		}
		return false;
	}
	
	
}
