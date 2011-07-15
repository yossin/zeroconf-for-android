package mta.yos.zeroconf.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Lamp {
/*
	public static class LampKey implements Serializable{
		private static final long serialVersionUID = 4765358061735399989L;
		String name;
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	LampKey key;
	public LampKey getKey() {
		return key;
	}
	public void setKey(LampKey key) {
		this.key = key;
	}
	}
	*/
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	long id;

	//@GeneratedValue(strategy=GenerationType.TABLE)
	String name;
	Boolean on;
	
	public Lamp(){
	}
	
	public Lamp(String name, Boolean on) {
		super();
		this.name = name;
		this.on = on;
	}
	public void setOn(Boolean on) {
		this.on = on;
	}
	public Boolean getOn() {
		return on;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public long getId() {
		return id;
	}
}
