/*
 * Publisher.cpp
 *
 *  Created on: 24/08/2011
 *      Author: yos
 */

#include "Publisher.h"

Publisher::Publisher() {
	group=NULL;
	client=NULL;
	simple_poll=NULL;
	name=NULL;
}

Publisher::~Publisher() {
	if (group == NULL) delete group;
	if (client == NULL) delete client;
	if (simple_poll == NULL) delete simple_poll;
	if (name == NULL) delete name;
}

void Publisher::entry_group_callback(AvahiEntryGroup *g, AvahiEntryGroupState state, void *context) {
	Publisher *publisher =(Publisher*)context;
	assert(g == publisher->group || publisher->group == NULL);

    /* Called whenever the entry group state changes */

    switch (state) {
        case AVAHI_ENTRY_GROUP_ESTABLISHED :
            /* The entry group has been established successfully */
            fprintf(stderr, "Service '%s' successfully established.\n", publisher->name);
            break;

        case AVAHI_ENTRY_GROUP_COLLISION : {
            char *n;

            /* A service name collision happened. Let's pick a new name */
            n = avahi_alternative_service_name(publisher->name);
            avahi_free(publisher->name);
            publisher->name = n;

            fprintf(stderr, "Service name collision, renaming service to '%s'\n", publisher->name);

            /* And recreate the services */
            publisher->client=avahi_entry_group_get_client(g);
            publisher->create_services();
            break;
        }

        case AVAHI_ENTRY_GROUP_FAILURE :

            fprintf(stderr, "Entry group failure: %s\n", avahi_strerror(avahi_client_errno(avahi_entry_group_get_client(g))));

            /* Some kind of failure happened while we were registering our services */
            avahi_simple_poll_quit(publisher->simple_poll);
            break;

        case AVAHI_ENTRY_GROUP_UNCOMMITED:
        case AVAHI_ENTRY_GROUP_REGISTERING:
            ;
    }
}

void Publisher::create_services() {
    char r[128];
    int ret;

    if (!group)
        if (!(group = avahi_entry_group_new(client, &Publisher::entry_group_callback, this))) {
            fprintf(stderr, "avahi_entry_group_new() failed: %s\n", avahi_strerror(avahi_client_errno(client)));
            goto fail;
        }

    fprintf(stderr, "Adding service '%s'\n", name);

    /* Create some random TXT data */
    snprintf(r, sizeof(r), "random=%i", rand());

    /* Add the same service for BSD LPR */
    if ((ret = avahi_entry_group_add_service(group, AVAHI_IF_UNSPEC, AVAHI_PROTO_UNSPEC, AVAHI_PUBLISH_USE_MULTICAST, name, "_printer._tcp", NULL, NULL, 515, NULL)) < 0) {
        fprintf(stderr, "Failed to add _printer._tcp service: %s\n", avahi_strerror(ret));
        goto fail;
    }


    /* Tell the server to register the service */
    if ((ret = avahi_entry_group_commit(group)) < 0) {
        fprintf(stderr, "Failed to commit entry_group: %s\n", avahi_strerror(ret));
        goto fail;
    }

    return;

fail:
    avahi_simple_poll_quit(simple_poll);
}

void Publisher::client_callback(AvahiClient *c, AvahiClientState state, void *context) {
    assert(c);
    /* Called whenever the client or server state changes */
    Publisher *publisher =(Publisher*)context;

    switch (state) {
        case AVAHI_CLIENT_S_RUNNING:

            /* The server has startup successfully and registered its host
             * name on the network, so it's time to create our services */
            if (!publisher->group)
            	publisher->create_services();
            break;

        case AVAHI_CLIENT_FAILURE:

            fprintf(stderr, "Client failure: %s\n", avahi_strerror(avahi_client_errno(c)));
            avahi_simple_poll_quit(publisher->simple_poll);

            break;

        case AVAHI_CLIENT_S_COLLISION:

            /* Let's drop our registered services. When the server is back
             * in AVAHI_SERVER_RUNNING state we will register them
             * again with the new host name. */

        case AVAHI_CLIENT_S_REGISTERING:

            /* The server records are now being established. This
             * might be caused by a host name change. We need to wait
             * for our own records to register until the host name is
             * properly esatblished. */

            if (publisher->group)
                avahi_entry_group_reset(publisher->group);

            break;

        case AVAHI_CLIENT_CONNECTING:
            ;
    }

}

void Publisher::publish(){
    int error;
	/* Allocate main loop object */
	    if (!(simple_poll = avahi_simple_poll_new())) {
	        fprintf(stderr, "Failed to create simple poll object.\n");
	    }

	    name = avahi_strdup("MegaPrinter");

	    //TODO: init function to client_callback

	    /* Allocate a new client */
	    client = avahi_client_new(avahi_simple_poll_get(simple_poll), AVAHI_CLIENT_NO_FAIL, &Publisher::client_callback, this, &error);

	    /* Check wether creating the client object succeeded */
	    if (!client) {
	        fprintf(stderr, "Failed to create client: %s\n", avahi_strerror(error));
	    }


	    /* Run the main loop */
	    avahi_simple_poll_loop(simple_poll);

	fail:

	    /* Cleanup things */

	    if (client)
	        avahi_client_free(client);

	    if (simple_poll)
	        avahi_simple_poll_free(simple_poll);

	    avahi_free(name);

}
