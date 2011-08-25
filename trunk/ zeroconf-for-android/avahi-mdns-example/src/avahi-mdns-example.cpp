#include "Publisher.h"
#include <iostream>


int main(AVAHI_GCC_UNUSED int argc, AVAHI_GCC_UNUSED char*argv[]) {

	Publisher pub = Publisher("foo", "_lamp._tcp",7008);
	std::cout << "hit enter quit" << std::endl;

	std::string in;
	std::cin >> in;
	std::cout << "quitting.." << in << std::endl;
	pub.stop();
	return 0;
}
