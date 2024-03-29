/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package mta.yos.zeroconf.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.sun.xml.bind.CycleRecoverable;


@Entity
public class Device implements Serializable, CycleRecoverable{
	private static final long serialVersionUID = 1974238629718666354L;
	@Id @Column(nullable=false)
    private String id;
	@Column(nullable=false)
    private String name;
	// 0 - off
	// 1 - on
	// 2 - unknown
    private int state;    
    @ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.REFRESH) @JsonIgnore
    private Zone zone;

    @JsonIgnore
    private Long radius;
    
    @ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.REFRESH) @JsonIgnore
    private DeviceLocationRole locationRole;

   // @MapsId @OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE, orphanRemoval=false, optional=true) @JsonIgnore
    @OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE, orphanRemoval=false, optional=true) @PrimaryKeyJoinColumn @JsonIgnore
    private Service service;
    
    public Device(){
    }
    
    public Device(String id, String name, boolean on) {
		this.id=id;
    	this.name = name;
		setOn(on);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	@JsonIgnore
	public void setOn(boolean on){
		this.state = (on?1:0);
	}
	@JsonIgnore
    public boolean isOn(){
    	return state==1;
    }
    

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@JsonIgnore
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	@JsonIgnore
	public Zone getZone() {
		return zone;
	}

	@JsonIgnore
	public void setService(Service service) {
		this.service = service;
	}
	@JsonIgnore
	public Service getService() {
		return service;
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		Device device = new Device();
		device.setId(id);
		device.setName(name);
		device.setService(service);
		device.setState(state);
		return device;
	}
	@JsonIgnore
	public void setLocationRole(DeviceLocationRole locationRole) {
		this.locationRole = locationRole;
	}

	@JsonIgnore
	public DeviceLocationRole getLocationRole() {
		return locationRole;
	}

    @JsonIgnore
	public void setRadius(Long radius) {
		this.radius = radius;
	}

    @JsonIgnore
	public Long getRadius() {
		return radius;
	}	
    
}
