/*
 * LampInfo.cpp
 *
 *  Created on: 16/08/2011
 *      Author: yos
 */

#include "LampInfo.h"

namespace mta_yos_zeroconf_lamp {

LampInfo::LampInfo(){
	LampInfo("",0,"");
}
LampInfo::LampInfo(std::string name, int port, std::string serial) {
	LampInfo::name=name;
	LampInfo::port=port;
	LampInfo::serial=serial;
}

LampInfo::~LampInfo() {
}

int LampInfo::getPort(){
	return port;
}

std::string LampInfo::getName(){
	return name;
}

std::string LampInfo::getSerial(){
	return serial;
}

}
