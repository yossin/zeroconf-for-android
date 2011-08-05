package mta.yos.zeroconf.session;

import java.util.List;

import mta.yos.zeroconf.domain.Zone;

public interface Zones extends EntityManager<Zone, String>{
	List<Zone> findDefinedZones() throws Exception;
}
