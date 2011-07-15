package test1.m2m;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class OneMany1 implements Serializable{
	
	private static final long serialVersionUID = 496665367501701381L;
	@Id
	private	long id;
	private String desc;
	
	@OneToMany
	private 
	Set<OneMany2> set2;


	/**
	 * @param one2 the one2 to set
	 */
	public OneMany1() {
	}
	public OneMany1(long id, String desc) {
		super();
		this.setId(id);
		this.desc = desc;
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
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setSet2(Set<OneMany2> set2) {
		this.set2 = set2;
	}
	public Set<OneMany2> getSet2() {
		return set2;
	}

	
}
