package mta.yos.zeroconf.entities;

import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

@Stateful(name = "Lamps")
public class LampsImpl implements Lamps{
	@PersistenceContext(unitName = "jmdns-ws-jpa", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;


	@Override
    public Lamp find(String key) throws Exception {
    	return entityManager.find(Lamp.class, key);
    }
	@Override
	public void add(Lamp entity) throws Exception{
		entityManager.persist(entity);
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