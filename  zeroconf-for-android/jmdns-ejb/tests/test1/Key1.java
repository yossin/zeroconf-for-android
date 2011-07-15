package test1;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
class Key1 implements Serializable{
	private static final long serialVersionUID = 8067347212143500720L;
	private String id;
	private String id2;
	public Key1(String id, String id2) {
		super();
		this.setId(id);
		this.setId2(id2);
	}
	public Key1() {
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}
	public String getId2() {
		return id2;
	}
	
	
}