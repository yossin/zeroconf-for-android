/*
 * LampHandler.cpp
 *
 *  Created on: 18/08/2011
 *      Author: yos
 */

#include "LampHandler.h"

namespace mta_yos_zeroconf_lamp {
using namespace std;
void LampHandler::printState(){
	if (state==0 ){
		cout << "Lamp is OFF";
	}
	else if (state==1){
		cout << "Lamp is ON";
	}
}

LampHandler::LampHandler() {
	state=0;
}

LampHandler::~LampHandler() {
}

void LampHandler::turnOff(){
	state=0;
	printState();
}
void LampHandler::turnOn(){
	state=1;
	printState();
}
int LampHandler::getState(){
	return state;
}

}
