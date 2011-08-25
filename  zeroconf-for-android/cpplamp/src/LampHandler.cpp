/*
 * LampHandler.cpp
 *
 *  Created on: 18/08/2011
 *      Author: yos
 */

#include "LampHandler.h"

namespace mta_yos_zeroconf_lamp {


LampHandler::LampHandler(Lamp *lamp1){
	lamp=lamp1;
}

LampHandler::~LampHandler() {
}

const int& LampHandler::handle(const std::string &operation){
	if (operation.compare("turn-on")==0) {
		lamp->turnOn();
	} else if (operation.compare("turn-off")==0){
		lamp->turnOff();
	} else if (operation.compare("state")==0){
	} else {
		std::cerr << "unknown operation: " << operation << std::endl;
	}
	return lamp->getState();
}

}
