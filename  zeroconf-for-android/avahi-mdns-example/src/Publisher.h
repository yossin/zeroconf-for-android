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
#include <avahi-common/simple-watch.h>
#include <avahi-common/malloc.h>
#include <avahi-common/error.h>
#include <avahi-common/timeval.h>

class Publisher {
private:
	AvahiEntryGroup *group;
	AvahiClient *client;
	AvahiSimplePoll *simple_poll;
	char *name;
	void create_services();
	static void client_callback(AvahiClient *c, AvahiClientState state, void *context);
	static void entry_group_callback(AvahiEntryGroup *g, AvahiEntryGroupState state, void *context);
public:
	Publisher();
	virtual ~Publisher();
	void publish();
};

#endif /* PUBLISHER_H_ */
