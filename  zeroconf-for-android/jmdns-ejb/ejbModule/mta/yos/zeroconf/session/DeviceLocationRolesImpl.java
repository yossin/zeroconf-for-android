package mta.yos.zeroconf.session;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mta.yos.zeroconf.domain.Device;
import mta.yos.zeroconf.domain.DeviceLocationRole;

@Stateless(name = "DeviceLocationRoles")
public class DeviceLocationRolesImpl implements DeviceLocationRoles {
	@PersistenceContext(unitName = "jmdns-ws-jpa")
	private EntityManager entityManager;


	@Override
    public DeviceLocationRole find(String key) throws Exception {
    	return entityManager.find(DeviceLocationRole.class, key);
    }
	@Override
	public void save(DeviceLocationRole entity) throws Exception{
		DeviceLocationRole entity1 = find(entity.getName());
		if (entity1 == null){
			entityManager.persist(entity);
		} else {
			entityManager.merge(entity);
		}
		Set<Device> devices = entity.getDevices();
		if (devices==null) return;
		for (Device device : devices) {
			entityManager.persist(device);
		}
	}
	@Override
	public void delete(String key) throws Exception {
		DeviceLocationRole entity = find(key);
		if (entity == null) return;
		entityManager.remove(entity);
	}
	@Override
	public List<DeviceLocationRole> listAll() throws Exception{
    	String q="SELECT o from DeviceLocationRole as o";
        return query(q);
	}

	public List<DeviceLocationRole> query(String statement) throws Exception{
        Query query = entityManager.createQuery(statement);
        return query.getResultList();
	}
	
}