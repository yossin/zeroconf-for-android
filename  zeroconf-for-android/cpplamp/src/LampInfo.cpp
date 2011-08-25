/*
 * LampInfo.cpp
 *
 *  Created on: 16/08/2011
 *      Author: yos
 */

#include "LampInfo.h"

namespace mta_yos_zeroconf_lamp {

LampInfo::LampInfo(const std::string &deviceName1, const int &port1, const std::string &serialNumber1)
	: deviceName(deviceName1), port(port1), serialNumber(serialNumber1){
	serviceName=deviceName1+"["+serialNumber1+"]";
}

LampInfo::~LampInfo() {
}

const int& LampInfo::getPort(){
	return port;
}

void LampInfo::setServiceName(const std::string& serviceName1){
	serviceName=serviceName1;
}

const std::string& LampInfo::getServiceName(){
	return serviceName;
}

const std::string& LampInfo::getDeviceName(){
	return deviceName;
}
const std::string& LampInfo::getProviderClassName(){
	return providerClassName;
}

const std::string& LampInfo::getSerialNumber(){
	return serialNumber;
}

}
