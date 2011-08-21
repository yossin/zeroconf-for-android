/*
 * LampApp.cpp
 *
 *  Created on: 19/08/2011
 *      Author: yos
 */

#include "LampApp.h"
namespace mta_yos_zeroconf_lamp {

LampApp::LampApp(LampInfo info) {
	LampApp::info=info;
	handler = LampHandler();
	listener = new TcpListener(info, handler);
}

LampApp::~LampApp() {
	delete listener;
}

void LampApp::doListen(){
	listener->startListen();
}

void LampApp::run(){
    boost::thread listenThread(&LampApp::doListen, this);
    listenThread.join();

}
}
