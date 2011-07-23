package mta.yos.zeroconf.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Service implements Serializable{
	private static final long serialVersionUID = 816277132910932929L;
	@Id @Column(nullable=false)
    private String fullName;
	private String hostname;
	private String textRecord;
	private int port;
	private String providerClassName;
    
    public Service(){
    }
    
    public Service(String fullName) {
		this.setFullName(fullName);
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

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setProviderClassName(String providerClassName) {
		this.providerClassName = providerClassName;
	}

	public String getProviderClassName() {
		return providerClassName;
	}

    
}
