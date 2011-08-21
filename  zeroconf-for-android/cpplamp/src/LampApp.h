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
#include "TcpListener.h"
#include <boost/thread.hpp>
namespace mta_yos_zeroconf_lamp {

class LampApp {
private:
	LampInfo info;
	LampHandler handler;
	TcpListener *listener;
	void doListen();
public:
	LampApp(LampInfo info);
	virtual ~LampApp();
	void run();
};
}
#endif /* LAMPAPP_H_ */
