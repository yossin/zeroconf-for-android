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
	std::string name;
	int port;
	std::string serial;
public:
	LampInfo();
	LampInfo(std::string name, int port, std::string serial);
	virtual ~LampInfo();
	int getPort();
	std::string getSerial();
	std::string getName();
};

}

#endif /* LAMPINFO_H_ */
