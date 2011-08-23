/*
 * TcpListener.cpp
 *
 *  Created on: 18/08/2011
 *      Author: yos
 */

#include "TcpListener.h"

namespace mta_yos_zeroconf_lamp {

using boost::asio::ip::tcp;

TcpListener::TcpListener(const LampInfo &info1, LampHandler &handler1)
		:info(info1), handler(handler1){}

TcpListener::~TcpListener() {
}

int TcpListener::handleRequest(boost::asio::ip::tcp::socket &socket) {
	std::string operation;
	boost::asio::streambuf buffer;
	boost::asio::read_until(socket,buffer,'\n');
	std::istream is(&buffer);
	is >> operation;
	return handler.handle(operation);
}


void TcpListener::startListen() {
		boost::asio::io_service io_service;
		boost::asio::ip::tcp::endpoint endpoint(boost::asio::ip::tcp::v4(), info.getPort());
		boost::asio::ip::tcp::acceptor acceptor(io_service, endpoint);
		boost::asio::ip::tcp::socket socket(io_service);

		std::cout << "Listener is up" << std::endl;
		while (true) {
			try {
				acceptor.accept(socket);
				handleRequest(socket);
				socket.close();
			} catch (std::exception& e) {
				socket.close();
				std::cerr << "Exception: " << e.what() << std::endl;
			}
		}
}

/*
TcpSession::TcpSession(boost::asio::io_service& io_service)
: socket(io_service){
}

boost::asio::ip::tcp::socket& TcpSession::getSocket(){
  return socket;
}

void TcpSession::start(){
    socket.async_read_some(boost::asio::buffer(data, max_length),
        boost::bind(&TcpSession::handleRead, this,
          boost::asio::placeholders::error,
          boost::asio::placehsolders::bytes_transferred));
}

void TcpSession::handleRead(const boost::system::error_code& error,
    size_t bytes_transferred)
{
  if (!error)
  {
    boost::asio::async_write(socket,
        boost::asio::buffer(data, bytes_transferred),
        boost::bind(&TcpSession::handleWrite, this,
          boost::asio::placeholders::error));
  }
  else
  {
    delete this;
  }
}

void TcpSession::handleWrite(const boost::system::error_code& error){
  if (!error)
  {
    socket.async_read_some(boost::asio::buffer(data, max_length),
        boost::bind(&session::handle_read, this,
          boost::asio::placeholders::error,
          boost::asio::placeholders::bytes_transferred));
  }
  else
  {
    delete this;
  }
}

*/

}
