package mta.yos.zeroconf.helpers;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import mta.yos.zeroconf.domain.Service;
import mta.yos.zeroconf.session.Services;

public abstract class AbstractServicesHelper{
	Logger logger = Logger.getLogger(AbstractServicesHelper.class.getName());
	protected abstract Services getServices();
	private Service find(String serviceId){
		try {
			return getServices().find(serviceId);
		} catch (Exception e){
			logger.throwing(AbstractServicesHelper.class.getName(), "find", e);
		}
		return null;
	}

	public void save(Service service) {
		try {
			getServices().save(service);
		}catch (Exception e){
			logger.throwing(Services.class.getName(), "save", e);
			logger.severe("unable to save service "+service.getId()+". message: "+ e.getMessage());
		}
	}
	
	public Service update(String serviceId, String hostname, int port, String providerClassName){
		Service service = find(serviceId);
		if (service == null){
			service = new Service();
			service.setId(serviceId);
		}
		service.setHostname(hostname);
		service.setPort(port);
		service.setProviderClassName(providerClassName);
		save(service);
		return service;
	}

	
	public void delete(String serviceId){
		try {
			getServices().delete(serviceId);
		} catch (Exception e) {
			logger.severe("unable to delete service ("+serviceId+")");
			logger.throwing(Services.class.getName(), "delete", e);
		}
	}
	
	public List<Service> listAll(){
		List<Service> serviceList = null;
		try {
			serviceList = getServices().listAll();
		} catch (Exception e){
			logger.throwing(Services.class.getName(), "listAll", e);
		}
		if (serviceList == null){
			//logger.finer("got null service list - return an empty list");
			serviceList = new LinkedList<Service>();
		}
		return serviceList;
	}
}