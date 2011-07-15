package test1.o2o;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class One1 implements Serializable{
	private static final long serialVersionUID = 496665367501701381L;
	@Id
	String id;
	String desc;
	@OneToOne
	One2 one2;
	/**
	 * @return the one2
	 */
	public One2 getOne2() {
		return one2;
	}
	/**
	 * @param one2 the one2 to set
	 */
	public void setOne2(One2 one2) {
		this.one2 = one2;
	}
	public One1() {
	}
	public One1(String id, String desc) {
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
