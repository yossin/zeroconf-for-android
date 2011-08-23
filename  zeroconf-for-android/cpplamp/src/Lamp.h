/*
 * Lamp.h
 *
 *  Created on: 23/08/2011
 *      Author: yos
 */

#ifndef LAMP_H_
#define LAMP_H_
#include <iostream>

namespace mta_yos_zeroconf_lamp {

class Lamp {
private:
	int state;
	void printState();
public:
	Lamp();
	virtual ~Lamp();
	void turnOn();
	void turnOff();
	const int& getState();
};

}

#endif /* LAMP_H_ */
