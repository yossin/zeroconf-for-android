package mta.yos.zeroconf.entities;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

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
		if (entityManager.contains(entity)){
			entityManager.refresh(entity);
		}else {
			entityManager.persist(entity);
		}
		entityManager.flush();
//		entityManager.close();
	}
	@Override
	public void delete(Zone entity) throws Exception {
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
	
}