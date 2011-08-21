/*
 * LampHandler.h
 *
 *  Created on: 18/08/2011
 *      Author: yos
 */

#ifndef LAMPHANDLER_H_
#define LAMPHANDLER_H_
#include <iostream>

namespace mta_yos_zeroconf_lamp {

class LampHandler {
private:
	int state;
	void printState();
public:
	LampHandler();
	virtual ~LampHandler();
	void turnOn();
	void turnOff();
	int getState();
};

}

#endif /* LAMPHANDLER_H_ */
