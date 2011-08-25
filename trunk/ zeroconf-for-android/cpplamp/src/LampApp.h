/*
 * LampApp.h
 *
 *  Created on: 19/08/2011
 *      Author: yos
 */

#ifndef LAMPAPP_H_
#define LAMPAPP_H_
#include "LampInfo.h"
#include "LampHandler.h"
#include "Lamp.h"
#include "TcpListener.h"
#include "Publisher.h"
#include <boost/thread.hpp>
namespace mta_yos_zeroconf_lamp {

class LampApp {
private:
	LampInfo info;
	LampHandler handler;
	Lamp *lamp;
	TcpListener *listener;
	boost::thread *listenThread;
	void ioListen();
	void tcpListen();
	bool handleOperation(const std::string &operation);
public:
	LampApp(LampInfo &info, Lamp *lamp1);
	virtual ~LampApp();
	void run();
};
}
#endif /* LAMPAPP_H_ */
