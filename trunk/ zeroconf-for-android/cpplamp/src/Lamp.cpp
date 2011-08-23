/*
 * Lamp.cpp
 *
 *  Created on: 23/08/2011
 *      Author: yos
 */

#include "Lamp.h"

namespace mta_yos_zeroconf_lamp {
void Lamp::printState(){
	if (state==0 ){
		std::cout << "Lamp is OFF" << std::endl;
	}
	else if (state==1){
		std::cout << "Lamp is ON" << std::endl;
	}
}

Lamp::Lamp() {
	state=0;
}

Lamp::~Lamp() {
}

void Lamp::turnOff(){
	state=0;
	printState();
}
void Lamp::turnOn(){
	state=1;
	printState();
}
const int& Lamp::getState(){
	return state;
}

}
