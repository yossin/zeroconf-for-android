/*
 * LampInfo.h
 *
 *  Created on: 16/08/2011
 *      Author: yos
 */
#ifndef LAMPINFO_H_
#define LAMPINFO_H_
#include <string>
namespace mta_yos_zeroconf_lamp {

class LampInfo {
private:
	const int port;
	const std::string deviceName;
	const std::string providerClassName;
	const std::string serialNumber;
	std::string serviceName;
public:
	LampInfo(const std::string &name, const int &port, const std::string &serial);
	virtual ~LampInfo();
	const int& getPort();
	const std::string& getSerialNumber();
	const std::string& getServiceName();
	const std::string& getDeviceName();
	const std::string& getProviderClassName();
	void setServiceName(const std::string& name1);

};

}

#endif /* LAMPINFO_H_ */
