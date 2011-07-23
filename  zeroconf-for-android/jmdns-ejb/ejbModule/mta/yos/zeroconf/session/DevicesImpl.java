package mta.yos.zeroconf.session;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mta.yos.zeroconf.domain.Device;

@Stateless(name = "Devices")
public class DevicesImpl implements Devices {
	@PersistenceContext(unitName = "jmdns-ws-jpa")
	private EntityManager entityManager;


	@Override
    public Device find(String key) throws Exception {
    	return entityManager.find(Device.class, key);
    }
	@Override
	public void save(Device entity) throws Exception{
		Device entity1 = find(entity.getId());
		if (entity1 == null){
			entityManager.persist(entity);
		} else {
			entityManager.merge(entity);
		}
	}
	@Override
	public void delete(String key) throws Exception {
		Device entity = find(key);
		if (entity == null) return;
		entityManager.remove(entity);
	}
	@Override
	public List<Device> listAll() throws Exception{
    	String q="SELECT o from Device as o";
        return query(q);
	}

	public List<Device> query(String statement) throws Exception{
        Query query = entityManager.createQuery(statement);
        return query.getResultList();
	}
	
}