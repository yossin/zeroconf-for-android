/*
 * LampInfo.cpp
 *
 *  Created on: 16/08/2011
 *      Author: yos
 */

#include "LampInfo.h"

namespace mta_yos_zeroconf_lamp {

LampInfo::LampInfo(const std::string &name1, const int &port1, const std::string &serial1)
	: name(name1), port(port1), serial(serial1){
}

LampInfo::~LampInfo() {
}

const int& LampInfo::getPort(){
	return port;
}

const std::string& LampInfo::getName(){
	return name;
}

const std::string& LampInfo::getSerial(){
	return serial;
}

}
