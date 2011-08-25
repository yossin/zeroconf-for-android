#include "Publisher.h"



int main(AVAHI_GCC_UNUSED int argc, AVAHI_GCC_UNUSED char*argv[]) {

	Publisher pub = Publisher();
	pub.publish();
	return 0;
}
