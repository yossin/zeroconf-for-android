package mta.yos.zeroconf.session;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mta.yos.zeroconf.domain.Service;

@Stateless(name = "Services")
public class ServicesImpl implements Services {
	@PersistenceContext(unitName = "jmdns-ws-jpa")
	private EntityManager entityManager;


	@Override
    public Service find(String key) throws Exception {
    	return entityManager.find(Service.class, key);
    }

	@Override
    public List<Service> findByName(String name) throws Exception {
    	String q="SELECT o from Service as o where o.name="+name;
        return query(q);
    }

	@Override
	public void save(Service entity) throws Exception{
		Service entity1 = find(entity.getId());
		if (entity1 == null){
			entityManager.persist(entity);
		} else {
			entityManager.merge(entity);
		}
	}
	@Override
	public void delete(String key) throws Exception {
		Service entity = find(key);
		if (entity == null) return;
		entityManager.remove(entity);
	}

	@Override
	public void deleteByName(String name) throws Exception {
		List<Service> services = findByName(name);
		if (services == null) return;
		for (Service service : services) {
			entityManager.remove(service);
		}
	}

	@Override
	public List<Service> listAll() throws Exception{
    	String q="SELECT o from Service as o";
        return query(q);
	}

	public List<Service> query(String statement) throws Exception{
        Query query = entityManager.createQuery(statement);
        return query.getResultList();
	}
	
}