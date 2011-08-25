/*
 * Publisher.cpp
 *
 *  Created on: 24/08/2011
 *      Author: yos
 */

#include "Publisher.h"

Publisher::Publisher(const char *name, const char *service_type, uint16_t port)
	:avahi_poll_(NULL), avahi_client_(NULL), avahi_group_(NULL),
	 port_(port), running_(false) {
	name_ = avahi_strdup(name);
	service_type_ = avahi_strdup(service_type);
	do_start();
}

Publisher::~Publisher() {
	stop();
	if (avahi_client_)
		avahi_client_free( avahi_client_);
	if (avahi_poll_)
		avahi_threaded_poll_free( avahi_poll_);
	avahi_free( name_);
	avahi_free( service_type_);
}


/** Called from outside of thread to stop operations. */
void Publisher::stop() {
	if (running_) {
		int error = avahi_threaded_poll_stop(avahi_poll_);
		if (error < 0) {
			printf("Error stopping avahi threaded poll (%s).\n",
					avahi_strerror(error));
		}
		running_ = false;
	}
}

void Publisher::do_start() {
	int error;
	// create poll object
	avahi_poll_ = avahi_threaded_poll_new();
	if (avahi_poll_ == NULL) {
		fprintf(stderr, "Could not create avahi threaded poll object.\n");
		return;
	}
    // create client
    avahi_client_ = avahi_client_new(
    		avahi_threaded_poll_get(avahi_poll_), AVAHI_CLIENT_NO_FAIL, &Publisher::client_callback, this, &error);

    if (avahi_client_ == NULL) {
      fprintf(stderr, "Failed to create avahi client (%s).\n", avahi_strerror(error));
      return;
    }

    error = avahi_threaded_poll_start(avahi_poll_);
    if (error < 0) {
      printf("Error starting avahi threaded poll (%s).\n", avahi_strerror(error));
    } else {
      running_ = true;
    }
  }

void Publisher::next_name() {
	char *new_name = avahi_alternative_service_name(name_);
	avahi_free( name_);
	name_ = new_name;
}

void Publisher::create_services(AvahiClient *client) {
	int error;

	if (avahi_group_ == NULL) {
		avahi_group_ = avahi_entry_group_new(client, &Publisher::entry_avahi_group_callback, this);

		if (avahi_group_ == NULL) {
			fprintf(stderr, "Could not create avahi group (%s).\n",
					avahi_strerror(avahi_client_errno(client)));
			return;
		}
	}

	if (avahi_entry_group_is_empty(avahi_group_)) {
		// new group or after reset
		error = avahi_entry_group_add_service(avahi_group_, // group
				AVAHI_IF_UNSPEC, // interface
				AVAHI_PROTO_UNSPEC, // protocol to announce service with
				(AvahiPublishFlags) 0, // flags
				name_, // name
				service_type_, // service type
				NULL, // domain
				NULL, // host
				port_, // port
				NULL); // list of txt records

		if (error < 0) {
			// error
			if (error == AVAHI_ERR_COLLISION) {
				// collision with local service name
				next_name();
				avahi_entry_group_reset(avahi_group_);
				// retry
				create_services(client);
			} else {
				fprintf(
						stderr,
						"Could not add service '%s' (%s) to avahi group (%s)\n",
						name_, service_type_, avahi_strerror(error));
				return;
			}
		}
		// start registering the service
		error = avahi_entry_group_commit(avahi_group_);
		if (error < 0) {
			fprintf(stderr, "Could not commit avahi group '%s' (%s)\n", name_,
					avahi_strerror(error));
		}
	}
}

void Publisher::client_callback(AvahiClient *client, AvahiClientState state,
		void *context) {
	Publisher *impl = (Publisher*) context;

	// called on every server state change
	switch (state) {
	case AVAHI_CLIENT_S_RUNNING:
		// server is fine, we can register
		impl->create_services(client);
		break;
	case AVAHI_CLIENT_S_COLLISION:
	case AVAHI_CLIENT_S_REGISTERING:
		// server not ready (collision, registering)
		// make sure we restart registration
		avahi_entry_group_reset(impl->avahi_group_);
		break;
	case AVAHI_CLIENT_CONNECTING:
		// wait
		break;
	case AVAHI_CLIENT_FAILURE:
		fprintf(stderr, "Server connection failure: %s\n",
				avahi_strerror(avahi_client_errno(client)));
		impl->quit();
	}
}

void Publisher::entry_avahi_group_callback(AvahiEntryGroup *g,
		AvahiEntryGroupState state, void *context) {
	Publisher *impl = (Publisher*) context;
	// callback called whenever our entry group state changes
	switch (state) {
	case AVAHI_ENTRY_GROUP_ESTABLISHED:
		// done !
		printf("Registration ok: %s:%i (%s)\n", impl->name_, impl->port_,
				impl->service_type_);
		break;
	case AVAHI_ENTRY_GROUP_COLLISION:
		// build new name
		impl->next_name();
		// retry
		impl->create_services(avahi_entry_group_get_client(g));
		break;
	case AVAHI_ENTRY_GROUP_FAILURE:
		fprintf(
				stderr,
				"Registration failure (%s).\n",
				avahi_strerror(
						avahi_client_errno(avahi_entry_group_get_client(g))));
		impl->quit();
		break;
	case AVAHI_ENTRY_GROUP_UNCOMMITED:
	case AVAHI_ENTRY_GROUP_REGISTERING:
		;
	}
}

void Publisher::quit() {
  avahi_threaded_poll_quit(avahi_poll_);
  running_ = false;
}
