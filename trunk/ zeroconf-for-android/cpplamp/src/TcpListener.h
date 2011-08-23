/*
 * TcpListener.h
 *
 *  Created on: 18/08/2011
 *      Author: yos
 */

#ifndef TCPLISTENER_H_
#define TCPLISTENER_H_
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include "LampHandler.h"
#include "LampInfo.h"
#include <boost/asio.hpp>


namespace mta_yos_zeroconf_lamp {

class TcpListener {
private:
	Lamp lamp;
	LampInfo info;
	LampHandler handler;
	int handleRequest(boost::asio::ip::tcp::socket &socket);
public:
	TcpListener(const LampInfo &info, LampHandler &handler);
	virtual ~TcpListener();
	void startListen();
};


}

#endif /* TCPLISTENER_H_ */
