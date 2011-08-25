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

class Publisher {
private:
	  AvahiThreadedPoll *avahi_poll_;
	  AvahiClient       *avahi_client_;
	  AvahiEntryGroup   *avahi_group_;
	  char *name_;
	  char *service_type_;
	  uint16_t port_;
	  bool running_;
	void create_services();
	void do_start();
	void next_name();
	void quit();
	void create_services(AvahiClient *client);
	static void client_callback(AvahiClient *client, AvahiClientState state, void *context);
	static void entry_avahi_group_callback(AvahiEntryGroup *g, AvahiEntryGroupState state, void *context);
public:
	Publisher(const char *name, const char *service_type, uint16_t port);
	virtual ~Publisher();
	void stop();
	void publish();
};

#endif /* PUBLISHER_H_ */
