package mta.yos.zeroconf.entities;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name = "Lamps")
public class LampsImpl implements Lamps {
	@PersistenceContext(unitName = "jmdns-ws-jpa")
	private EntityManager entityManager;


	@Override
    public Lamp find(String key) throws Exception {
    	return entityManager.find(Lamp.class, key);
    }
	@Override
	public void save(Lamp entity) throws Exception{
		Lamp entity1 = find(entity.getId());
		if (entity1 == null){
			entityManager.persist(entity);
		} else {
			delete(entity1);
			entityManager.persist(entity);
		}
		entityManager.flush();
//		entityManager.close();
	}
	@Override
	public void delete(Lamp entity) throws Exception {
		entityManager.remove(entity);
	}
	@Override
	public List<Lamp> listAll() throws Exception{
    	String q="SELECT o from Lamp as o";
        return query(q);
	}

	public List<Lamp> query(String statement) throws Exception{
        Query query = entityManager.createQuery(statement);
        return query.getResultList();
	}
	
}