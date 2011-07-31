package mta.yos.zeroconf.session;

import java.util.List;

import mta.yos.zeroconf.domain.Service;

public interface Services extends EntityManager<Service, String>{

	void deleteByName(String name) throws Exception;
	List<Service> findByName(String name) throws Exception;
}
