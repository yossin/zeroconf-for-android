package mta.yos.zeroconf.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


@Entity
@NamedQuery(name="Service.findByName", query="SELECT o from Service as o where o.name=:name")
public class Service implements Serializable{
	private static final long serialVersionUID = 816277132910932929L;
	@Id @Column(nullable=false)
    private String id;
    private String name;
	private String hostname;
	private String textRecord;
	private int port;
	private String providerClassName;
    
    public Service(){
    }
    
    public Service(String id) {
		this.setId(id);
	}


	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getHostname() {
		return hostname;
	}

	public void setTextRecord(String textRecord) {
		this.textRecord = textRecord;
	}

	public String getTextRecord() {
		return textRecord;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setProviderClassName(String providerClassName) {
		this.providerClassName = providerClassName;
	}

	public String getProviderClassName() {
		return providerClassName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

    
}
