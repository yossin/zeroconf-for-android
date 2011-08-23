/*
 * LampHandler.h
 *
 *  Created on: 18/08/2011
 *      Author: yos
 */

#ifndef LAMPHANDLER_H_
#define LAMPHANDLER_H_
#include <iostream>
#include "Lamp.h"

namespace mta_yos_zeroconf_lamp {

class LampHandler {
private:
	int state;
	void printState();
	Lamp lamp;
public:
	LampHandler(Lamp &lamp1);
	virtual ~LampHandler();
	const int& handle(const std::string &operation);
};

}

#endif /* LAMPHANDLER_H_ */
