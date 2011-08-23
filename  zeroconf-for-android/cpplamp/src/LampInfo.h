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
	const std::string name;
	const int port;
	const std::string serial;
public:
	LampInfo(const std::string &name, const int &port, const std::string &serial);
	virtual ~LampInfo();
	const int& getPort();
	const std::string& getSerial();
	const std::string& getName();
};

}

#endif /* LAMPINFO_H_ */
