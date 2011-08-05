package mta.yos.zeroconf.helper;

import mta.yos.zeroconf.domain.Location;

public class LocationHelper {
	private final static Location DEFAULT_LOCATION = new Location(32083738, 34770669);

	public static Location createLocation(long latitude, long longitude){
		if (latitude ==0 || longitude ==0){
			return DEFAULT_LOCATION;
		}
		return new Location(latitude, longitude);
	}

	public static Location createLocation(Location location) {
		return createLocation(location.getLatitude(), location.getLongitude());
	}
}
