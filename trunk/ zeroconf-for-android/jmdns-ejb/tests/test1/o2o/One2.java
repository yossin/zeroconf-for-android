package test1.o2o;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class One2 implements Serializable{

	private static final long serialVersionUID = -663645722693992725L;
	@Id
	String id;
	String desc;
	@OneToOne
	One1 one1;

	/**
	 * @return the one1
	 */
	public One1 getOne1() {
		return one1;
	}
	/**
	 * @param one1 the one1 to set
	 */
	public void setOne1(One1 one1) {
		this.one1 = one1;
	}
	public One2() {
	}
	public One2(String id, String desc) {
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
