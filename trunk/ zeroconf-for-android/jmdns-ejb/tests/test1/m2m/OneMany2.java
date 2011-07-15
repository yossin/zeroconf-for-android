package test1.m2m;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OneMany2 implements Serializable{
	private static final long serialVersionUID = 496665367501701381L;
	@Id
	String id;
	String desc;
	
	@ManyToOne
	OneMany1 one1;

	/**
	 * @param one2 the one2 to set
	 */
	public OneMany2() {
	}
	public OneMany2(String id, String desc) {
		super();
		this.id = id;
		this.desc = desc;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
