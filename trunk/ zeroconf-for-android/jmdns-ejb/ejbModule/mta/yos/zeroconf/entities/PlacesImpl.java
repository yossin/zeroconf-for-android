package mta.yos.zeroconf.entities;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

@Stateless(name = "Places")
public class PlacesImpl implements Places {
	@PersistenceContext(unitName = "jmdns-ws-jpa")
	private EntityManager entityManager;


	@Override
    public Place find(String key) throws Exception {
    	return entityManager.find(Place.class, key);
    }
	@Override
	public void save(Place entity) throws Exception{
		entityManager.persist(entity);
		entityManager.flush();
//		entityManager.close();
	}
	@Override
	public void delete(Place entity) throws Exception {
		entityManager.remove(entity);
	}
	@Override
	public List<Place> listAll() throws Exception{
    	String q="SELECT o from Place as o";
        return query(q);
	}

	public List<Place> query(String statement) throws Exception{
        Query query = entityManager.createQuery(statement);
        return query.getResultList();
	}
	
}