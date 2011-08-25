/*
 * Publisher.h
 *
 *  Created on: 24/08/2011
 *      Author: yos
 */

#ifndef PUBLISHER_H_
#define PUBLISHER_H_

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

#include <iostream>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#include <avahi-client/client.h>
#include <avahi-client/publish.h>

#include <avahi-common/alternative.h>
#include <avahi-common/thread-watch.h>
#include <avahi-common/malloc.h>
#include <avahi-common/error.h>
#include <avahi-common/timeval.h>

#include "LampInfo.h"

namespace mta_yos_zeroconf_lamp {

class Publisher {
private:
	AvahiThreadedPoll *poller;
  	AvahiClient       *client;
  	AvahiEntryGroup   *group;
  	LampInfo info;
  	std::string txtDataSerialNumber;
  	std::string txtDataDeviceName;
  	std::string txtDataProviderClassName;
  	const char* serviceType;
  	bool running;
	void doStart();
	void nextName();
	void quit();
	void createServices(AvahiClient *client);
	static void clientCallback(AvahiClient *client, AvahiClientState state, void *context);
	static void entryGroupCallback(AvahiEntryGroup *g, AvahiEntryGroupState state, void *context);
public:
	Publisher(LampInfo &info);
	virtual ~Publisher();
	void stop();
	void publish();
};

}

#endif /* PUBLISHER_H_ */
