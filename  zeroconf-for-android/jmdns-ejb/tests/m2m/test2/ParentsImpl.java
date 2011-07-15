package m2m.test2;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name = "Parents")
public class ParentsImpl implements Parents {
	@PersistenceContext(unitName = "jmdns-ws-jpa")
	private EntityManager entityManager;


	@Override
    public Parent find(ParentPk key) throws Exception {
    	return entityManager.find(Parent.class, key);
    }
	@Override
	public void save(Parent entity) throws Exception{
		entityManager.persist(entity);
		entityManager.flush();
//		entityManager.close();
	}
	@Override
	public void delete(Parent entity) throws Exception {
		entityManager.remove(entity);
	}
	@Override
	public List<Parent> listAll() throws Exception{
    	String q="SELECT o from Parent as o";
        return query(q);
	}

	public List<Parent> query(String statement) throws Exception{
        Query query = entityManager.createQuery(statement);
        return query.getResultList();
	}
	
}