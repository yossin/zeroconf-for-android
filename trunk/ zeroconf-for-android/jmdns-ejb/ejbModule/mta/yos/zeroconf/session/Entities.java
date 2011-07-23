package mta.yos.zeroconf.session;

import java.util.List;

public interface Entities<E,K> {
	void save(E entity) throws Exception;
	void delete(K keys) throws Exception;
	List<E> listAll() throws Exception;
	E find(K key) throws Exception;
}
