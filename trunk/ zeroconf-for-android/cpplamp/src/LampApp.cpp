/*
 * LampApp.cpp
 *
 *  Created on: 19/08/2011
 *      Author: yos
 */

#include "LampApp.h"
namespace mta_yos_zeroconf_lamp {

LampApp::LampApp(LampInfo &info1, Lamp *lamp1)
	: info(info1), handler(lamp1){
	lamp=lamp1;
	listener=NULL;
	listenThread=NULL;
	listener = new TcpListener(info, handler);
}

LampApp::~LampApp() {
	if (listener) delete listener;
	if (listenThread) delete listenThread;
}

void LampApp::tcpListen(){
    listenThread = new boost::thread(&TcpListener::startListen, listener);
}

void LampApp::ioListen(){
	std::string operation;
	bool quit(false);
	while(quit==false){
	    getline(std::cin, operation);
	    quit=handleOperation(operation);
	}
}

bool LampApp::handleOperation(const std::string &operation) {
	if (operation.compare("quit")==0){
		return true;
	} else {
		handler.handle(operation);
		return false;
	}
}



void LampApp::run(){
	// 1. start tcp listening
	tcpListen();
	// 2. publish lamp mdns
	Publisher publisher = Publisher(info);
	// 3. listen for IO commands (till we will be asked to quit)
	ioListen();
}
}
