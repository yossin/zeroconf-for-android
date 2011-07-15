package mta.yos.zeroconf.entities;

import java.util.List;

public interface Entities<E,K> {
	void save(E entity) throws Exception;
	void delete(E entity) throws Exception;
	List<E> listAll() throws Exception;
	E find(K key) throws Exception;
}
