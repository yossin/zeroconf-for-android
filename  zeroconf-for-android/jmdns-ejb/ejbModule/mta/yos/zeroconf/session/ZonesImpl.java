package mta.yos.zeroconf.session;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mta.yos.zeroconf.domain.Zone;

@Stateless(name = "Zones")
public class ZonesImpl implements Zones {
	@PersistenceContext(unitName = "jmdns-ws-jpa")
	private EntityManager entityManager;


	@Override
    public Zone find(String key) throws Exception {
    	return entityManager.find(Zone.class, key);
    }
	@Override
	public void save(Zone entity) throws Exception{
		Zone entity1 = find(entity.getName());
		if (entity1 == null){
			entityManager.persist(entity);
		} else {
			entityManager.merge(entity);
		}
	}
	@Override
	public void delete(String key) throws Exception {
		Zone entity = find(key);
		if (entity == null) return;
		entityManager.remove(entity);
	}
	@Override
	public List<Zone> listAll() throws Exception{
    	String q="SELECT o from Zone as o";
        return query(q);
	}

	public List<Zone> query(String statement) throws Exception{
        Query query = entityManager.createQuery(statement);
        return query.getResultList();
	}
	@Override
	public List<Zone> findDefinedZones() throws Exception {
        Query query = entityManager.createNamedQuery("Zone.findDefinedZones");
        return query.getResultList();
	}
	
}