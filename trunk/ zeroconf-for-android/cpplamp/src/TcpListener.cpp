/*
 * TcpListener.cpp
 *
 *  Created on: 18/08/2011
 *      Author: yos
 */

#include "TcpListener.h"

namespace mta_yos_zeroconf_lamp {


using boost::asio::ip::tcp;



TcpListener::TcpListener(LampInfo info, LampHandler handler) {
	TcpListener::info=info;
	TcpListener::handler=handler;
}

TcpListener::~TcpListener() {
}

void TcpListener::startListen(){
	try
	  {
	    boost::asio::io_service io_service;

	    tcp::endpoint endpoint(tcp::v4(), info.getPort());
	    tcp::acceptor acceptor(io_service, endpoint);

	    for (;;)
	    {
	      tcp::iostream stream;
	      acceptor.accept(*stream.rdbuf());
	      stream << "test" << "1";
	    }
	  }
	  catch (std::exception& e)
	  {
	    std::cerr << e.what() << std::endl;
	  }


}



}
