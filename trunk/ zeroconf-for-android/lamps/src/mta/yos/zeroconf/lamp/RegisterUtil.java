package mta.yos.zeroconf.lamp;

class RegisterUtil{

	static int calculateId(LampInfo info, int listenPort){
		return listenPort-info.getPort()+1;
	}

	static String generateServiceName(LampInfo info, int id) {
		return info.getName()+id+"["+info.getSerialNumber()+"]";
	}
	
}