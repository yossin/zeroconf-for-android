/*
 * Publisher.cpp
 *
 *  Created on: 24/08/2011
 *      Author: yos
 */

#include "Publisher.h"
namespace mta_yos_zeroconf_lamp {

Publisher::Publisher(LampInfo &info1)
	:poller(NULL), client(NULL), group(NULL),
	 running(false), info(info1) {
	serviceType = "_device._tcp";
	txtDataDeviceName="deviceName="+info.getDeviceName();
	txtDataSerialNumber="serialNumber="+info.getSerialNumber();
	txtDataProviderClassName="providerClassName=mta.yos.zeroconf.providers.CppLampProvider";
	doStart();
}

Publisher::~Publisher() {
	stop();
	if (client)
		avahi_client_free(client);
	if (poller)
		avahi_threaded_poll_free( poller);
}


/** Called from outside of thread to stop operations.
 */
void Publisher::stop() {
	if (running) {
		int error = avahi_threaded_poll_stop(poller);
		if (error < 0) {
			std::cerr << "Error stopping avahi threaded poll ("
					<< avahi_strerror(error) << ")" << std::endl;
		}
		running = false;
	}
}

void Publisher::doStart() {
	int error;
	// create poll object
	poller = avahi_threaded_poll_new();
	if (poller == NULL) {
		std::cerr << "Could not create avahi threaded poll object." << std::endl;
		return;
	}
    // create client
    client = avahi_client_new(
    		avahi_threaded_poll_get(poller), AVAHI_CLIENT_NO_FAIL, &Publisher::clientCallback, this, &error);

    if (client == NULL) {
    	std::cerr << "Failed to create avahi client ("
    			<< avahi_strerror(error) << ")" << std::endl;
      return;
    }

    error = avahi_threaded_poll_start(poller);
    if (error < 0) {
      std::cerr << "Error starting avahi threaded poll ("
    		  << avahi_strerror(error) << ")" << std::endl;
    } else {
      running = true;
    }
  }

void Publisher::nextName() {
	char *newName = avahi_alternative_service_name(info.getServiceName().c_str());
	info.setServiceName(std::string(newName));
	avahi_free(newName);
}

void Publisher::createServices(AvahiClient *client) {
	int error;
	const char* name = info.getServiceName().c_str();
	int port = info.getPort();

	if (group == NULL) {
		group = avahi_entry_group_new(client, &Publisher::entryGroupCallback, this);

		if (group == NULL) {
			std::cerr << "Could not create avahi group ("
					<< 	avahi_strerror(avahi_client_errno(client))
					<< ")." << std::endl;
			return;
		}
	}

	if (avahi_entry_group_is_empty(group)) {
		// new group or after reset
		error = avahi_entry_group_add_service(group, // group
				AVAHI_IF_UNSPEC, // interface
				AVAHI_PROTO_UNSPEC, // protocol to announce service with
				AVAHI_PUBLISH_USE_MULTICAST, // flags
				name, // service name
				serviceType, // service type
				NULL, // domain
				NULL, // host
				port, // port
				txtDataDeviceName.c_str(),
				txtDataProviderClassName.c_str(),
				txtDataSerialNumber.c_str(),
				NULL); // list of txt records

		if (error < 0) {
			// error
			if (error == AVAHI_ERR_COLLISION) {
				// collision with local service name
				nextName();
				avahi_entry_group_reset(group);
				// retry
				createServices(client);
			} else {
				std::cerr << "Could not add service '"
						<< name << "' (" << serviceType << ") to avahi group ("
						<< avahi_strerror(error) << ")" << std::endl;
				return;
			}
		}
		// start registering the service
		error = avahi_entry_group_commit(group);
		if (error < 0) {
			std::cerr << "Could not commit avahi group "
					<< "'" << name << "'"
					<< " (" << avahi_strerror(error) << ")" << std::endl;
		}
	}
}

void Publisher::clientCallback(AvahiClient *client, AvahiClientState state, void *context) {
	Publisher *publisher = (Publisher*) context;

	// called on every server state change
	switch (state) {
	case AVAHI_CLIENT_S_RUNNING:
		// server is fine, we can register
		publisher->createServices(client);
		break;
	case AVAHI_CLIENT_S_COLLISION:
	case AVAHI_CLIENT_S_REGISTERING:
		// server not ready (collision, registering)
		// make sure we restart registration
		avahi_entry_group_reset(publisher->group);
		break;
	case AVAHI_CLIENT_CONNECTING:
		// wait
		break;
	case AVAHI_CLIENT_FAILURE:
		std::cerr << "Server connection failure: "
					<<  avahi_strerror(avahi_client_errno(client)) << std::endl;
		publisher->quit();
	}
}

void Publisher::entryGroupCallback(AvahiEntryGroup *g, AvahiEntryGroupState state, void *context) {
	Publisher *publisher = (Publisher*) context;
	const char* name = publisher->info.getServiceName().c_str();
	const int port = publisher->info.getPort();

	// callback called whenever our entry group state changes
	switch (state) {
	case AVAHI_ENTRY_GROUP_ESTABLISHED:
		std::cout << "Registration ok:" << name <<":" << port << " (" << publisher->serviceType << ")" << std::endl;
		break;
	case AVAHI_ENTRY_GROUP_COLLISION:
		// build new name
		publisher->nextName();
		// retry
		publisher->createServices(avahi_entry_group_get_client(g));
		break;
	case AVAHI_ENTRY_GROUP_FAILURE:
		std::cerr << "Registration failure ("
				<< avahi_strerror(avahi_client_errno(avahi_entry_group_get_client(g)))
				<< ")" << std::endl;
		publisher->quit();
		break;
	case AVAHI_ENTRY_GROUP_UNCOMMITED:
	case AVAHI_ENTRY_GROUP_REGISTERING:
		;
	}
}

void Publisher::quit() {
  avahi_threaded_poll_quit(poller);
  running = false;
}

}
